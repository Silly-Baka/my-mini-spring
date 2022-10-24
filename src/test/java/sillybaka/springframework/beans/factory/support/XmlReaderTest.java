package sillybaka.springframework.beans.factory.support;

import org.junit.Test;
import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.core.io.DefaultResourceLoader;
import sillybaka.springframework.core.io.ResourceLoader;

/**
 * Date: 2022/10/17
 * Time: 23:34
 *
 * @Author SillyBaka
 * Description：测试XmlReader的测试类
 **/
public class XmlReaderTest {
    @Test
    public void testXmlReader(){
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        BeanDefinitionRegistry beanDefinitionRegistry = new DefaultListableBeanFactory();
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(resourceLoader,beanDefinitionRegistry);

        // 1、先从xml配置文件中读取Bean定义 并注册进注册表
        beanDefinitionReader.loadBeanDefinitions("classpath:test1.xml");

        // 2、BeanFactory根据beanName获取bean实例（懒汉式创建）
        BeanFactory beanFactory = new DefaultListableBeanFactory();
        Object carBean1 = beanFactory.getBean("carBean1");

        System.out.println(carBean1);

        beanDefinitionReader.loadBeanDefinitions("classpath:test2.xml");

        Object carBean2 = beanFactory.getBean("carBean2");
        System.out.println(carBean2);

    }
}
