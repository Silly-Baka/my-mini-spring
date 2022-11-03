package sillybaka.springframework.annotation;

import org.junit.Test;
import sillybaka.springframework.aop.HelloService;
import sillybaka.springframework.context.support.ClassPathXmlApplicationContext;
import sillybaka.springframework.entity.TestScan2;

/**
 * Description：测试包扫描功能
 * <p>Date: 2022/11/3
 * <p>Time: 22:10
 *
 * @Author SillyBaka
 **/
public class TestComponentScan {
    @Test
    public void testScan(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testComponentScan.xml");
        Object hello = classPathXmlApplicationContext.getBean("hello");

        ((HelloService)hello).hello();
        System.out.println(hello);

        Hello hello2 = (Hello) classPathXmlApplicationContext.getBean("hello");
        System.out.println(hello2);

        TestComponentBean testComponentBean = (TestComponentBean) classPathXmlApplicationContext.getBean("testComponentBean");
        testComponentBean.hello();
        TestComponentBean testComponentBean2 = (TestComponentBean) classPathXmlApplicationContext.getBean("testComponentBean");
        testComponentBean2.hello();


        TestScan2 niubi = (TestScan2) classPathXmlApplicationContext.getBean("niubi");
        niubi.test();
    }

}
