package sillybaka.springframework.entity;

import lombok.Data;
import sillybaka.springframework.beans.factory.DisposableBean;

/**
 * Description：
 * Date: 2022/10/22
 * Time: 17:30
 *
 * @Author SillyBaka
 **/
@Data
public class TestDestroy implements DisposableBean {

    private String name;
    private Integer age;

    @Override
    public void destroy() {
        System.out.println("正在执行DisposableBean接口的destroy方法");
        System.out.println(name);
    }

    public void myDestroy(){
        System.out.println("正在执行指定的destroy方法");
        System.out.println(name);
    }
}
