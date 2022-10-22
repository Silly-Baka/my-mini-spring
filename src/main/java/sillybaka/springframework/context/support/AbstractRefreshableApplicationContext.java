package sillybaka.springframework.context.support;

import sillybaka.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 *
 *
 * {@link AbstractApplicationContext}的子类，实现了 {@link #refresh()} 的实际逻辑
 *
 * @see AbstractApplicationContext
 *
 * @Author SillyBaka
 **/
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{

    private volatile DefaultListableBeanFactory beanFactory;

    @Override
    public void refreshBeanFactory() {
        // 如果内置BeanFactory已被创建 则注销该容器的所有bean 并且关闭该容器 重开一个新的
        if(this.beanFactory != null){
            this.beanFactory.destroySingletons();
            closeBeanFactory();
        }
        // 创建一个新的BeanFactory
        DefaultListableBeanFactory beanFactory = createBeanFactory();

        // 加载所有的beanDefinition  todo 这里是否包含特殊bean？不包含
        loadBeanDefinitions(beanFactory);

        this.beanFactory = beanFactory;
    }

    public DefaultListableBeanFactory createBeanFactory(){
        //todo 更改为用父级上下文的内置BeanFactory作为本上下文的内置BeanFactory的父亲
        return new DefaultListableBeanFactory();
    }

    @Override
    protected DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 关闭内置的BeanFactory
     */
    @Override
    protected void closeBeanFactory() {
        beanFactory = null;
    }

    /**
     * 将BeanDefinition加载到指定的BeanFactory中。此功能委托给BeanDefinitionReader来实现
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

}
