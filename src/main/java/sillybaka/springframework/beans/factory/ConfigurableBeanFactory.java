package sillybaka.springframework.beans.factory;

import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.beans.factory.support.SingletonBeanRegistry;

/**
 * Date: 2022/10/16
 * Time: 19:36
 *
 * @Author SillyBaka
 * Description：提供对BeanFactory进行配置的接口
 **/
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {
}
