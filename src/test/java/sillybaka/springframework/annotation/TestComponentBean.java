package sillybaka.springframework.annotation;

import sillybaka.springframework.context.annotation.Scope;
import sillybaka.springframework.context.stereotype.Component;

/**
 * Description：
 * <p>Date: 2022/11/3
 * <p>Time: 22:25
 *
 * @Author SillyBaka
 **/
@Component
@Scope("prototype")
public class TestComponentBean {

    public void hello(){
        System.out.println("可以 测试的没毛病");
        System.out.println("我的哈希码："+this);
    }
}
