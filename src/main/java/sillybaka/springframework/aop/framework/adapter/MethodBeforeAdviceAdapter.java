package sillybaka.springframework.aop.framework.adapter;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import sillybaka.springframework.aop.Advisor;
import sillybaka.springframework.aop.MethodBeforeAdvice;

/**
 * 用于向Spring适配 {@link MethodBeforeAdvice} 和 {@link MethodBeforeAdviceInterceptor} 的适配器
 * <p>Date: 2022/10/28
 * <p>Time: 16:13
 *
 * @Author SillyBaka
 **/
public class MethodBeforeAdviceAdapter implements AdvisorAdapter{

    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        MethodBeforeAdvice advice = (MethodBeforeAdvice) advisor.getAdvice();
        return new MethodBeforeAdviceInterceptor(advice);
    }

    @Override
    public boolean supportAdvice(Advice advice) {
        return (advice instanceof MethodBeforeAdvice);
    }
}
