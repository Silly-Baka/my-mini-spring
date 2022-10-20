package sillybaka.springframework.context.support;

import sillybaka.springframework.context.support.AbstractXmlApplicationContext;
import sillybaka.springframework.core.io.Resource;

/**
 * 基于{@link AbstractXmlApplicationContext}的子类
 * 该子类是提供给用户使用的实现类，可按照给定的文件系统路径（绝对路径或相对路径)自动加载bean
 *
 * Date: 2022/10/20
 * Time: 21:14
 *
 * @see AbstractXmlApplicationContext
 * @Author SillyBaka
 **/
public class FileSystemXmlApplicationContext extends AbstractXmlApplicationContext {

    private final Resource[] resources;

    public FileSystemXmlApplicationContext(String configLocation){
        this(new String[]{configLocation},true);
    }
    public FileSystemXmlApplicationContext(String...configLocations){
        this(configLocations,true);
    }
    public FileSystemXmlApplicationContext(String[] configLocations,boolean refresh){
        resources = getResourcesByLocations(configLocations);

        if(refresh){
            refresh();
        }
    }

    @Override
    protected Resource[] getConfigResources() {
        return resources;
    }
}
