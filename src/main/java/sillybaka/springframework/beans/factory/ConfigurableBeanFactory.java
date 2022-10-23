package sillybaka.springframework.beans.factory;

import sillybaka.springframework.beans.factory.config.BeanFactoryPostProcessor;
import sillybaka.springframework.beans.factory.config.BeanPostProcessor;
import sillybaka.springframework.beans.factory.support.SingletonBeanRegistry;

import java.util.List;

/**
 * Description：提供对BeanFactory进行配置的接口以及属性
 * Date: 2022/10/16
 * Time: 19:36
 *
 * @Author SillyBaka
 **/
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {

    /**
     * 单例作用域的标识符
     */
    String SCOPE_SINGLETON = "singleton";
    /**
     * 多例作用域的标识符
     */
    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 获取当前容器的BeanPostProcessor列表
     * @return
     */
    List<BeanPostProcessor> getBeanPostProcessors();

    /**
     * 获取当前容器的BeanFactoryPostProcessor列表
     * @return
     */
    List<BeanFactoryPostProcessor> getBeanFactoryBeanPostProcessors();

    /**
     * 向当前容器添加BeanPostProcessor
     * @param beanPostProcessor 指定的bean后置处理器
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 向当前容器添加BeanFactoryPostProcessor
     * @param beanFactoryPostProcessor
     */
    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor);

    /**
     * 摧毁当前BeanFactory的所有bean的原型单例
     */
    void destroySingletons();

    /**
     * 预实例化所有的bean
     */
    void preInitiateSingletons();

    /**
     * 对指定的bean执行自定义destroy方法
     * @param beanName
     * @param bean
     */
    void destroySingleton(String beanName,Object bean);

}
