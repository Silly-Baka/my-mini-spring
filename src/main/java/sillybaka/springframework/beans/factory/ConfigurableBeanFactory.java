package sillybaka.springframework.beans.factory;

import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.beans.factory.config.BeanFactoryPostProcessor;
import sillybaka.springframework.beans.factory.config.BeanPostProcessor;
import sillybaka.springframework.beans.factory.support.SingletonBeanRegistry;

import java.util.List;

/**
 * Date: 2022/10/16
 * Time: 19:36
 *
 * @Author SillyBaka
 * Description：提供对BeanFactory进行配置的接口
 **/
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {

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


}
