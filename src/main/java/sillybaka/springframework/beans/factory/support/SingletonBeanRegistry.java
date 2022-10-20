package sillybaka.springframework.beans.factory.support;

/**
 * Date: 2022/10/11
 * Time: 20:18
 *
 * @Author SillyBaka
 * Description：bean实例的单例注册表 --> 用于缓存bean实例
 **/
public interface SingletonBeanRegistry {
    /**
     * 注册bean到注册表中
     * @param beanName bean的名字
     * @param bean bean的实例对象
     */
    void registerBean(String beanName, Object bean);

    /**
     * 根据bean的类型来获得单例实例对象
     * @param beanClass bean的类型
     * @return bean的实例
     */
    Object getSingletonBean(Class<?> beanClass);

    /**
     * 根据bean的名字来获得单例实例对象
     * @param beanName bean的名字
     * @return bean的实例
     */
    Object getSingletonBean(String beanName);

    /**
     * 反注册所有的单例bean
     */
    void destroySingletonBeans();
}
