package sillybaka.springframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import sillybaka.springframework.aop.MethodAfterAdvice;

/**
 * Description：
 * <p>Date: 2022/10/29
 * <p>Time: 20:45
 *
 * @Author SillyBaka
 **/
public class MethodAfterAdviceInterceptor implements MethodInterceptor {

    private final MethodAfterAdvice advice;

    public MethodAfterAdviceInterceptor(MethodAfterAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object retVal;

        retVal = invocation.proceed();
        // 在调用完后再执行通知
        advice.after(invocation.getMethod(),invocation.getArguments(),invocation.getThis());

        return retVal;
    }
}
