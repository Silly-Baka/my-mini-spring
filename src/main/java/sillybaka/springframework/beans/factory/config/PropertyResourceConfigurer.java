package sillybaka.springframework.beans.factory.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedIOException;
import sillybaka.springframework.beans.factory.ConfigurableBeanFactory;
import sillybaka.springframework.beans.factory.ConfigurableListableBeanFactory;

import java.util.Properties;

/**
 * 该抽象基类提供了能够根据Properties文件来改变bean定义中相应属性的功能<p>
 * 使用Properties文件对BeanFactory中bean定义进行属性替换的具体逻辑由子类实现，抽象方法{@link PropertyResourceConfigurer#processProperties(ConfigurableListableBeanFactory, Properties)}
 * <p>Date: 2022/11/2
 * <p>Time: 18:08
 *
 * @Author SillyBaka
 **/
@Slf4j
public abstract class PropertyResourceConfigurer extends PropertiesLoaderSupport implements BeanFactoryPostProcessor{

    /**
     * 模板方法 具体逻辑子类实现
     * @param beanFactory 上下文中所使用的工厂对象
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory){
        // 1、读取上下文的property
        Properties props = null;
        try {
            props = loadProperties();
        } catch (NestedIOException e) {
            log.error("加载上下文配置文件时出错",e);
        }
        // 2、将property应用于BeanFactory
        processProperties(beanFactory,props);
    }


    /**
     * 将给定的property中的属性应用于BeanFactory中的bean定义
     * @param beanFactory 指定的BeanFactory
     * @param props property属性集合
     */
    protected abstract void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props);
}
