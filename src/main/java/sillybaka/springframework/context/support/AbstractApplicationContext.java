package sillybaka.springframework.context.support;

import sillybaka.springframework.beans.factory.ConfigurableListableBeanFactory;
import sillybaka.springframework.beans.factory.config.BeanFactoryPostProcessor;
import sillybaka.springframework.beans.factory.config.BeanPostProcessor;
import sillybaka.springframework.beans.factory.support.ApplicationContextAwareProcessor;
import sillybaka.springframework.beans.factory.support.DefaultListableBeanFactoryBean;
import sillybaka.springframework.context.ConfigurableApplicationContext;
import sillybaka.springframework.core.io.DefaultResourceLoader;
import sillybaka.springframework.exception.BeansException;

/**
 * Date: 2022/10/20
 * Time: 15:52
 *
 * @Author SillyBaka
 * Description：ApplicationContext接口的抽象实现，简单地实现了通用方法。
 *             使用模板方法设计模式，由子类实现具体的方法逻辑
 **/
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    //todo 模板方法 只提供了抽象逻辑 具体逻辑由子类实现 -- 有待完善
    @Override
    public void refresh() {

        // 获取BeanFactory
        ConfigurableListableBeanFactory beanFactory = obtainBeanFactory();

        // 为新的内置BeanFactory填充特殊内置属性
        prepareBeanFactory(beanFactory);

        // 在实例化bean之前调用beanFactoryPostProcessor 看是否需要修改beanDefinition
        invokeBeanFactoryPostProcessors(beanFactory);

        // 注册BeanPostProcessors进当前内置的BeanFactory
        registerBeanPostProcessors(beanFactory);

        // 预实例化所有的bean
        beanFactory.preInitiateSingletons();

    }

    /**
     * 为指定的BeanFactory填充特殊属性，一般用于初始化内置BeanFactory
     */
    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

    }

    /**
     * 启动并获取BeanFactory，若已启动则重启BeanFactory
     */
    protected ConfigurableListableBeanFactory obtainBeanFactory(){
        // 启动上下文 创建内置的beanFactory 从xml文件中加载所有的beanDefinition（包括特殊bean）
        refreshBeanFactory();
        // 获取BeanFactory
        return getBeanFactory();
    }

    /**
     * 启动上下文内置的BeanFactory
     * 由子类 AbstractRefreshableApplicationContext 实现
     */
    protected abstract void refreshBeanFactory();

    /**
     * 获取当前上下文的内置BeanFactory
     * 由子类 AbstractRefreshableApplicationContext 实现
     */
    protected abstract DefaultListableBeanFactoryBean getBeanFactory();

    /**
     * 执行指定BeanFactory的所有后置处理器
     */
    public void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory){
        String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true);
        for (String postProcessorName : postProcessorNames) {
            Object bean = beanFactory.getBean(postProcessorName);
            if(bean instanceof BeanFactoryPostProcessor){
                ((BeanFactoryPostProcessor) bean).postProcessBeanFactory(beanFactory);
            }else {
                throw new BeansException("the bean name with: ["+postProcessorName+"] is not a BeanFactoryPostProcessor");
            }
        }
    }

    /**
     * 为指定的beanFactory注册所有BeanPostProcessor
     * @param beanFactory
     */
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory){
        String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true);
        for (String postProcessorName : postProcessorNames) {
            // 由指定BeanFactory管理该BeanPostProcessor bean
            Object bean = beanFactory.getBean(postProcessorName);
            if(bean instanceof BeanPostProcessor){
                beanFactory.addBeanPostProcessor((BeanPostProcessor) bean);
            }else {
                throw new BeansException("the bean name with: ["+postProcessorName+"] is not a BeanPostProcessor");
            }
        }
    }

    /**
     * 模板方法 实际逻辑交给子类实现
     */
    @Override
    public void close() {

        // 先销毁所有的bean实例
        destroyBeans();
        // 再关闭内置的BeanFactory
        closeBeanFactory();

    }

    /**
     * 销毁所有的bean实例
     */
    protected void destroyBeans(){
        getBeanFactory().destroySingletons();
    }

    /**
     * 关闭内置的BeanFactory
     */
    protected abstract void closeBeanFactory();

    /**
     * 委托给内置BeanFactory处理
     * @param beanName bean的名字
     */
    @Override
    public Object getBean(String beanName) {
        return getBeanFactory().getBean(beanName);
    }

    /**
     * 委托给内置BeanFactory处理
     * @param type bean类型
     * @param includeNonSingleton 是否包含非单例的bean
     */
    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingleton) {
        return getBeanFactory().getBeanNamesForType(type,includeNonSingleton);
    }
}
