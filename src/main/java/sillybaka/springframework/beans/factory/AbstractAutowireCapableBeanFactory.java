package sillybaka.springframework.beans.factory;

import lombok.extern.slf4j.Slf4j;
import sillybaka.springframework.beans.definition.BeanDefinition;


/**
 * Date: 2022/10/13
 * Time: 19:40
 *
 * @Author SillyBaka
 * Description：抽象的自动装配Bean工厂 --> 定义了使用bean定义创建bean实例的抽象方法
 **/
@Slf4j
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{
    @Override
    protected <T> T createBean(String beanName, BeanDefinition<T> beanDefinition) {
        return doCreate(beanName,beanDefinition);
    }

    /**
     * 创建Bean实例的实际逻辑，由子类实现
     * @param beanName
     * @param beanDefinition
     * @param <T>
     * @return
     */
    public abstract <T> T doCreate(String beanName,BeanDefinition<T> beanDefinition);
}
