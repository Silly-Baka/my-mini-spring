package sillybaka.springframework.testImpl;

import sillybaka.springframework.beans.factory.config.BeanPostProcessor;
import sillybaka.springframework.entity.Car;

/**
 * Date: 2022/10/19
 * Time: 23:29
 *
 * @Author SillyBaka
 * Description：
 **/
public class TestBeanPostProcessor implements BeanPostProcessor {
    @Override
    public <T> T postProcessBeforeInitialization(T bean, String beanName) {
        System.out.println("初始化之前被调用");
        System.out.println(bean);
        if(bean instanceof Car){
            ((Car) bean).setBrand("我变成究极法拉利了！");
        }
        return bean;
    }

    @Override
    public <T> T postProcessAfterInitialization(T bean, String beanName) {
        System.out.println("初始化之后被调用");
        System.out.println(bean);
        if(bean instanceof Car){
            Car car = (Car) bean;
            System.out.println("我是一台" + car.getBrand());
        }
        return bean;
    }
}
