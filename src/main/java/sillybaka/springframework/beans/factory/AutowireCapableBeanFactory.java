package sillybaka.springframework.beans.factory;

import org.springframework.lang.Nullable;
import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.beans.factory.config.BeanDefinition;
import sillybaka.springframework.beans.factory.config.PropertyValues;

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
     * @param beanName bean的名字
     * @param existingBean 已创建的bean实例对象
     * @param beanDefinition bean定义
     * @param pvs 指定的属性，可为空
     */
    void autoWirePropertyValues(String beanName, Object existingBean, BeanDefinition<?> beanDefinition, @Nullable PropertyValues pvs);
}
