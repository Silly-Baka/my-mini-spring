package sillybaka.springframework.context.event;

import sillybaka.springframework.context.ApplicationContext;
import sillybaka.springframework.context.ApplicationEvent;

/**
 * Spring上下文中事件的基类
 *
 * @Author SillyBaka
 **/
public abstract class ApplicationContextEvent extends ApplicationEvent {

    /**
     * 注册该事件的上下文对象
     */
    protected final ApplicationContext applicationContext;

    public ApplicationContextEvent(Object source, ApplicationContext applicationContext) {
        super(source);
        this.applicationContext = applicationContext;
    }
}
