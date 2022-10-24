package sillybaka.springframework.beans.factory;

import org.junit.Test;
import sillybaka.springframework.beans.factory.support.DefaultListableBeanFactory;
import sillybaka.springframework.beans.factory.support.XmlBeanDefinitionReader;
import sillybaka.springframework.context.support.ClassPathXmlApplicationContext;
import sillybaka.springframework.core.io.DefaultResourceLoader;
import sillybaka.springframework.entity.TestAware;

/**
 * Description：测试Aware接口的测试类
 * Date: 2022/10/22
 * Time: 20:57
 *
 * @Author SillyBaka
 **/
public class AwareTest {

    @Test
    public void testBeanFactoryAware(){
        // 使用BeanFactory测试
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(new DefaultResourceLoader(), beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:testAware.xml");

        Object testAware = beanFactory.getBean("testAware");

        System.out.println(testAware);
        System.out.println(beanFactory);
        System.out.println(((TestAware)testAware).getBeanFactory());

    }

    @Test
    public void testApplicationAware(){
        // 使用ApplicationContext测试
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:testAware.xml");

        TestAware testAware = (TestAware) applicationContext.getBean("testAware");

        System.out.println(testAware);
        System.out.println(testAware.getApplicationContext());
        System.out.println(applicationContext);

    }
}
