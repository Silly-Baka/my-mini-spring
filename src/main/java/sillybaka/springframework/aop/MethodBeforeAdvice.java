package sillybaka.springframework.aop;


import java.lang.reflect.Method;

/**
 * 表示在调用方法之前执行的通知
 * <p>Date: 2022/10/27
 * <p>Time: 22:12
 *
 * @Author SillyBaka
 **/
public interface MethodBeforeAdvice extends BeforeAdvice{

    /**
     * 在调用通知所在的方法（连接点）前 会调用此方法，用于增强方法逻辑
     * @param method 要调用的方法
     * @param args 参数表
     * @param target 要调用方法的目标对象
     */
    void before(Method method,Object[] args,Object target);
}
