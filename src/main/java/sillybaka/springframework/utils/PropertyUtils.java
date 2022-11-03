package sillybaka.springframework.utils;

import cn.hutool.core.util.StrUtil;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.bcel.internal.generic.LNEG;

import javax.annotation.PostConstruct;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2022/10/15
 * Time: 16:41
 *
 * @Author SillyBaka
 * Description：用于Bean的Property属性的工具类（底层使用PropertyDescriptor
 **/
public class PropertyUtils {
    /**
     * 使用Bean类获取其所有属性的PropertyDescriptor Map
     */
    private static final Map<Class<?>, Map<String,PropertyDescriptor>> BEANS_PROPERTY_MAP;

    /**
     * 冗余项：用于获取Bean类的每种属性类型
     */
    private static final Map<Class<?>, Map<String,Class<?>>> BEANS_PROPERTY_TYPE_MAP;

    static {
        BEANS_PROPERTY_MAP = new HashMap<>();
        BEANS_PROPERTY_TYPE_MAP = new HashMap<>();
    }

    public static void addBeanPropertyMap(Class<?> clazz, Map<String,PropertyDescriptor> map){
        BEANS_PROPERTY_MAP.putIfAbsent(clazz,map);
    }

    /**
     * 根据指定类的clazz获取其propertyDescriptorMap
     * @param clazz
     * @return
     */
    public static Map<String,PropertyDescriptor> getBeanPropertyMap(Class<?> clazz){
        Map<String, PropertyDescriptor> descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
        if(descriptorMap == null){
            // 懒汉式单例加载
            synchronized (PropertyUtils.class){
                descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
                if(descriptorMap == null){
                    addAllPropertyDescriptor(clazz);
                }
                descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
            }
        }
        return descriptorMap;
    }


    /**
     * 根据指定类的class将其所有属性的PropertyDescriptor添加到map中 （没有内嵌Bean的才能调用）
     * 生命周期开始为在XMl扫描时按需调用，只能注入基本属性
     * @param clazz 指定类的class
     */
    public static void addAllPropertyDescriptor(Class<?> clazz){
        Map<String, PropertyDescriptor> descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
        if(descriptorMap == null){
            BEANS_PROPERTY_MAP.putIfAbsent(clazz,new HashMap<>());
            descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
        }
        // 获取该类的所有属性的Descriptor
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            String propertyName = field.getName();
            try {
                Class<?> propertyType = field.getType();

                PropertyDescriptor pv = new PropertyDescriptor(propertyName, clazz);

                descriptorMap.put(propertyName,pv);
                addPropertyType(clazz, propertyName, propertyType);

            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据指定类的的class和propertyName添加PropertyDescriptor到map中，可以指定property类型
     * @param clazz 指定类的class
     * @param propertyName 属性名
     * @param anotherType  若不为空 则设置为该property的别类型
     */
    public static void addPropertyDescriptor(Class<?> clazz, String propertyName, @Nullable Class<?> anotherType){
        PropertyDescriptor pd;

        try {
            Field field = clazz.getDeclaredField(propertyName);

            Class<?> propertyType = field.getType();

            pd = new PropertyDescriptor(propertyName, clazz);

            // 添加进map中
            Map<String, PropertyDescriptor> descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
            if(descriptorMap == null){
                BEANS_PROPERTY_MAP.putIfAbsent(clazz,new HashMap<>());
                descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
            }
            descriptorMap.put(propertyName,pd);

            // 检查是否有设置另外的类型
            if(anotherType != null){
                addPropertyType(clazz, propertyName, anotherType);
            }else {
                addPropertyType(clazz,propertyName,propertyType);
            }

        } catch (NoSuchFieldException | IntrospectionException e) {
            e.printStackTrace();
        }
    }

    public static void addPropertyType(Class<?> clazz, String propertyName, Class<?> propertyType){
        Map<String, Class<?>> propertyTypeMap = BEANS_PROPERTY_TYPE_MAP.get(clazz);
        if(propertyTypeMap == null){
            synchronized (PropertyUtils.class){
                BEANS_PROPERTY_TYPE_MAP.putIfAbsent(clazz,new HashMap<>());
                propertyTypeMap = BEANS_PROPERTY_TYPE_MAP.get(clazz);
            }
        }
        propertyTypeMap.put(propertyName,propertyType);
    }

    public static Class<?> getPropertyType(Class<?> clazz, String propertyName){
        Map<String, Class<?>> propertyTypeMap = BEANS_PROPERTY_TYPE_MAP.get(clazz);
        if(propertyTypeMap == null){
            throw new IllegalArgumentException("该Bean类型不存在");
        }
        Class<?> propertyType = propertyTypeMap.get(propertyName);
        if(propertyType == null){
            throw new IllegalArgumentException("该Bean不存在该属性名: " + propertyName);
        }
        return propertyType;
    }

    public static Map<String,Class<?>> getPropertyTypeMap(Class<?> clazz){
        return BEANS_PROPERTY_TYPE_MAP.get(clazz);
    }

    /**
     * 将传入的属性值按照属性类型进行转换 并返回转换后的值，只限于基本类
     * @param propertyValue 属性值
     * @param propertyType 属性类型
     * @return 转换后的属性值
     */
    public static <T> T propertyValueTypeConversion(String propertyValue, Class<T> propertyType){
        if(propertyType.isAssignableFrom(Integer.TYPE) || propertyType.isAssignableFrom(Integer.class)){
            return (T) Integer.valueOf(propertyValue);
        }
        if(propertyType.isAssignableFrom(Double.TYPE) || propertyType.isAssignableFrom(Double.class)){
            return (T) Double.valueOf(propertyValue);
        }
        if(propertyType.isAssignableFrom(Long.TYPE) || propertyType.isAssignableFrom(Long.class)){
            return (T) Long.valueOf(propertyValue);
        }
        if(propertyType.isAssignableFrom(Boolean.TYPE) || propertyType.isAssignableFrom(Boolean.class)){
            return (T) Boolean.valueOf(propertyValue);
        }

        return (T) propertyValue;
    }

    public static void initClazzPropertyMap(Class<?> beanType){
        synchronized (BEANS_PROPERTY_MAP){
            if(!BEANS_PROPERTY_MAP.containsKey(beanType)){
                BEANS_PROPERTY_MAP.putIfAbsent(beanType,new HashMap<>());
            }
        }
        synchronized (BEANS_PROPERTY_TYPE_MAP){
            if(!BEANS_PROPERTY_TYPE_MAP.containsKey(beanType)){
                BEANS_PROPERTY_TYPE_MAP.putIfAbsent(beanType,new HashMap<>());
            }
        }
    }
}
