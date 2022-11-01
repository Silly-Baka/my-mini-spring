package sillybaka.springframework.aop.autoproxy;

import org.junit.Test;
import sillybaka.springframework.aop.HelloService;
import sillybaka.springframework.context.ApplicationContext;
import sillybaka.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description：自动代理的测试类
 * <p>Date: 2022/11/1
 * <p>Time: 21:00
 *
 * @Author SillyBaka
 **/
public class TestAutoProxy {

    @Test
    public void test1(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:testAutoProxy.xml");

        Object object = applicationContext.getBean("helloService", HelloService.class);

        HelloService helloService = (HelloService) object;
        helloService.hello();
    }
}
