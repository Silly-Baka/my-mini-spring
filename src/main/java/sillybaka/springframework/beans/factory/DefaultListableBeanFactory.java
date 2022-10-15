package sillybaka.springframework.beans.factory;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import sillybaka.springframework.beans.factory.config.BeanDefinition;
import sillybaka.springframework.beans.factory.config.BeanDefinitionRegistry;
import sillybaka.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2022/10/13
 * Time: 20:02
 *
 * @Author SillyBaka
 * Description：核心的Bean工厂，真正可以独立地创建一个Bean
 **/
@Slf4j
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {
    /**
     * bean定义的注册表
     */
    private static final Map<String,BeanDefinition<?>> BEAN_DEFINITION_REGISTRY = new HashMap<>();

    @Override
    protected BeanDefinition<?> getBeanDefinition(String beanName) {
        if(StrUtil.isBlank(beanName)){
            throw new IllegalArgumentException("beanName不能为空");
        }

        BeanDefinition<?> beanDefinition = BEAN_DEFINITION_REGISTRY.get(beanName);

        if(beanDefinition == null){
            log.error("不存在该beanName的bean定义：{}",beanName);
            throw new IllegalArgumentException("不存在该beanName的bean定义");
        }

        return beanDefinition;
    }

    @Override
    public <T> void registerBeanDefinition(String beanName, BeanDefinition<T> beanDefinition) {
        if(StrUtil.isBlank(beanName)){
            throw new IllegalArgumentException("beanName不能为空");
        }
        BEAN_DEFINITION_REGISTRY.put(beanName,beanDefinition);
    }
}
