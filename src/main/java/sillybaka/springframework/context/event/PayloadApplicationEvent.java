package sillybaka.springframework.context.event;

import sillybaka.springframework.context.ApplicationEvent;

/**
 * 基于{@link ApplicationEvent}的子类，能够携带任意载荷的事件实体类，是一个通用的事件类
 *
 * @Author SillyBaka
 **/
public class PayloadApplicationEvent<T> extends ApplicationEvent{
    /**
     * 该事件的载荷
     */
    private final T payload;

    public PayloadApplicationEvent(T payload) {
        super(payload);
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
}
