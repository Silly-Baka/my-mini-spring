package sillybaka.springframework.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

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

    private final Map<String,Object> beansRegistryMap = new ConcurrentHashMap<>();

    private final Map<Class<?>,String> beanNamesMap = new ConcurrentHashMap<>();

    @Override
    public void registerBean(String beanName, Object bean) {
        if(beansRegistryMap.containsKey(beanName)){
            throw new IllegalArgumentException("注册表中已存在同名的bean，注册失败");
        }
        beanNamesMap.putIfAbsent(bean.getClass(),beanName);
        beansRegistryMap.putIfAbsent(beanName,bean);
    }

    @Override
    public Object getSingletonBean(Class<?> beanClass) {
        String beanName = beanNamesMap.get(beanClass);
        if(StrUtil.isBlank(beanName)){
            throw new IllegalArgumentException("不存在该类型的bean");
        }
        Object bean = beansRegistryMap.get(beanName);
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

        return beansRegistryMap.get(beanName);
    }

    @Override
    public void destroySingletonBeans() {
        beansRegistryMap.clear();
        beanNamesMap.clear();
    }
}
