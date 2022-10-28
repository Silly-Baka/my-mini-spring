package sillybaka.springframework.aop;

/**
 * TargetSource用于封装和获取将要被Aop的目标对象（在Spring中为一个Method）
 * <p>Date: 2022/10/27
 * <p>Time: 23:05
 *
 * @Author SillyBaka
 **/
public interface TargetSource {

    /**
     * 在Aop通知的逻辑执行之前调用，获取被Aop的目标对象
     * @return 连接点所在的目标对象，若不存在则返回null
     */
    Object getTarget();
}
