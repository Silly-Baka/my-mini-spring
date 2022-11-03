package sillybaka.springframework.beans.factory.config;

import org.junit.Test;
import sillybaka.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description：测试替换占位符的BeanPostProcessor
 * <p>Date: 2022/11/3
 * <p>Time: 19:08
 *
 * @Author SillyBaka
 **/
public class testPlaceholder {

    @Test
    public void test1(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testPlaceholder.xml");
        Object testCar = classPathXmlApplicationContext.getBean("testCar");

        System.out.println(testCar);
    }

    @Test
    public void test2(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testPlaceholder2.xml");
        Object carRoll = classPathXmlApplicationContext.getBean("carRoll");
        System.out.println(carRoll);
    }
}
