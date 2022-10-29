package sillybaka.springframework.aop;

import java.lang.reflect.Method;

/**
 * Description：
 * <p>Date: 2022/10/29
 * <p>Time: 20:51
 *
 * @Author SillyBaka
 **/
public class TestMethodAfterAdvice implements MethodAfterAdvice{
    @Override
    public void after(Method method, Object[] args, Object target) {
        System.out.println("方法执行后调用通知逻辑");
    }
}
