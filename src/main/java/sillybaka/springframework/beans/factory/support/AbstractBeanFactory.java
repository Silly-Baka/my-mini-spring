package sillybaka.springframework.beans.factory.support;

import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.beans.factory.FactoryBean;
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
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements BeanFactory, ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName,null);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) {
        return doGetBean(beanName,requiredType);
    }

    protected <T> T doGetBean(String beanName, Class<T> requiredType){
        Object beanInstance;

        BeanDefinition<?> beanDefinition = getBeanDefinition(beanName);

        // 在缓存中获取共享的bean实例
        Object sharedInstance = getSingleton(beanName);

        if(sharedInstance != null){
            beanInstance = getObjectForBeanInstance(sharedInstance,beanName);
        }
        // 否则要准备创建新的bean实例
        else {
            beanInstance = createBean(beanName,beanDefinition);

            // 单例 需要缓存
            if(beanDefinition.isSingleton()){
                addSingleton(beanName,beanInstance);
            }

            beanInstance = getObjectForBeanInstance(beanInstance,beanName);
        }

        return adaptBeanInstance(beanName,beanInstance,requiredType);
    }

    @SuppressWarnings("unchecked")
    protected <T> T adaptBeanInstance(String beanName, Object beanInstance, Class<?> requiredType) {
        // 如果bean不是指定类型的实例 需要判断能否转化成指定类型
        if(requiredType != null && !requiredType.isInstance(beanInstance)){
            // 判断能够转化为对应类型的对象
        }

        return (T) beanInstance;
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

//    @Override
//    public void destroySingleton(String beanName, Object bean) {
//        // 委托给DisposableBeanAdapter进行处理
//        new DisposableBeanAdapter(beanName,bean,getBeanDefinition(beanName)).destroy();
//    }

    /**
     * 从bean实例中获取bean
     * 用于特殊bean的处理（如FactoryBean、prototype作用域的bean等）
     * @param beanInstance bean实例
     */
    protected Object getObjectForBeanInstance(Object beanInstance,String beanName){

        Object instance;
        // 如果该实例是FactoryBean，则获取内置的bean
        if(beanInstance instanceof FactoryBean){
            // 先从缓存中获取
            Object cacheInstance = getCachedObjectForFactoryBean(beanName);
            if(cacheInstance != null){
                return cacheInstance;
            }
            // 若缓存中没有
            FactoryBean<?> factory = (FactoryBean<?>) beanInstance;

            instance = getObjectFromFactoryBean(beanName,factory);

        }else {
            instance = beanInstance;
        }

        return instance;
    }
}
