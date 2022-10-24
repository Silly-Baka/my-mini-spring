package sillybaka.springframework.context.event;

import sillybaka.springframework.context.ApplicationContext;

/**
 * 在初始化或者刷新ApplicationContext时触发的事件
 *
 * @Author SillyBaka
 **/
public class ContextRefreshedEvent extends ApplicationContextEvent{

    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }
}
