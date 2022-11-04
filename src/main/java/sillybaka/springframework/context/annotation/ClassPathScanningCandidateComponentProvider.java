package sillybaka.springframework.context.annotation;

import cn.hutool.core.util.ClassUtil;
import sillybaka.springframework.beans.factory.config.BeanDefinition;
import sillybaka.springframework.context.stereotype.Component;
import sillybaka.springframework.utils.PropertyUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 提供了从指定basePackage中读取所有候选Component组件（即带有@Component或其子注解的类）的基本实现
 * <p>Date: 2022/11/3
 * <p>Time: 21:09
 *
 * @Author SillyBaka
 **/
public class ClassPathScanningCandidateComponentProvider {

    /**
     * 从指定的包读取所有候选components的bean定义
     * @param basePackage 指定的包
     * @return 指定包下所有Component的bean定义
     */
    protected Set<BeanDefinition<?>> scanCandidateComponents(String basePackage){
        Set<Class<?>> classSet = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        Set<BeanDefinition<?>> beanDefinitionSet = new HashSet<>();
        for (Class<?> clazz : classSet) {
            beanDefinitionSet.add(parseClassToBeanDefinition(clazz));
        }
        return beanDefinitionSet;
    }

    /**
     * 将指定类转换成相应的BeanDefinition
     * @param clazz 指定类
     */
    protected BeanDefinition<?> parseClassToBeanDefinition(Class clazz){
        BeanDefinition<?> beanDefinition = new BeanDefinition<>();
        beanDefinition.setType(clazz);

        // 还要将该bean的所有属性注册进IOC容器
        PropertyUtils.addAllPropertyDescriptor(clazz);

        return beanDefinition;
    }
}
