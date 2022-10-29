package sillybaka.springframework.aop.framework.adapter;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import sillybaka.springframework.aop.Advisor;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link AdvisorAdapterRegistry} 接口的默认实现，保存Spring支持的通知、拦截器的适配器，由Spring内部使用
 * <p>使用时注意：在注册新的适配器时，也要提供定义对应的通知类型和拦截器类型
 * <p>Date: 2022/10/28
 * <p>Time: 16:53
 *
 * @Author SillyBaka
 **/
public class DefaultAdvisorAdapterRegistry implements AdvisorAdapterRegistry{

    private final List<AdvisorAdapter> adapters = new ArrayList<>();

    public DefaultAdvisorAdapterRegistry(){
        // 注册内置的adapter
        this.adapters.add(new MethodBeforeAdviceAdapter());
    }

    @Override
    public void registerAdvisorAdapter(AdvisorAdapter adapter) {
        this.adapters.add(adapter);
    }

    @Override
    public MethodInterceptor[] getInterceptors(Advisor advisor) {
        List<MethodInterceptor> interceptors = new ArrayList<>();
        Advice advice = advisor.getAdvice();

        // 在注册表中查看是否有支持该类型通知的适配器
        for(AdvisorAdapter adapter : adapters){
            if(adapter.supportAdvice(advice)){
                interceptors.add(adapter.getInterceptor(advisor));
            }
        }

        return interceptors.toArray(new MethodInterceptor[0]);
    }
}
