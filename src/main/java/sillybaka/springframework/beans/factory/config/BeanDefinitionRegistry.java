package sillybaka.springframework.beans.factory.config;


/**
 * Date: 2022/10/13
 * Time: 19:36
 *
 * @Author SillyBaka
 * Description：bean定义的注册表 --> 保存bean定义
 **/
public interface BeanDefinitionRegistry {

    /**
     * 向beanName注册它的bean定义
     * @param beanName
     * @param beanDefinition
     * @param <T>
     */
    <T> void registerBeanDefinition(String beanName, BeanDefinition<T> beanDefinition);
}