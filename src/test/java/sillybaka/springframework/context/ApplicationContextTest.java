package sillybaka.springframework.context;

import org.junit.Test;
import sillybaka.springframework.context.support.ClassPathXmlApplicationContext;
import sillybaka.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Description：用于测试ApplicationContext的测试类
 * Date: 2022/10/20
 * Time: 22:06
 *
 * @Author SillyBaka
 **/
public class ApplicationContextTest {
    /**
     * 测试ClassPathXmlApplicationContext
     */
    @Test
    public void testClassPathXmlApplicationContext(){
        ClassPathXmlApplicationContext xmlApplicationContext1 = new ClassPathXmlApplicationContext("classpath:test1.xml");
        Object carBean1 = xmlApplicationContext1.getBean("carBean1");
        System.out.println(carBean1);

        Object carRoll1 = xmlApplicationContext1.getBean("carRoll1");
        System.out.println(carRoll1);


        ClassPathXmlApplicationContext xmlApplicationContext2 = new ClassPathXmlApplicationContext("classpath:test2.xml");
        Object carBean2 = xmlApplicationContext2.getBean("carBean2");
        System.out.println(carBean2);

        Object carRoll = xmlApplicationContext2.getBean("carRoll");
        System.out.println(carRoll);

    }
    /**
     * 测试FileSystemXmlApplicationContext
     */
    @Test
    public void testFileSystemXmlApplicationContext(){
        FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext("C:\\Users\\86176\\Desktop\\JAVA\\my-mini-spring\\src\\test\\resources\\test1.xml");
        Object carBean1 = fileSystemXmlApplicationContext.getBean("carBean1");
        System.out.println(carBean1);

        FileSystemXmlApplicationContext fileSystemXmlApplicationContext1 = new FileSystemXmlApplicationContext("C:\\Users\\86176\\Desktop\\JAVA\\my-mini-spring\\src\\test\\resources\\test2.xml");
        Object carBean2 = fileSystemXmlApplicationContext1.getBean("carBean2");
        System.out.println(carBean2);
    }
}
