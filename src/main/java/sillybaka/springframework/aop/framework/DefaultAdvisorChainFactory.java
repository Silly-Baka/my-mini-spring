package sillybaka.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import sillybaka.springframework.aop.Advisor;
import sillybaka.springframework.aop.MethodMatcher;
import sillybaka.springframework.aop.PointcutAdvisor;
import sillybaka.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import sillybaka.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link AdvisorChainFactory}接口的默认实现类
 * <p>Date: 2022/10/28
 * <p>Time: 14:14
 *
 * @Author SillyBaka
 **/
public class DefaultAdvisorChainFactory implements AdvisorChainFactory{

    @Override
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(AdvisedSupport config, Method method, Class<?> targetClass) {

        Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());

        AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();

        List<Object> interceptorList = new ArrayList<>();

        List<Advisor> advisors = config.getAdvisors();
        for (Advisor advisor : advisors) {
            if(advisor instanceof PointcutAdvisor){
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
                // 判断类是否符合切入点的表达式 符合就继续判断
                if(pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)){
                    MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
                    boolean match = mm.matches(method, targetClass);
                    // 如果方法也匹配 则说明当前Advisor可以被执行 从注册表中获取拦截它的所有拦截器
                    if(match){
                        MethodInterceptor[] interceptors = registry.getInterceptors(pointcutAdvisor);

                        //todo 需要修改成运行时
                        interceptorList.addAll(Arrays.asList(interceptors));
                    }
                }
            }else {
                MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
                interceptorList.addAll(Arrays.asList(interceptors));
            }
        }
        return interceptorList;
    }
}
