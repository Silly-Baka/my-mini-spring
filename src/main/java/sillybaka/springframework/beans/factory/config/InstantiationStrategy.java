package sillybaka.springframework.beans.factory.config;

import sillybaka.springframework.beans.factory.config.BeanDefinition;

/**
 * Date: 2022/10/13
 * Time: 21:47
 *
 * @Author SillyBaka
 * Description：实例化策略接口
 **/
public interface InstantiationStrategy {

    /**
     * 使用某种策略实例化Bean对象
     * @param beanDefinition
     * @param <T>
     * @return
     */
    <T> T instantiation(BeanDefinition<T> beanDefinition);
}
