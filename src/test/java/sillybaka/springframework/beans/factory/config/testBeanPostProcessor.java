package sillybaka.springframework.beans.factory.config;

import org.junit.Test;
import sillybaka.springframework.beans.factory.support.BeanDefinitionReader;
import sillybaka.springframework.beans.factory.support.BeanDefinitionRegistry;
import sillybaka.springframework.beans.factory.support.DefaultListableBeanFactory;
import sillybaka.springframework.beans.factory.support.XmlBeanDefinitionReader;
import sillybaka.springframework.core.io.DefaultResourceLoader;
import sillybaka.springframework.core.io.ResourceLoader;
import sillybaka.springframework.testImpl.TestBeanPostProcessor;

/**
 * Date: 2022/10/19
 * Time: 23:32
 *
 * @Author SillyBaka
 * Description：测试bean后置处理器的测试类
 **/
public class testBeanPostProcessor {
    @Test
    public void test(){
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        BeanDefinitionRegistry beanDefinitionRegistry = new DefaultListableBeanFactory();

        BeanDefinitionReader reader = new XmlBeanDefinitionReader(resourceLoader,beanDefinitionRegistry);

        reader.loadBeanDefinitions("classpath:test2.xml");

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 给bean工厂添加后置处理器
        BeanPostProcessor beanPostProcessor = new TestBeanPostProcessor();
        beanFactory.addBeanPostProcessor(beanPostProcessor);

        Object carBean2 = beanFactory.getBean("carBean2");
    }
}
