package sillybaka.springframework.beans.factory;

/**
 * Date: 2022/10/16
 * Time: 20:19
 *
 * @Author SillyBaka
 * Description：整合了BeanFactory所需的所有特性，并额外提供了修改bean以及解决循环依赖的接口
 **/
public interface ConfigurableListableBeanFactory extends ConfigurableBeanFactory, AutowireCapableBeanFactory, ListableBeanFactory {
}
