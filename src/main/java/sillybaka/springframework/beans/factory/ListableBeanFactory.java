package sillybaka.springframework.beans.factory;

import sillybaka.springframework.beans.factory.BeanFactory;

/**
 * Date: 2022/10/16
 * Time: 20:17
 *
 * @Author SillyBaka
 * Description：为BeanFactory提供了查询bean的能力，可以一次性获取所有需要的Bean
 **/
public interface ListableBeanFactory extends BeanFactory {

    /**
     * 根据bean的类型 获取bean名字列表
     * @param type bean类型
     * @param includeNonSingleton 是否包含非单例的bean
     * @return 符合条件的beanName列表
     */
    String[] getBeanNamesForType(Class<?> type, boolean includeNonSingleton);
}
