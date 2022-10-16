package sillybaka.springframework.beans.factory.support;

import sillybaka.springframework.beans.factory.config.BeanDefinition;
import sillybaka.springframework.beans.factory.config.InstantiationStrategy;

/**
 * Date: 2022/10/13
 * Time: 21:58
 *
 * @Author SillyBaka
 * Description：使用cglib的动态代理生成实例对象
 **/
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {
    @Override
    public <T> T instantiation(BeanDefinition<T> beanDefinition) {
        return null;
    }
}
