package sillybaka.springframework.beans.registry.impl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import sillybaka.springframework.beans.registry.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Date: 2022/10/11
 * Time: 20:22
 *
 * @Author SillyBaka
 * Description：Bean注册表的默认实现类
 **/
@Slf4j
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private static final Map<String,Object> BEANS_REGISTRY_MAP = new HashMap<>();

    private static final Map<Class<?>,String> BEAN_NAME_MAP = new HashMap<>();

    @Override
    public void registerBean(String beanName, Object bean) {
        if(BEANS_REGISTRY_MAP.containsKey(beanName)){
            throw new IllegalArgumentException("注册表中已存在同名的bean，注册失败");
        }
        BEAN_NAME_MAP.putIfAbsent(bean.getClass(),beanName);
        BEANS_REGISTRY_MAP.putIfAbsent(beanName,bean);
    }

    @Override
    public Object getSingletonBean(Class<?> beanClass) {
        String beanName = BEAN_NAME_MAP.get(beanClass);
        if(StrUtil.isBlank(beanName)){
            throw new IllegalArgumentException("不存在该类型的bean");
        }
        Object bean = BEANS_REGISTRY_MAP.get(beanName);
        if(bean == null){
            throw new IllegalArgumentException("不存在该类型的bean");
        }

        return bean;
    }

    @Override
    public Object getSingletonBean(String beanName) {
//        Object bean = BEANS_REGISTRY_MAP.get(beanName);
//        if(bean == null){
//            throw new IllegalArgumentException("不存在该类型的bean");
//        }

        return BEANS_REGISTRY_MAP.get(beanName);
    }
}
