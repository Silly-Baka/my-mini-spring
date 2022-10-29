package sillybaka.springframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import sillybaka.springframework.aop.Advisor;

/**
 * AdvisorAdapter注册中心的接口，存放不同AdvisorAdapter的单例对象
 * <p>Date: 2022/10/28
 * <p>Time: 16:33
 *
 * @Author SillyBaka
 **/
public interface AdvisorAdapterRegistry {

    /**
     * 向注册表注册目标AdvisorAdapter
     * @param adapter 要注册的adapter
     */
    void registerAdvisorAdapter(AdvisorAdapter adapter);

    /**
     * 在注册中心中获取支持目标advisor的拦截器列表
     * @param advisor 要查询的advisor
     */
    MethodInterceptor[] getInterceptors(Advisor advisor);
}
