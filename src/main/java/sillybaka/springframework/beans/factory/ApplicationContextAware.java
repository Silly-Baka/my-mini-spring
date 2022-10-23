package sillybaka.springframework.beans.factory;

import sillybaka.springframework.beans.factory.config.BeanDefinition;
import sillybaka.springframework.beans.factory.support.AbstractAutowireCapableBeanFactoryBean;
import sillybaka.springframework.context.ApplicationContext;

/**
 * Description：实现了该接口的Bean可以通过回调感知到其所属的ApplicationContext
 * Date: 2022/10/22
 * Time: 19:52
 *
 * @Author SillyBaka
 **/
public interface ApplicationContextAware extends Aware{

    /**
     * 为了获取Bean所属的ApplicationContext而设置的回调函数，在执行自定义初始化方法{@link AbstractAutowireCapableBeanFactoryBean#invokeInitMethods(Object, String, BeanDefinition)}之前
     * 通过BeanPostProcessor来调用
     *
     * @see AbstractAutowireCapableBeanFactoryBean
     * @param applicationContext 所属的上下文对象
     */
    void setApplicationContext(ApplicationContext applicationContext);
}
