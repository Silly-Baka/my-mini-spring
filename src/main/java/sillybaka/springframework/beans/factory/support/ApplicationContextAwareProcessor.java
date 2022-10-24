package sillybaka.springframework.beans.factory.support;

import sillybaka.springframework.beans.factory.ApplicationContextAware;
import sillybaka.springframework.beans.factory.config.BeanPostProcessor;
import sillybaka.springframework.context.ConfigurableApplicationContext;

/**
 * Description：为实现了{@link sillybaka.springframework.beans.factory.ApplicationContextAware}接口的Bean回调注入ApplicationContext的BeanPostProcessor
 * 将在ApplicationContext启动时自动注册进其内置的BeanFactory
 *
 * @Author SillyBaka
 **/
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    protected final ConfigurableApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T postProcessBeforeInitialization(T bean, String beanName) {
        if(bean instanceof ApplicationContextAware){
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }

        return bean;
    }
}
