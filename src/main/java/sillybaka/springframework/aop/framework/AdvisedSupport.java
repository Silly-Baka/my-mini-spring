package sillybaka.springframework.aop.framework;

import org.aopalliance.aop.Advice;
import sillybaka.springframework.aop.Advisor;
import sillybaka.springframework.aop.TargetSource;

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
     * 连接点对应的通知链
     */
    private final List<Advisor> advisors = new ArrayList<>();
    /**
     * 连接点实现的接口（用于创建Jdk动态代理对象）
     */
    private final List<Class<?>> interfaces = new ArrayList<>();

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

    public Class<?>[] getInterfaces(){
        if(this.cacheInterfaces == null){
            this.cacheInterfaces = new Class[interfaces.size()];
            interfaces.toArray(this.cacheInterfaces);
        }
        return this.cacheInterfaces;
    }
}
