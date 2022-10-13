package sillybaka.springframework.beans.factory.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2022/10/13
 * Time: 21:14
 *
 * @Author SillyBaka
 * Description：用于保存bean属性的键值对，比map更灵活
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyValue {
    private String propertyName;
    private Object propertyValue;
}
