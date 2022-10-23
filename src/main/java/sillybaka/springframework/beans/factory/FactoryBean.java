package sillybaka.springframework.beans.factory;

/**
 * 由{@link BeanFactory}内部某些对象实现的接口，实现了这个接口的对象是管理某个Bean的BeanFactory
 * 内部Bean的生命周期由自己管理，但自己作为一个FactoryBean被IOC容器管理
 * 内部Bean可以实现singleton、prototype两种模式
 *
 * @Author SillyBaka
 **/
public interface FactoryBean<T> {

    /**
     * 获取内部bean
     */
    T getObject();

    /**
     * 获取内部bean的类型
     */
    Class<T> getObjectType();

    /**
     * 当前FactoryBean是否是单例的，默认为true，可被实现类重写
     */
    default boolean isSingleton(){
        return true;
    }

}
