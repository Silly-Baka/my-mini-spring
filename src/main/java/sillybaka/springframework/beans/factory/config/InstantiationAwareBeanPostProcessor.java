package sillybaka.springframework.beans.factory.config;

/**
 * {@link BeanPostProcessor} 的子接口<p>
 * 实例化前后调用的后置处理器，只用于特殊Bean的实例化
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


}
