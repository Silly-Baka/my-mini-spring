package sillybaka.springframework.context.event;

/**
 * ApplicationContext被关闭时触发的事件
 *
 * @Author SillyBaka
 **/
public class ContextClosedEvent extends ApplicationContextEvent {

    public ContextClosedEvent(Object source) {
        super(source);
    }
}
