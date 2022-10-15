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
@AllArgsConstructor
@NoArgsConstructor
public class BeanDefinition<T> {

    /**
     * Bean的名字
     */
    private String name;
    /**
     * Bean的类型
     */
    private Class<T> type;
    /**
     * Bean的属性
     */
    private PropertyValues propertyValues;


    public BeanDefinition(String name,Class<T> type){
        this.name = name;
        this.type = type;
    }
}
