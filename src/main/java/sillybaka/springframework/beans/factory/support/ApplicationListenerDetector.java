package sillybaka.springframework.beans.factory.support;

import sillybaka.springframework.beans.factory.config.BeanPostProcessor;
import sillybaka.springframework.context.ApplicationListener;
import sillybaka.springframework.context.ConfigurableApplicationContext;



/**
 * 基于{@link ApplicationContextAwareProcessor}的子类，可以感知到上下文<p>
 * 实现了{@link BeanPostProcessor}接口，用于检测bean是否是监听器，并与广播者进行交互
 *
 * @Author SillyBaka
 **/
public class ApplicationListenerDetector extends ApplicationContextAwareProcessor implements BeanPostProcessor {

    public ApplicationListenerDetector(ConfigurableApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public <T> T postProcessAfterInitialization(T bean, String beanName) {
        // 检查是否是监听器
        if(bean instanceof ApplicationListener){
            //若是 则注册进上下文中
            applicationContext.addApplicationListener((ApplicationListener<?>) bean);
        }
        return bean;
    }
}
