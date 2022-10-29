package sillybaka.springframework.aop.support;

import sillybaka.springframework.aop.ClassFilter;
import sillybaka.springframework.aop.MethodMatcher;
import sillybaka.springframework.aop.Pointcut;

import java.lang.reflect.Method;

/**
 * 匹配了所有类所有方法的规范的Pointcut
 * <p>Date: 2022/10/29
 * <p>Time: 13:52
 *
 * @Author SillyBaka
 **/
public class DefaultPointcut implements Pointcut,ClassFilter,MethodMatcher {

    public static final DefaultPointcut INSTANCE = new DefaultPointcut();

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public boolean matches(Class<?> targetClass) {
        return true;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return true;
    }
}
