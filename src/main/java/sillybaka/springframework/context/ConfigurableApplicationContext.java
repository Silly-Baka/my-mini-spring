package sillybaka.springframework.context;

/**
 * 提供了配置ApplicationContext的接口，是拥有完整生命周期的最底层可用的ApplicationContext
 * 封装了ApplicationContext的生命周期，提供启动及关闭上下文等接口
 *  todo 后期将变为一个SPI接口
 * Date: 2022/10/20
 * Time: 15:40
 *
 * @Author SillyBaka
 **/
public interface ConfigurableApplicationContext extends ApplicationContext{

    /**
     * 启动或刷新上下文（实例化所有bean或不实例化）
     * 在实例化ApplicationContext的时候按需调用
     */
    void refresh();

    /**
     * 关闭上下文
     */
    void close();
}
