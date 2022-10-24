package sillybaka.springframework.beans.factory;

/**
 * Date: 2022/10/11
 * Time: 20:29
 *
 * @Author SillyBaka
 * Description：bean工厂 封装了bean的获取与创建
 **/
public interface BeanFactory {
    /**
     * 获取bean
     * @param beanName bean的名字
     * @return bean的实例对象
     */
    Object getBean(String beanName);

    /**
     * 根据指定类型获取bean（避免强转的方法）
     * @param beanName bean的名字
     * @param requiredType 需要的类型
     */
    <T> T getBean(String beanName,Class<T> requiredType);
}
