package sillybaka.springframework.beans.factory.annotation;

import org.junit.Test;
import sillybaka.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description：测试@Value注解的测试类
 * <p>Date: 2022/11/4
 * <p>Time: 20:05
 *
 * @Author SillyBaka
 **/
public class testValueAnnotation {

    @Test
    public void testSimpleInject(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testValue.xml");
        ValueEntity valueEntity = (ValueEntity) classPathXmlApplicationContext.getBean("valueEntity");

        valueEntity.hello();
    }

    @Test
    public void testXMLAndComponent(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testValue.xml");
        ValueEntity valueEntity = (ValueEntity) classPathXmlApplicationContext.getBean("valueEntity");
        valueEntity.hello();
        System.out.println(valueEntity);

        ValueEntity valueEntity2 = (ValueEntity) classPathXmlApplicationContext.getBean("valueEntity2");
        valueEntity2.hello();
        System.out.println(valueEntity2);
    }
}
