package sillybaka.springframework.context.event;

import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.beans.factory.BeanFactoryAware;
import sillybaka.springframework.beans.factory.ConfigurableBeanFactory;
import sillybaka.springframework.context.ApplicationEvent;
import sillybaka.springframework.context.ApplicationEventMulticaster;
import sillybaka.springframework.context.ApplicationListener;
import sillybaka.springframework.exception.BeansException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link ApplicationEventMulticaster}接口的抽象实现，提供了注册监听器的功能、判断监听类型的通用方法 <p>
 * 广播事件的方法{@link ApplicationEventMulticaster#multicastEvent(ApplicationEvent)}实际逻辑由子类实现
 *
 * @Author SillyBaka
 **/
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    /**
     * 用于保存绑定的监听者，在同一上下文中无法注册完全相同的监听者
     */
    protected final Set<ApplicationListener<?>> applicationListeners = new HashSet<>(16);

    private ConfigurableBeanFactory beanFactory;

    @Override
    public void addApplicationListener(ApplicationListener<?> applicationListener) {
        applicationListeners.add(applicationListener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> applicationListener) {
        applicationListeners.remove(applicationListener);
    }

    @Override
    public void addApplicationListenerBean(String beanName) {
        if(beanFactory == null){
            throw new BeansException("the inner beanFactory is null");
        }
        Object bean = beanFactory.getBean(beanName);
        // 如果不是监听器bean
        if(!(bean instanceof ApplicationListener)){
            throw new BeansException("the bean named ["+ beanName +"] is not a ApplicationListener");
        }
        applicationListeners.add((ApplicationListener<?>) bean);
    }

    @Override
    public void removeApplicationListenerBean(String beanName) {

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    /**
     * 检查目标监听器是否对指定事件感兴趣
     * @param listener 要检查的监听器
     * @param event 指定事件
     */
    //todo 这里的判断逻辑可以优化 可以先将event解析成type 再使用type去检查
    protected boolean supportsEvent(ApplicationListener<?> listener, ApplicationEvent event){
        // 检查当前listener监听的事件类型（检查泛型的类型）
        Type[] genericInterfaces = listener.getClass().getGenericInterfaces();

        Class<? extends ApplicationEvent> eventType = event.getClass();

        for (Type type : genericInterfaces) {
            // 如果是参数型类型
            if(type instanceof ParameterizedType){
                // 获得实际代表的类型列表
                Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
                for (Type actualType : actualTypes) {
                    String actualTypeName = actualType.getTypeName();
                    try {
                        if(Class.forName(actualTypeName).isAssignableFrom(eventType)){
                            return true;
                        }
                    } catch (ClassNotFoundException e) {
                        throw new BeansException("wrong event type name:"+actualTypeName,e);
                    }
                }
            }
        }
        return false;
    }
}
