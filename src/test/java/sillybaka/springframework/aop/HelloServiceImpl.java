package sillybaka.springframework.aop;

/**
 * Description：
 * <p>Date: 2022/10/28
 * <p>Time: 20:40
 *
 * @Author SillyBaka
 **/
public class HelloServiceImpl implements HelloService{
    @Override
    public void hello() {
        System.out.println("未被动态代理前的方法");
    }
}
