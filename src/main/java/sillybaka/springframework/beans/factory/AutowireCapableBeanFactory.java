package sillybaka.springframework.beans.factory;

import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.beans.factory.config.BeanDefinition;

/**
 * Date: 2022/10/16
 * Time: 19:44
 *
 * @Author SillyBaka
 * Description：为BeanFactory提供自动配置Bean属性的接口
 **/
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 按照Bean的定义为已创建的bean实例对象自动装配属性 (底层使用setter注入）
     * @param beanName
     * @param existingBean 已创建的bean实例对象
     * @param beanDefinition bean定义
     */
    <T> void autoWirePropertyValues(String beanName,T existingBean, BeanDefinition<T> beanDefinition);
}
