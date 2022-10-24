package sillybaka.springframework.context.event;

import sillybaka.springframework.context.ApplicationContext;

/**
 * ApplicationContext被关闭时触发的事件
 *
 * @Author SillyBaka
 **/
public class ContextClosedEvent extends ApplicationContextEvent {

    public ContextClosedEvent(Object source, ApplicationContext applicationContext) {
        super(source, applicationContext);
    }
}
