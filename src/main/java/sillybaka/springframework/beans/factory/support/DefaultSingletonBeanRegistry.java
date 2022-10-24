package sillybaka.springframework.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import sillybaka.springframework.beans.factory.DisposableBean;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description：Bean注册表的默认实现类
 * Date: 2022/10/11
 * Time: 20:22
 *
 * @Author SillyBaka
 **/
@Slf4j
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String,Object> singletonObjects = new ConcurrentHashMap<>();

    private final Map<Class<?>,String> beanNamesMap = new ConcurrentHashMap<>();

    private final Map<String,Object> disposableBeans = new ConcurrentHashMap<>();

    @Override
    public void registerSingleton(String beanName, Object bean) {
        synchronized (this.singletonObjects){
            if(singletonObjects.containsKey(beanName)){
                throw new IllegalArgumentException("注册表中已存在同名的bean，注册失败");
            }
            addSingleton(beanName,bean);
        }
    }

    protected void addSingleton(String beanName, Object bean){
        beanNamesMap.putIfAbsent(bean.getClass(),beanName);
        singletonObjects.putIfAbsent(beanName,bean);
    }

    @Override
    public Object getSingleton(Class<?> beanClass) {
        String beanName = beanNamesMap.get(beanClass);
        if(StrUtil.isBlank(beanName)){
            throw new IllegalArgumentException("不存在该类型的bean");
        }
        Object bean = singletonObjects.get(beanName);
        if(bean == null){
            throw new IllegalArgumentException("不存在该类型的bean");
        }

        return bean;
    }

    @Override
    public Object getSingleton(String beanName) {
        synchronized (singletonObjects){
            return singletonObjects.get(beanName);
        }
    }


    public void destroySingletons() {
        // 执行所有bean的自定义destroy方法
        Set<String> disposableBeanNames = disposableBeans.keySet();
        for (String disposableBeanName : disposableBeanNames) {
            ((DisposableBean) disposableBeans.get(disposableBeanName)).destroy();
            disposableBeans.remove(disposableBeanName);
        }
        // 清除缓存
        singletonObjects.clear();
        beanNamesMap.clear();
    }

    public void registerDisposableBean(String beanName,Object bean){
        synchronized (this.disposableBeans){
            this.disposableBeans.putIfAbsent(beanName,bean);
        }
    }

    public void destroySingleton(String beanName){
        singletonObjects.remove(beanName);

        DisposableBean disposableBean;
        synchronized (this.disposableBeans){
            disposableBean = (DisposableBean)disposableBeans.remove(beanName);
        }
        disposableBean.destroy();
    }
}
