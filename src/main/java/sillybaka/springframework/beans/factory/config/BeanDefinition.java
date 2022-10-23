package sillybaka.springframework.beans.factory.config;

import lombok.Data;
import sillybaka.springframework.beans.factory.ConfigurableBeanFactory;


/**
 * Description：Bean定义
 * Date: 2022/10/11
 * Time: 20:13
 *
 * @Author SillyBaka
 **/
@Data
public class BeanDefinition<T> {
    /**
     * 单例作用域的标识符
     */
    public static final String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;
    /**
     * 多例作用域的标识符
     */
    public static final String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    /**
     * Bean的类型
     */
    private Class<T> type;
    /**
     * Bean的属性
     */
    private PropertyValues propertyValues;
    /**
     * 指定的初始化方法名字
     */
    private String initMethodName;
    /**
     * 指定的destroy方法名字
     */
    private String destroyMethodName;
    /**
     * 该Bean是否定义为单例
     */
    private boolean isSingleton;
    /**
     * 该Bean是否定义为多例
     */
    private boolean isPrototype;

    public BeanDefinition(){};

    public BeanDefinition(Class<T> type, PropertyValues propertyValues) {
        this.type = type;
        this.propertyValues = propertyValues;
    }

    public BeanDefinition(Class<T> type, PropertyValues propertyValues, String initMethodName, String destroyMethodName) {
        this.type = type;
        this.propertyValues = propertyValues;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public boolean isPrototype() {
        return isPrototype;
    }
}
