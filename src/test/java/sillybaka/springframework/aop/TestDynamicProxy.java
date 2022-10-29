package sillybaka.springframework.aop;

import org.junit.Test;
import sillybaka.springframework.aop.aspectj.AspectJExpressionPointcut;
import sillybaka.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import sillybaka.springframework.aop.framework.*;

/**
 * Description：测试动态代理的测试类
 * <p>Date: 2022/10/28
 * <p>Time: 20:34
 *
 * @Author SillyBaka
 **/
public class TestDynamicProxy {

    @Test
    public void testJDKProxy(){
        TargetSource targetSource = new TargetSource(new HelloServiceImpl());
        AdvisedSupport advisedSupport = new AdvisedSupport(targetSource);

        //添加切入点和通知
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor(new TestMethodBeforeAdvice(),
                new AspectJExpressionPointcut("execution(* sillybaka.springframework.aop.*.*(..))"));
        advisedSupport.addAdvisor(advisor);

        AopProxyFactory aopProxyFactory = new DefaultAopProxyFactory();
        AopProxy aopProxy = aopProxyFactory.createAopProxy(advisedSupport);

        HelloService helloService = (HelloService) aopProxy.getProxy();
        helloService.hello();
    }
}
