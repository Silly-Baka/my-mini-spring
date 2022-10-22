package sillybaka.springframework.beans.factory;

/**
 * Description：实现了该接口的Bean可以感知到其所属的BeanFactory
 *
 * @Author SillyBaka
 **/
public interface BeanFactoryAware extends Aware{

    /**
     * 为了获取所属的beanFactory而设置的回调函数
     * 在为bean填充完属性后，在调用其初始化方法{@link InitializingBean#afterPropertiesSet}或自定义初始化方法 之前调用
     *
     * @see InitializingBean
     * @param beanFactory spring回调该函数时输入的beanFactory
     */
    void setBeanFactory(BeanFactory beanFactory);
}
