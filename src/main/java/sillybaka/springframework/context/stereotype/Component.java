package sillybaka.springframework.context.stereotype;

import java.lang.annotation.*;

/**
 * 用来标注Spring组件的注解
 * <p>Date: 2022/11/3
 * <p>Time: 21:17
 *
 * @Author SillyBaka
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    /**
     * 组件的名字
     */
    String value() default "";
}
