package sillybaka.springframework.exception;

/**
 * Date: 2022/10/16
 * Time: 13:23
 *
 * @Author SillyBaka
 * Description：
 **/
public class BeansException extends RuntimeException{
    public BeansException() {
    }

    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }
}
