package sillybaka.springframework.context.event;

import sillybaka.springframework.context.ApplicationEvent;

/**
 * Spring上下文中事件的基类
 *
 * @Author SillyBaka
 **/
public abstract class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(Object source) {
        super(source);
    }
}
