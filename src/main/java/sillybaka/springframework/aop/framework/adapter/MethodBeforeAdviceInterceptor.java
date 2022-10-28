package sillybaka.springframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import sillybaka.springframework.aop.BeforeAdvice;
import sillybaka.springframework.aop.MethodBeforeAdvice;

/**
 * 对象适配器，包装了一个MethodBeforeAdvice
 * 用于拦截方法，并且执行内部的BeforeAdvice逻辑
 * <p>Date: 2022/10/28
 * <p>Time: 16:00
 *
 * @Author SillyBaka
 **/
public class MethodBeforeAdviceInterceptor implements MethodInterceptor, BeforeAdvice {

    private final MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice){
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 执行连接点的逻辑之前 执行当前通知的逻辑
        this.advice.before(invocation.getMethod(),invocation.getArguments());
        // 交给下一个拦截器
        return invocation.proceed();
    }
}
