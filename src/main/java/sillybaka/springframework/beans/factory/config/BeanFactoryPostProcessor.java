package sillybaka.springframework.beans.factory.config;

import org.springframework.core.NestedIOException;
import sillybaka.springframework.beans.factory.ConfigurableBeanFactory;
import sillybaka.springframework.beans.factory.ConfigurableListableBeanFactory;

/**
 * Date: 2022/10/19
 * Time: 22:53
 *
 * @Author SillyBaka
 * Description：beanFactory的后置处理器
 **/
public interface BeanFactoryPostProcessor {

    /**
     * 在使用BeanFactory之前（即创建bean实例之前）调用该方法。可以修改指定的beanDefinition
     * @param beanFactory 上下文中所使用的工厂对象
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);
}
