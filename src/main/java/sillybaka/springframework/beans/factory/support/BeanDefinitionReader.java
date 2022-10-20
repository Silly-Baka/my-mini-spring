package sillybaka.springframework.beans.factory.support;

import sillybaka.springframework.core.io.Resource;
import sillybaka.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * Date: 2022/10/16
 * Time: 20:54
 *
 * @Author SillyBaka
 * Description：提供读取Bean定义的接口
 **/
public interface BeanDefinitionReader {

    ResourceLoader getResourceLoader();

    BeanDefinitionRegistry getBeanDefinitionRegistry();

    void loadBeanDefinitions(String location);

    void loadBeanDefinitions(String[] locations);

    void loadBeanDefinitions(Resource resource);

    void loadBeanDefinitions(Resource[] resources);
}
