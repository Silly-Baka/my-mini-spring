package sillybaka.springframework.aop.framework;

/**
 * Aop代理类的工厂接口，用于创建代理
 * <p>Date: 2022/10/27
 * <p>Time: 22:48
 *
 * @Author SillyBaka
 **/
public interface AopProxyFactory {

    /**
     * 根据Aop代理配置创建一个代理
     * @param config Aop代理的配置类
     * @return 代理对象
     */
    AopProxy createAopProxy(AdvisedSupport config);
}
