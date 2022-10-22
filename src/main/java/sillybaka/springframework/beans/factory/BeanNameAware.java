package sillybaka.springframework.beans.factory;

/**
 * Description：实现了该接口的bean可以通过回调感知到自己所属的BeanName
 * Date: 2022/10/22
 * Time: 20:12
 *
 * @Author SillyBaka
 **/
public interface BeanNameAware extends Aware{

    /**
     * 为感知到BeanName而设置的回调函数，由BeanFactory调用
     * 在为bean填充完属性后，在调用其初始化方法{@link InitializingBean#afterPropertiesSet}或自定义初始化方法 之前调用
     * @param beanName 在容器中所属的beanName
     */
    void setBeanName(String beanName);
}
