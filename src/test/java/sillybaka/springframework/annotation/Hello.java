package sillybaka.springframework.annotation;

import sillybaka.springframework.context.annotation.Scope;
import sillybaka.springframework.context.stereotype.Component;

/**
 * Description：
 * <p>Date: 2022/11/3
 * <p>Time: 22:13
 *
 * @Author SillyBaka
 **/
@Component
@Scope("prototype")
public class Hello implements sillybaka.springframework.aop.HelloService {

    @Override
    public void hello() {
        System.out.println("我是component组件？");
    }
}
