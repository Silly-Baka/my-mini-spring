package sillybaka.springframework.context;

/**
 * Spring事件机制中的广播者，提供将事件转发给事件监听者，让监听者和事件类型相绑定的接口
 *
 * @Author SillyBaka
 **/
public interface ApplicationEventMulticaster {

    <E> void addApplicationListener(Class<E> eventType, ApplicationListener<? extends ApplicationEvent> listener);


}
