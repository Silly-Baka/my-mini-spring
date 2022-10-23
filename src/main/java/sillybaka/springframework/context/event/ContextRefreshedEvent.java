package sillybaka.springframework.context.event;

/**
 * 在初始化或者刷新ApplicationContext时触发的事件
 *
 * @Author SillyBaka
 **/
public class ContextRefreshedEvent extends ApplicationContextEvent{

    public ContextRefreshedEvent(Object source) {
        super(source);
    }
}
