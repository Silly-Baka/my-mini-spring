package sillybaka.springframework.beans.factory.support;

import sillybaka.springframework.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 继承自{@link DefaultSingletonBeanRegistry}，提供了为当前上下文管理的FactoryBean的内部bean进行缓存的功能，
 * 同时也为创建、获取FactoryBean中的bean提供接口
 *
 * @Author SillyBaka
 **/
public class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{

    private final Map<String,Object> factoryBeanObjectsCache = new ConcurrentHashMap<>(16);

    /**
     * 从缓存中为指定的FactoryBean获取内部的bean
     */
    protected Object getCachedObjectForFactoryBean(String beanName){
        return factoryBeanObjectsCache.get(beanName);
    }

    /**
     * 添加缓存
     */
    protected void addCachedObjectForFactoryBean(String beanName,Object bean){
        factoryBeanObjectsCache.putIfAbsent(beanName,bean);
    }

    /**
     * 从指定FactoryBean中获取内置bean
     */
    protected Object getObjectFromFactoryBean(String beanName, FactoryBean<?> factoryBean){

        if(factoryBean.isSingleton()){
            Object bean;
            synchronized (this.factoryBeanObjectsCache){
                bean = factoryBeanObjectsCache.get(beanName);
                if(bean != null){
                    return bean;
                }
                bean = factoryBean.getObject();
                factoryBeanObjectsCache.putIfAbsent(beanName,bean);

                return bean;
            }
        }else {
            return factoryBean.getObject();
        }
    }

}
