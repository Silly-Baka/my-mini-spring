package sillybaka.springframework.aop;

/**
 * 包含了某切入点的所有Advisor，是切入点驱动通知的顶层接口
 * <p>Date: 2022/10/28
 * <p>Time: 11:15
 *
 * @Author SillyBaka
 **/
public interface PointcutAdvisor extends Advisor{

    /**
     * 获取当前Advisor所匹配的切入点
     */
    Pointcut getPointcut();
}
