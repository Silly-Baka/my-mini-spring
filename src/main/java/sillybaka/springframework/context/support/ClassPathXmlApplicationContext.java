package sillybaka.springframework.context.support;


import sillybaka.springframework.core.io.Resource;

/**
 * 基于{@link AbstractXmlApplicationContext}的子类
 * 该子类是提供给用户使用的实现类，可按照给定的类路径自动加载bean
 *
 * Date: 2022/10/20
 * Time: 20:48
 *
 * @see AbstractXmlApplicationContext
 * @Author SillyBaka
 **/
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private final Resource[] configResources;

    public ClassPathXmlApplicationContext(String configLocation){
        this(new String[]{configLocation},true);
    }
    public ClassPathXmlApplicationContext(String...configLocations) {
        this(configLocations,true);
    }

    /**
     * @param refresh 是否启动上下文
     */
    public ClassPathXmlApplicationContext(String[] configLocations,boolean refresh){
        configResources = getResourcesByLocations(configLocations);
        if(refresh){
            refresh();
        }
    }

    @Override
    protected Resource[] getConfigResources() {
        return configResources;
    }


}
