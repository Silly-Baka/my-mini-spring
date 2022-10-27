package sillybaka.springframework.aop;

/**
 * 类过滤器接口，PointCut的一部分
 * <p>Date: 2022/10/27
 * <p>Time: 21:02
 *
 * @Author SillyBaka
 **/
public interface ClassFilter {

    /**
     * 检查目标类是否匹配
     * @param targetClass 目标类
     * @return true-匹配  false-不匹配
     */
    boolean matches(Class<?> targetClass);
}
