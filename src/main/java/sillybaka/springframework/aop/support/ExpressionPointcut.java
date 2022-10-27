package sillybaka.springframework.aop.support;

import sillybaka.springframework.aop.Pointcut;

/**
 * PointCut接口的子接口，是以表达式作为主体的PointCut
 * <p>Date: 2022/10/27
 * <p>Time: 21:11
 *
 * @Author SillyBaka
 **/
public interface ExpressionPointcut extends Pointcut {

    /**
     * 获取当前切入点的表达式
     * @return
     */
    String getExpression();
}
