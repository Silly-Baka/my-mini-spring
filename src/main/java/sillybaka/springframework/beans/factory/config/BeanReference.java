package sillybaka.springframework.beans.factory.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2022/10/15
 * Time: 16:10
 *
 * @Author SillyBaka
 * Description：对Bean的引用属性
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanReference {
    /**
     * bean的名字
     */
    private String beanName;
    /**
     * bean的类型
     */
//    private String beanType;
}
