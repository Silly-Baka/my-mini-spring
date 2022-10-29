package sillybaka.springframework.aop.framework;


import org.aopalliance.aop.Advice;
import sillybaka.springframework.aop.Advisor;
import sillybaka.springframework.aop.TargetSource;
import sillybaka.springframework.aop.support.DefaultPointcutAdvisor;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于配置代理类，封装了需要被代理的连接点、通知链、以及其实现的接口等信息
 * <p>Date: 2022/10/27
 * <p>Time: 23:03
 *
 * @Author SillyBaka
 **/
public class AdvisedSupport {

    /**
     * 被Aop的目标对象（连接点）
     */
    private TargetSource targetSource;
    /**
     * 是否代理目标类
     * 若是 则为Cglib动态代理
     * 若否 则为Jdk动态代理
     */
    private boolean isProxyTargetClass = true;
    /**
     * 连接点对应的通知链
     */
    private final List<Advisor> advisors = new ArrayList<>();
    /**
     * todo 代理需要实现的接口（源码中这里太分散了 可能会弃了这个属性 只实现简单的动态代理）
     */
    private final List<Class<?>> interfaces = new ArrayList<>();
    /**
     * 通知链工厂
     */
    private final AdvisorChainFactory chainFactory = new DefaultAdvisorChainFactory();

    private Class<?>[] cacheInterfaces;

    public AdvisedSupport(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public void addAdvice(Advice advice){
        addAdvisor(advice);
    }

    public void addAdvisor(Advice advice){
        if(advice != null){
            this.advisors.add(new DefaultPointcutAdvisor(advice));
            this.cacheInterfaces = null;
        }
    }
    public void addAdvisor(Advisor advisor){
        if(advisor != null){
            this.advisors.add(advisor);
            this.cacheInterfaces = null;
        }
    }
    public void addAdvisors(Advisor ... advisors){
        for (Advisor advisor : advisors){
             if(advisor != null){
                 this.advisors.add(advisor);
                 this.cacheInterfaces = null;
             }
        }
    }

    public void addInterface(Class<?> inf){
        if(inf != null){
            interfaces.add(inf);
            this.cacheInterfaces = null;
        }
    }
    public void addInterfaces(Class<?> ... interfaces){
        for (Class<?> inf : interfaces) {
            if(inf != null){
                this.interfaces.add(inf);
                this.cacheInterfaces = null;
            }
        }
    }

    public List<Advisor> getAdvisors() {
        return this.advisors;
    }

    //todo 等待复用
//    public Class<?>[] getInterfaces(){
//        if(this.cacheInterfaces == null){
//            this.cacheInterfaces = new Class[interfaces.size()];
//            interfaces.toArray(this.cacheInterfaces);
//        }
//        return this.cacheInterfaces;
//    }

    public AdvisorChainFactory getAdvisorChainFactory(){
        return this.chainFactory;
    }

    public Class<?> getTargetClass(){
        return targetSource.getTargetClass();
    }

    //todo 这里实现了简单的获取接口
    public Class<?>[] getInterfaces(){
        return targetSource.getTargetClass().getInterfaces();
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        isProxyTargetClass = proxyTargetClass;
    }

    public boolean isProxyTargetClass() {
        return isProxyTargetClass;
    }
}
