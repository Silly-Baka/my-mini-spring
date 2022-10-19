package sillybaka.springframework.beans.factory.config;

/**
 * Date: 2022/10/19
 * Time: 22:56
 *
 * @Author SillyBaka
 * Description：Bean对象的后置处理器
 **/
public interface BeanPostProcessor {

    /**
     * 在初始化bean之前被调用
     * @param bean 将被初始化的bean对象
     * @param beanName bean名字
     * @return 装饰后的bean对象
     */
    <T> T postProcessBeforeInitialization(T bean, String beanName);

    /**
     * 在初始化bean之后被调用
     * @param bean 被初始化的bean对象
     * @param beanName bean名字
     * @return 装饰后的bean对象
     */
    <T> T postProcessAfterInitialization(T bean, String beanName);
}
