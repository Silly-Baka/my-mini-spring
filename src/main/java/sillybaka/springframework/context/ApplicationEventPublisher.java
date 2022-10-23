package sillybaka.springframework.context;

/**
 * Spring事件机制中的发布者，提供了发布事件的接口。
 * 事件最终会被发送到广播者处{@link ApplicationEventMulticaster}
 *
 * @Author SillyBaka
 **/
public interface ApplicationEventPublisher {

    /**
     * 发布事件 最终发送到广播者处
     */
    void publishEvent(ApplicationEvent event);

}
