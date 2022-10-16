package sillybaka.springframework.beans.factory.support;

import sillybaka.springframework.beans.factory.config.BeanDefinition;
import sillybaka.springframework.beans.factory.config.InstantiationStrategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Date: 2022/10/13
 * Time: 21:55
 *
 * @Author SillyBaka
 * Description：使用jdk提供的反射来实例化对象
 **/
public class SimpleInstantiationStrategy implements InstantiationStrategy {
    @Override
    public <T> T instantiation(BeanDefinition<T> beanDefinition) {
        Class<T> clazz = beanDefinition.getType();

        T instance = null;
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();

            instance = constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return instance;
    }
}
