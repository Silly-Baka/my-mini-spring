package sillybaka.springframework.aop;

/**
 * TargetSource用于封装将要被Aop的目标对象
 * <p>Date: 2022/10/27
 * <p>Time: 23:05
 *
 * @Author SillyBaka
 **/
public class TargetSource {

    private final Object target;

    public TargetSource(Object target){
        this.target = target;
    }
    /**
     * 在Aop通知的逻辑执行之前调用，获取被Aop的目标对象
     * @return 连接点所在的目标对象，若不存在则返回null
     */
    public Object getTarget(){
        return this.target;
    }

    /**
     * 获取被Aop代理的目标对象的类型
     * 若为null则说明代理的是方法，否则代理的是一个类
     * @return
     */
    public Class<?> getTargetClass(){
        return this.target.getClass();
    }
}
