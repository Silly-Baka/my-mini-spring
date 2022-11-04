package sillybaka.springframework.beans.factory.config;

/**
 * {@link BeanPostProcessor} 的子接口<p>
 * 实例化前后调用的后置处理器，用于特殊bean的实例化 以及 实例化后注入属性前更改要注入的属性
 * <p>Date: 2022/11/1
 * <p>Time: 16:00
 *
 * @Author SillyBaka
 **/
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{

    /**
     * 在实例化Bean前调用的方法，用于生成代理对象代替原Bean实例
     * @param beanClass bean的实际类型
     * @param beanName bean名字
     * @return 成功创建代理对象则为代理对象
     *         否则返回null
     */
    default Object postProcessBeforeInstantiation(Class<?> beanClass,String beanName){
        return null;
    }

    /**
     * 用于在bean实例化之后，注入属性之前更改要注入的属性列表
     * @param pv 属性列表
     * @param bean bean实例
     * @param beanName bean名字
     * @return 更改后的属性列表
     */
    default PropertyValues postProcessPropertyValues(PropertyValues pv,Object bean,String beanName){
        return null;
    }
}
