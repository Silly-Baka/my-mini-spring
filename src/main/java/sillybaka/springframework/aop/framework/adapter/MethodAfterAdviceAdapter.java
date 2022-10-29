package sillybaka.springframework.aop.framework.adapter;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import sillybaka.springframework.aop.Advisor;
import sillybaka.springframework.aop.MethodAfterAdvice;

/**
 * 用于适配MethodAfterAdvice的适配器
 * <p>Date: 2022/10/29
 * <p>Time: 20:44
 *
 * @Author SillyBaka
 **/
public class MethodAfterAdviceAdapter implements AdvisorAdapter{
    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        return new MethodAfterAdviceInterceptor((MethodAfterAdvice) advisor.getAdvice());
    }

    @Override
    public boolean supportAdvice(Advice advice) {
        return (advice instanceof MethodAfterAdvice);
    }
}
