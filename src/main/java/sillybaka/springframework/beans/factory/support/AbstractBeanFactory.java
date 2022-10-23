package sillybaka.springframework.beans.factory.support;

import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.beans.factory.config.BeanDefinition;
import sillybaka.springframework.beans.factory.ConfigurableBeanFactory;
import sillybaka.springframework.beans.factory.config.BeanFactoryPostProcessor;
import sillybaka.springframework.beans.factory.config.BeanPostProcessor;
import sillybaka.springframework.exception.BeansException;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：抽象的Bean工厂（实现了通用的方法）
 * Date: 2022/10/11
 * Time: 20:47
 *
 * @Author SillyBaka
 **/
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String beanName) {

        Object bean = null;

        BeanDefinition<?> beanDefinition = getBeanDefinition(beanName);

        // 单例模式
        if(beanDefinition.isSingleton()){
            // 先查看缓存中有无该bean
            bean = getSingleton(beanName);

            // 如果bean为空，则说明缓存注册表中没有 需要在工厂中创建一个新的实例
            if(bean == null){
                synchronized (AbstractBeanFactory.class){
                    bean = createBean(beanName, beanDefinition);
                    addSingleton(beanName,bean);
                }
            }
        // 多例bean 创建一个新实例
        }else if(beanDefinition.isPrototype()){
            bean = createBean(beanName,beanDefinition);
        }

        if(bean == null){
            throw new BeansException("The scope of the bean [" + beanName + "] is invalid");
        }

        return bean;
    }

    /**
     * 创建名为beanName的Bean实例
     * 创建策略由实现类决定
     */
    protected abstract <T> T createBean(String beanName,BeanDefinition<T> beanDefinition);

    /**
     * 根据beanName获取它的bean定义
     */
    protected abstract BeanDefinition<?> getBeanDefinition(String beanName);

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        if(beanPostProcessor == null){
            throw new BeansException("添加的beanPostProcessor不能为空");
        }
        // 把旧的删掉
        beanPostProcessors.remove(beanPostProcessor);
        // 添加新的
        beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public List<BeanFactoryPostProcessor> getBeanFactoryBeanPostProcessors() {
        return beanFactoryPostProcessors;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
        beanFactoryPostProcessors.add(beanFactoryPostProcessor);
    }

    @Override
    public void destroySingleton(String beanName, Object bean) {
        // 委托给DisposableBeanAdapter进行处理
        new DisposableBeanAdapter(beanName,bean,getBeanDefinition(beanName)).destroy();
    }
}
