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

    private Advice advice;
    private Pointcut pointcut;

    public AspectJExpressionPointcutAdvisor(){}
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

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }
}
