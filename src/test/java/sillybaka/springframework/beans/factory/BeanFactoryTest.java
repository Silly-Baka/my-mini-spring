package sillybaka.springframework.beans.factory;

import org.junit.Test;
import sillybaka.springframework.beans.factory.config.BeanDefinition;
import sillybaka.springframework.beans.factory.config.BeanDefinitionRegistry;
import sillybaka.springframework.beans.factory.config.PropertyValue;
import sillybaka.springframework.beans.factory.config.PropertyValues;
import sillybaka.springframework.beans.utils.PropertyUtils;
import sillybaka.springframework.entity.Car;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * Date: 2022/10/15
 * Time: 16:13
 *
 * @Author SillyBaka
 * Description：
 **/
public class BeanFactoryTest {
    /**
     * 测试创建Bean以及注入属性
     */
    @Test
    public void testCreateBeanAndPropertyInjection(){
    //todo 1、模拟从xml中读取bean的属性 并封装在PropertyValue中
        PropertyValue brand = new PropertyValue("brand", "宝马");
        PropertyValue price = new PropertyValue("price", 2000000);
        PropertyValue owner = new PropertyValue("owner", "sillybaka");

        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(brand);
        propertyValues.addPropertyValue(price);
        propertyValues.addPropertyValue(owner);
    //todo 2、读取对Bean的定义
        BeanDefinition<Car> carBeanDefinition = new BeanDefinition<>("superCar", Car.class, propertyValues);
    //todo 3、将Bean定义注册进注册表
        BeanDefinitionRegistry definitionRegistry = new DefaultListableBeanFactory();
        definitionRegistry.registerBeanDefinition("superCar",carBeanDefinition);
    //todo 4、使用xml获取bean的全类名，并将类的所有属性的propertyDescriptor注册到缓存中
//        PropertyUtils.addPropertyDescriptor(Car.class);
        Map<String, PropertyDescriptor> beanPropertyMap = PropertyUtils.getBeanPropertyMap(Car.class);
        //todo 5、使用BeanFactory创建并获取bean实例
        BeanFactory beanFactory = new DefaultListableBeanFactory();
        Object car = beanFactory.getBean("superCar");

        System.out.println(car);
        System.out.println(car.getClass());
    }
}
