package sillybaka.springframework.context.support;

import sillybaka.springframework.beans.factory.support.DefaultListableBeanFactoryBean;
import sillybaka.springframework.beans.factory.support.XmlBeanDefinitionReader;
import sillybaka.springframework.core.io.DefaultResourceLoader;
import sillybaka.springframework.core.io.Resource;

/**
 * 基于{@link AbstractRefreshableApplicationContext}的子类，实现了{@link #refreshBeanFactory()}委托的逻辑
 *
 * Date: 2022/10/20
 * Time: 20:01
 *
 * @see AbstractRefreshableApplicationContext
 * @Author SillyBaka
 **/
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {
    /**
     * 使用xml配置文件的方式读取bean定义
     * @param beanFactory 要加载进的beanFactory
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactoryBean beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(new DefaultResourceLoader(), beanFactory);

        beanDefinitionReader.loadBeanDefinitions(getConfigResources());

    }

    /**
     * 由子类实现
     * @see ClassPathXmlApplicationContext
     * @see FileSystemXmlApplicationContext
     */
    protected abstract Resource[] getConfigResources();


    protected Resource[] getResourcesByLocations(String[] locations){
        Resource[] resources = new Resource[locations.length];
        for (int i = 0; i < locations.length; i++) {
            resources[i] = getResource(locations[i]);
        }
        return resources;
    }
}
