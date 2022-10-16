package sillybaka.springframework.beans.factory.support;

import sillybaka.springframework.core.io.ResourceLoader;

/**
 * Date: 2022/10/16
 * Time: 20:57
 *
 * @Author SillyBaka
 * Description：实现BeanDefinitionReader接口中的通用方法
 **/
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    private final ResourceLoader resourceLoader;

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public AbstractBeanDefinitionReader(ResourceLoader resourceLoader, BeanDefinitionRegistry beanDefinitionRegistry) {
        this.resourceLoader = resourceLoader;
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return beanDefinitionRegistry;
    }

    @Override
    public void loadBeanDefinitions(String location) {
        loadBeanDefinitions(resourceLoader.getResource(location));
    }

    @Override
    public void loadBeanDefinitions(String[] locations) {
        for (String location : locations) {
            loadBeanDefinitions(resourceLoader.getResource(location));
        }
    }
}
