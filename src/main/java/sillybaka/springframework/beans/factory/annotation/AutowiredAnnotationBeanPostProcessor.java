package sillybaka.springframework.beans.factory.annotation;

import cn.hutool.core.util.StrUtil;
import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.beans.factory.BeanFactoryAware;
import sillybaka.springframework.beans.factory.ConfigurableListableBeanFactory;
import sillybaka.springframework.beans.factory.config.BeanPostProcessor;
import sillybaka.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import sillybaka.springframework.beans.factory.config.PropertyValue;
import sillybaka.springframework.beans.factory.config.PropertyValues;
import sillybaka.springframework.utils.StringValueResolver;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 用于在Bean实例化之后装配属性之前，自动解析注解@Autowired和@Value的BeanPostProcessor
 * <p>Date: 2022/11/4
 * <p>Time: 16:13
 *
 * @Author SillyBaka
 **/
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor, InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    public AutowiredAnnotationBeanPostProcessor(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 在Bean实例化之后，自动装配属性之前调用，用于修改@Value注解定义的属性
     * @param pvs 属性列表
     * @param bean bean实例
     * @param beanName bean名字
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();

        StringValueResolver[] embeddedValueResolvers = beanFactory.getEmbeddedValueResolvers();

        List<PropertyValue> propertyValueList = pvs.getPropertyValueList();

        // 处理字段上的@Value注解
        for (Field field : beanClass.getDeclaredFields()) {
            Value value = field.getAnnotation(Value.class);
            String fieldName = field.getName();

            if(value != null){
                String propertyValue = value.value();
                // 解析字符串里的占位符
                for (StringValueResolver embeddedValueResolver : embeddedValueResolvers) {
                    propertyValue = embeddedValueResolver.resolveStringValue(propertyValue);
                }
                // 如果解析完不为空 则注入bean的属性中 替换掉Bean定义中的同名属性 或直接放入
                if(StrUtil.isNotBlank(propertyValue)){
//                    BeanUtil.setFieldValue(bean,field.getName(),propertyValue);
                    int length = propertyValueList.size();

                    boolean hasSame = false;
                    // 查找pvs中的同名属性，将其替换 防止冲突
                    for (int i = 0; i < length; i++) {
                        PropertyValue pv = propertyValueList.get(i);
                        if(pv.getPropertyName().equals(fieldName)){
                            pv.setPropertyValue(propertyValue);

                            propertyValueList.set(i,pv);

                            hasSame = true;
                            break;
                        }
                    }
                    // 若没有同名属性，则直接放入
                    if(!hasSame){
                        propertyValueList.add(new PropertyValue(fieldName,propertyValue));
                    }
                }
            }
        }

        return pvs;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }
}
