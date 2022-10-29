package sillybaka.springframework.aop.framework;

/**
 * Aop代理的顶层抽象接口，用于提供获取代理对象的接口
 * <p>Date: 2022/10/27
 * <p>Time: 22:10
 *
 * @Author SillyBaka
 **/
public interface AopProxy {

    /**
     * 获取（创建）一个代理对象
     * @return
     */
    Object getProxy();
}
