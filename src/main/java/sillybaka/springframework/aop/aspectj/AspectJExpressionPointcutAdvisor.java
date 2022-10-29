package sillybaka.springframework.aop.aspectj;

import org.aopalliance.aop.Advice;
import sillybaka.springframework.aop.Pointcut;
import sillybaka.springframework.aop.PointcutAdvisor;

/**
 * Description：
 * <p>Date: 2022/10/29
 * <p>Time: 13:59
 *
 * @Author SillyBaka
 **/
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    private final Advice advice;
    private final Pointcut pointcut;

    public AspectJExpressionPointcutAdvisor(Advice advice) {
        this.advice = advice;
        this.pointcut = Pointcut.DEFAULT_POINT_CUT;
    }
    public AspectJExpressionPointcutAdvisor(Advice advice,AspectJExpressionPointcut pointcut){
        this.advice = advice;
        this.pointcut = pointcut;
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
