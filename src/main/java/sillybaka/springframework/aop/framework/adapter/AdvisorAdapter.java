package sillybaka.springframework.aop.framework.adapter;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import sillybaka.springframework.aop.Advisor;

/**
 * 拓展用的适配器，实现了该接口的类可以使用自定义类型的Advice和Advisor，同时动态创建自定义的拦截器，并在Spring AOP中使用
 * <p>用法：一个适配器代表了一种通知及其拦截器，可以通过该适配器来判断是否支持目标Advisor的通知以及获取其所适配的拦截器
 * <p>Date: 2022/10/28
 * <p>Time: 16:08
 *
 * @Author SillyBaka
 **/
public interface AdvisorAdapter {

    /**
     * 获取指定Advisor所适配的拦截器（获取的拦截器为Advice和Interceptor的适配器）
     */
    MethodInterceptor getInterceptor(Advisor advisor);

    /**
     * 检查当前适配器是否支持目标类型的通知，能否为其创建一个相应的拦截器
     */
    boolean supportAdvice(Advice advice);
}
