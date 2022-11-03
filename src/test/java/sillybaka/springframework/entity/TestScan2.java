package sillybaka.springframework.entity;

import sillybaka.springframework.context.stereotype.Component;

/**
 * Description：
 * <p>Date: 2022/11/3
 * <p>Time: 22:46
 *
 * @Author SillyBaka
 **/
@Component("niubi")
public class TestScan2 {

    public void test(){
        System.out.println("我是测试ComponentScan第二个包");
    }
}
