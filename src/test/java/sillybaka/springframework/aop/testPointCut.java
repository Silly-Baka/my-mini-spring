package sillybaka.springframework.aop;

import org.junit.Test;
import sillybaka.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

/**
 * Description：测试切入点的测试类
 * <p>Date: 2022/10/27
 * <p>Time: 21:33
 *
 * @Author SillyBaka
 **/
public class testPointCut {

    @Test
    public void testPointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* sillybaka.springframework.aop.*.*(..))");

        Class<TestService> clazz = TestService.class;

        // 查看匹配类
        System.out.println(pointcut.matches(clazz));

        // 匹配方法
        Method testPointCut = clazz.getDeclaredMethod("testPointCut");

        System.out.println(pointcut.matches(testPointCut, clazz));

        System.out.println(pointcut.matches(sillybaka.springframework.aop.testPointCut.class));

        Method test2 = clazz.getDeclaredMethod("test2", Integer.TYPE);
        System.out.println(pointcut.matches(test2,clazz));
    }
}
