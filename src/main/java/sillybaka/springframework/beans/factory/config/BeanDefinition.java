package sillybaka.springframework.beans.factory.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Date: 2022/10/11
 * Time: 20:13
 *
 * @Author SillyBaka
 * Description：Bean的定义
 **/
@Data
public class BeanDefinition<T> {

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
}
