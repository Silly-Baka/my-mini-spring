package sillybaka.springframework.beans.factory;

import org.junit.Test;
import sillybaka.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description：测试工厂bean的测试类
 * Date: 2022/10/23
 * Time: 17:32
 *
 * @Author SillyBaka
 **/
public class FactoryBeanTest {

    @Test
    public void testFactoryBeanPrototype(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testFactoryBeanPrototype.xml");

        Object carBeanFactoryPrototype1 = classPathXmlApplicationContext.getBean("carBeanFactoryPrototype");
        System.out.println(carBeanFactoryPrototype1);

        Object carBeanFactoryPrototype2 = classPathXmlApplicationContext.getBean("carBeanFactoryPrototype");
        System.out.println(carBeanFactoryPrototype2);
    }
}
