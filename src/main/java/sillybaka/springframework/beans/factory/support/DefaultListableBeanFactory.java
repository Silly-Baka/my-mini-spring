package sillybaka.springframework.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import sillybaka.springframework.beans.factory.config.BeanDefinition;
import sillybaka.springframework.beans.factory.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description：核心的Bean工厂，真正可以独立地创建一个Bean
 * Date: 2022/10/13
 * Time: 20:02
 *
 * @Author SillyBaka
 **/
@Slf4j
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {
    /**
     * bean定义的注册表
     */
    private final Map<String,BeanDefinition<?>> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * 所有类型对应的beanName（包含多例单例）
     */
    private final Map<Class<?>,String[]> allBeanNamesByType = new ConcurrentHashMap<>();

    /**
     * 所有类型对应的单例的beanName
     */
    private final Map<Class<?>,String[]> singletonBeanNamesByType = new ConcurrentHashMap<>();


    @Override
    protected BeanDefinition<?> getBeanDefinition(String beanName) {
        if(StrUtil.isBlank(beanName)){
            throw new IllegalArgumentException("beanName不能为空");
        }

        BeanDefinition<?> beanDefinition = beanDefinitionMap.get(beanName);

        if(beanDefinition == null){
            log.error("不存在该beanName的bean定义：{}",beanName);
            throw new IllegalArgumentException("不存在该beanName的bean定义");
        }

        return beanDefinition;
    }

    @Override
    public <T> void registerBeanDefinition(String beanName, BeanDefinition<T> beanDefinition) {
        if(StrUtil.isBlank(beanName)){
            throw new IllegalArgumentException("beanName不能为空");
        }
        beanDefinitionMap.put(beanName,beanDefinition);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingleton) {

        Map<Class<?>,String[]> cacheMap = includeNonSingleton ? allBeanNamesByType : singletonBeanNamesByType;

        String[] beanNames = cacheMap.get(type);
        // 如果cache中有 直接返回
        if(beanNames != null){
            return beanNames;
        }
        // cache中没有 则遍历查找
        beanNames = doGetBeanNamesForType(type,includeNonSingleton);

        // 确保不会出现并发错误
        cacheMap.putIfAbsent(type,beanNames);
        beanNames = cacheMap.get(type);

        return beanNames;
    }

    /**
     * 获取指定类型的beanName列表 实际逻辑
     * @param type 类型
     * @param includeNonSingleton 是否包含非单例
     * @return beanName列表
     */
    public String[] doGetBeanNamesForType(Class<?> type, boolean includeNonSingleton){

        List<String> result = new ArrayList<>();
        //先查看自动注册的注册表
        beanDefinitionMap.forEach((beanName,beanDefinition)->{
            //todo 先不考虑是否是多例 --> 为了简单 默认单例

            // 类型相同 直接放入结果
            if(beanDefinition.getType().isAssignableFrom(type)){
                result.add(beanName);
            }

        });
        //todo 再查看手动注册的注册表 先不考虑实现

        return result.toArray(new String[0]);
    }

    @Override
    public void destroySingletons() {
        // 删除注册表中的所有bean实例
        super.destroySingletons();
        // 删除当前beanFactory中的所有cache
        beanDefinitionMap.clear();
        allBeanNamesByType.clear();
        singletonBeanNamesByType.clear();
    }

    @Override
    public void preInitiateSingletons() {
        beanDefinitionMap.forEach((beanName,beanDefinition)->{
            //todo 偷懒 直接调用getBean的逻辑 实际上这里浪费了返回值，并且全是以单例的方式创建
            getBean(beanName);
        });
    }

    /**
     * 销毁单例bean，实际逻辑委托给父类{@link DefaultSingletonBeanRegistry#destroySingleton(String)}
     * @param beanName 要销毁的bean的名字
     */
    @Override
    public void destroySingleton(String beanName) {
        super.destroySingleton(beanName);
    }
}
