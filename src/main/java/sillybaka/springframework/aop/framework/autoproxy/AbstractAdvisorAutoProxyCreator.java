package sillybaka.springframework.aop.framework.autoproxy;

import org.aopalliance.aop.Advice;
import sillybaka.springframework.aop.Advisor;
import sillybaka.springframework.aop.ClassFilter;
import sillybaka.springframework.aop.Pointcut;
import sillybaka.springframework.aop.TargetSource;
import sillybaka.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import sillybaka.springframework.aop.framework.AdvisedSupport;
import sillybaka.springframework.aop.framework.AopProxyFactory;
import sillybaka.springframework.aop.framework.DefaultAopProxyFactory;
import sillybaka.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import sillybaka.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.beans.factory.BeanFactoryAware;
import sillybaka.springframework.beans.factory.ConfigurableListableBeanFactory;
import sillybaka.springframework.beans.factory.config.BeanDefinition;
import sillybaka.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import sillybaka.springframework.beans.factory.support.DefaultListableBeanFactory;
import sillybaka.springframework.beans.factory.support.SimpleInstantiationStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link InstantiationAwareBeanPostProcessor} 接口的实现<p>
 * 可以用于自动将Bean按照已注册的AOP配置，封装成一个代理对象（基于Advisor）
 * <p>Date: 2022/11/1
 * <p>Time: 19:27
 *
 * @Author SillyBaka
 **/
public class AbstractAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private final AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();

    private DefaultListableBeanFactory beanFactory;

    public AbstractAdvisorAutoProxyCreator(){}

    public AbstractAdvisorAutoProxyCreator(ConfigurableListableBeanFactory beanFactory){
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {

        // 检查当前bean类型是否为AOP基础构件
        if(isInfrastructureClass(beanClass)){
            // 如果是则无法生成代理对象
            return null;
        }

        BeanDefinition<?> beanDefinition = beanFactory.getBeanDefinition(beanName, beanClass);
        // 正常这里应该是从IOC容器的某注册表中获取该bean定义对应的快照实例
        TargetSource targetSource = new TargetSource(new SimpleInstantiationStrategy().instantiation(beanDefinition));

        return createProxy(beanClass,beanName,targetSource);
    }


    /**
     * 为指定bean创建代理对象的实际逻辑
     */
    protected Object createProxy(Class<?> beanClass,String beanName,TargetSource targetSource){
        // 创建IOC配置类对象
        AdvisedSupport advisedSupport = new AdvisedSupport();

        advisedSupport.setTargetSource(targetSource);
        // 默认采用Cglib动态代理
        advisedSupport.setProxyTargetClass(true);

        // todo 这里先获取大家都通用的Advisor，后面应该可能有指定给哪个Bean的Advisor
        String[] advisorNames = beanFactory.getBeanNamesForType(Advisor.class, true);

        List<Object> advisors = new ArrayList<>();
        for (String advisorName : advisorNames) {
            Object advisorObject = beanFactory.getBean(advisorName);
            // 匹配每一个Advisor
            if(advisorObject instanceof AspectJExpressionPointcutAdvisor){
                AspectJExpressionPointcutAdvisor advisor = (AspectJExpressionPointcutAdvisor) advisorObject;
                Pointcut pointcut = advisor.getPointcut();
                ClassFilter classFilter = pointcut.getClassFilter();
                // 判断该Advisor是否匹配当前要代理的bean
                if(classFilter.matches(beanClass)){
                    advisors.add(advisor);
                }
            }
        }
        // 如果没有对应的Advisor 则说明无法生成代理对象 返回null
        if(advisors.size() == 0){
            return null;
        }
        advisedSupport.addAdvisors(advisors.toArray(new Advisor[0]));

        AopProxyFactory aopProxyFactory = new DefaultAopProxyFactory();

        return aopProxyFactory.createAopProxy(advisedSupport).getProxy();
    }

    /**
     * 检查将要被代理的类是否是AOP的基础构件
     * @param targetClass 被代理类
     */
    protected boolean isInfrastructureClass(Class<?> targetClass){
        boolean retVal = Advice.class.isAssignableFrom(targetClass) ||
                Advisor.class.isAssignableFrom(targetClass) ||
                Pointcut.class.isAssignableFrom(targetClass);

        return retVal;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
