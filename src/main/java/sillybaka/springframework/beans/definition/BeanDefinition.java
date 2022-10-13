package sillybaka.springframework.beans.definition;

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
     * Bean所带的参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * Bean所带的属性值
     */
    private Object[] values;

    public BeanDefinition(String name,Class<T> type){
        this.name = name;
        this.type = type;
    }
}
