package sillybaka.springframework.aop;

/**
 * 切入点的抽象接口
 * <p>Date: 2022/10/27
 * <p>Time: 21:01
 *
 * @Author SillyBaka
 **/
public interface Pointcut {

    /**
     * 获取当前切入点的类过滤器
     */
    ClassFilter getClassFilter();

    /**
     * 获取当前切入点的方法过滤器
     */
    MethodMatcher getMethodMatcher();


}
