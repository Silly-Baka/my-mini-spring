package sillybaka.springframework.aop;

import java.lang.reflect.Method;

/**
 * 方法匹配器接口，PointCut的一部分
 * <p>Date: 2022/10/27
 * <p>Time: 21:02
 *
 * @Author SillyBaka
 **/
public interface MethodMatcher {

    /**
     * 检查目标类的目标方法是否匹配
     * @param method 目标方法
     * @param targetClass 目标类
     * @return 匹配的结果
     */
    boolean matches(Method method, Class<?> targetClass);

}
