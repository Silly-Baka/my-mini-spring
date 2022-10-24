package sillybaka.springframework.context;

import java.util.EventObject;

/**
 * Description：Spring中事件实体类的顶层抽象类，Spring中任何一种事件都需要继承该抽象类
 *
 * @Author SillyBaka
 **/
public abstract class ApplicationEvent extends EventObject {

    public ApplicationEvent(Object source) {
        super(source);
    }
}
