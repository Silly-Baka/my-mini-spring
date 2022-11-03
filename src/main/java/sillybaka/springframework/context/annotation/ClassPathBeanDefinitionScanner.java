package sillybaka.springframework.context.annotation;

import cn.hutool.core.util.StrUtil;
import sillybaka.springframework.beans.factory.config.BeanDefinition;
import sillybaka.springframework.context.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 扫描器，用于获取指定包内所有加了特定注解的Bean定义，并将其包装成一个完整的Bean定义
 * <p>Date: 2022/11/3
 * <p>Time: 21:08
 *
 * @Author SillyBaka
 **/
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    public Set<BeanDefinition<?>> doScan(String[] basePackages){

        Set<BeanDefinition<?>> beanDefinitionSet = new HashSet<>();
        for (String basePackage : basePackages) {
            for (BeanDefinition<?> candidate : scanCandidateComponents(basePackage)) {
                String scope = resolveBeanScope(candidate);
                if(StrUtil.isNotBlank(scope)){
                    candidate.setScope(scope);
                }
                beanDefinitionSet.add(candidate);
            }
        }

        return beanDefinitionSet;
    }

    public String resolveBeanScope(BeanDefinition<?> beanDefinition){
        Class<?> clazz = beanDefinition.getType();
        Scope scope = clazz.getAnnotation(Scope.class);
        if(scope != null){
            return scope.value();
        }
        // 默认单例
        return "singleton";
    }

    public String determineBeanName(BeanDefinition<?> beanDefinition){
        Class<?> clazz = beanDefinition.getType();
        Component annotation = clazz.getAnnotation(Component.class);
        String value = annotation.value();
        if(StrUtil.isNotBlank(value)){
            return value;
        }
        // 默认beanName为 类名首字母小写
        return StrUtil.lowerFirst(clazz.getSimpleName());
    }
}
