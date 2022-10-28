package sillybaka.springframework.aop.framework;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * 基于反射实现的方法调用者，用于执行被拦截的连接点及其通知
 * <p>Date: 2022/10/28
 * <p>Time: 13:51
 *
 * @Author SillyBaka
 **/
public class ReflectiveMethodInvocation implements MethodInvocation {
    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Object proceed() throws Throwable {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return null;
    }
}
