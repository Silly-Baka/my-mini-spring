package sillybaka.springframework.exception;

/**
 * 在Aop配置过程中出现的异常
 * <p>Date: 2022/10/28
 * <p>Time: 20:26
 *
 * @Author SillyBaka
 **/
public class AopConfigException extends RuntimeException{

    public AopConfigException() {
    }

    public AopConfigException(String message) {
        super(message);
    }

    public AopConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
