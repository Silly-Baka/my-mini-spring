package sillybaka.springframework.exception;

/**
 * Date: 2022/10/16
 * Time: 13:23
 *
 * @Author SillyBaka
 * Description：
 **/
public class NestedIOException extends RuntimeException{
    public NestedIOException() {
    }

    public NestedIOException(String message) {
        super(message);
    }

    public NestedIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
