package sillybaka.springframework.context.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 作用域注解，用来标明bean实例的作用域
 * <p>Date: 2022/11/3
 * <p>Time: 21:56
 *
 * @Author SillyBaka
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    @AliasFor("scopeName")
    String value() default "";

    @AliasFor("value")
    String scopeName() default "";
}
