package sillybaka.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * 通用的通知类，适用于所有类型的通知
 * <p>Date: 2022/10/28
 * <p>Time: 11:13
 *
 * @Author SillyBaka
 **/
public interface Advisor {

    /**
     * 空白的通知对象
     */
    Advice EMPTY_INSTANCE = new Advice() {};

    /**
     * 若配置了通知则返回通知对象，否则返回空白对象
     */
    Advice getAdvice();


}
