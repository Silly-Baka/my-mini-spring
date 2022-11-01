package sillybaka.springframework.aop.framework.adapter;

/**
 * 全局通用的AdvisorAdapter注册表，用于获取单例的注册表<p>
 * 用户可以用于获取全局的单例注册表，并注册自定义类型的通知和拦截器进Spring中
 * <p>Date: 2022/11/1
 * <p>Time: 19:38
 *
 * @Author SillyBaka
 **/
public class GlobalAdvisorAdapterRegistry {

    private static AdvisorAdapterRegistry instance = new DefaultAdvisorAdapterRegistry();

    public static AdvisorAdapterRegistry getInstance(){
        return instance;
    }
}
