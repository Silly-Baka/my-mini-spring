package sillybaka.springframework.context;

import java.util.EventListener;

/**
 * Spring事件机制中的监听者，可以与广播者相绑定，监听目标广播者上的指定类型事件
 * 实现该接口的类可以对指定类型的事件进行监听并处理
 *
 * @Author SillyBaka
 **/
@FunctionalInterface
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 处理指定类型事件的函数式接口
     */
    void onApplicationEvent(E event);

}
