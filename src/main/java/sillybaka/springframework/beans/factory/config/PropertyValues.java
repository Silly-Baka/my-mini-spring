package sillybaka.springframework.beans.factory.config;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2022/10/13
 * Time: 21:15
 *
 * @Author SillyBaka
 * Description：用于处理PropertyValue的工具类
 **/
@Slf4j
public class PropertyValues {
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue pv){
        if(pv == null){
            throw new IllegalArgumentException("初始化bean添加的属性不能为空");
        }
        for (int i = 0; i < propertyValueList.size(); i++) {
            if(propertyValueList.get(i).getPropertyName().equals(pv.getPropertyName())){
                // 覆盖已有的属性值
                propertyValueList.set(i,pv);
                return;
            }
        }
        // 否则就添加
        propertyValueList.add(pv);
    }

    public PropertyValue[] getPropertyValues(){
        return propertyValueList.toArray(new PropertyValue[0]);
    }
}
