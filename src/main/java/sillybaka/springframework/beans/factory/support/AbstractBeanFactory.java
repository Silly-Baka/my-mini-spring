package sillybaka.springframework.beans.factory.support;

import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.beans.factory.config.BeanDefinition;

/**
 * Date: 2022/10/11
 * Time: 20:47
 *
 * @Author SillyBaka
 * Description：抽象的Bean工厂
 **/
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    @Override
    public Object getBean(String beanName) {

        Object bean = getSingletonBean(beanName);
        // 如果bean为空，则说明注册表中没有 需要在工厂创建
        if(bean == null){
            // 双重校验
            synchronized (AbstractBeanFactory.class){
                bean = getSingletonBean(beanName);
                if(bean == null){
                    bean = createBean(beanName,getBeanDefinition(beanName));
                    // 注册进注册表
                    registerBean(beanName,bean);
                }
            }
        }
        return bean;
    }

    /**
     * 创建名为beanName的Bean实例
     * 创建策略由实现类决定
     */
    protected abstract <T> T createBean(String beanName, BeanDefinition<T> beanDefinition);

    /**
     * 根据beanName获取它的bean定义
     */
    protected abstract BeanDefinition<?> getBeanDefinition(String beanName);
}
