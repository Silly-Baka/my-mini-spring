package sillybaka.springframework.aop;

import java.lang.reflect.Method;

/**
 * Description：
 * <p>Date: 2022/10/29
 * <p>Time: 14:11
 *
 * @Author SillyBaka
 **/
public class TestMethodBeforeAdvice implements MethodBeforeAdvice{
    @Override
    public void before(Method method, Object[] args,Object target) {
        System.out.println("测试方法执行前通知");
    }
}
