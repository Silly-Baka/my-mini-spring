package sillybaka.springframework.aop.framework;

/**
 * 使用Cglib的动态代理实现的代理对象
 * <p>Date: 2022/10/27
 * <p>Time: 22:26
 *
 * @Author SillyBaka
 **/
public class CglibAopProxy implements AopProxy{

    private final AdvisedSupport advisedSupport;

    public CglibAopProxy(AdvisedSupport config){
        this.advisedSupport = config;
    }

    @Override
    public Object getProxy() {
        return null;
    }
}
