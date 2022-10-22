package sillybaka.springframework.beans.factory;

/**
 * beans可实现的接口，若bean实现了该接口，BeanFactory应在其销毁时调用{@link #destroy()}
 * Date: 2022/10/21
 * Time: 23:32
 *
 * @Author SillyBaka
 **/
public interface DisposableBean {

    /**
     * 在bean销毁时，由其所属的BeanFactory调用，可自定义实现
     */
    void destroy();
}
