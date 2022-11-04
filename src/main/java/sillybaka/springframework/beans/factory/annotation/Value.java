package sillybaka.springframework.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * 用于对属性进行依赖注入的注解
 * 支持两种方式的注入：1、{value} 直接注入属性 2、${propertyName} 根据配置文件注入
 * <p>Date: 2022/11/4
 * <p>Time: 15:47
 *
 * @Author SillyBaka
 **/
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    /**
     * 用于标注属性，两种格式 ： 1、{value} 直接注入属性 2、${propertyName} 根据配置文件注入
     */
    String value() default "";
}
