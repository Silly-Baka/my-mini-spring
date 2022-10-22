package sillybaka.springframework.beans.factory;

import org.junit.Test;
import sillybaka.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description：测试bean生命周期的测试类
 * Date: 2022/10/22
 * Time: 16:55
 *
 * @Author SillyBaka
 **/
public class BeanInitAndDestroyTest {

    @Test
    public void testInitMethod(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testInit.xml");
    }

    @Test
    public void testDestroyMethod(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testDestroy.xml");
        classPathXmlApplicationContext.close();

    }
}
