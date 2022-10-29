package sillybaka.springframework.aop.support;

import org.aopalliance.aop.Advice;
import sillybaka.springframework.aop.Pointcut;
import sillybaka.springframework.aop.PointcutAdvisor;
import sillybaka.springframework.aop.aspectj.AspectJExpressionPointcut;

/**
 * {@link PointcutAdvisor} 接口的默认实现类，适用于大部分类型的Advice
 * <p>Date: 2022/10/28
 * <p>Time: 21:32
 *
 * @Author SillyBaka
 **/
public class DefaultPointcutAdvisor implements PointcutAdvisor{

    /**
     * 默认值为匹配所有类所有方法的切入点
     */
    private Pointcut pointcut = Pointcut.DEFAULT_POINT_CUT;

    private final Advice advice;

    public DefaultPointcutAdvisor(Advice advice){
        this.advice = advice;
    }

    public DefaultPointcutAdvisor(Pointcut pointcut,Advice advice){
        this.pointcut = pointcut;
        this.advice = advice;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }
}
