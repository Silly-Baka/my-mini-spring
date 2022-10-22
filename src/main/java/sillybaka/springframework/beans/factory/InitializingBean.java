package sillybaka.springframework.beans.factory;

/**
 * beans用于实现的接口，若bean实现了该接口，BeanFactory应该在装配完bean的属性后调用它的{@link #afterPropertiesSet()}
 * Date: 2022/10/21
 * Time: 23:28
 *
 * @Author SillyBaka
 **/
public interface InitializingBean {

    /**
     * 在beanFactory为bean装配完属性后调用，可以自定义
     */
    void afterPropertiesSet();
}
