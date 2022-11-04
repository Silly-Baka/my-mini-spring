package sillybaka.springframework.beans.factory.config;

import cn.hutool.core.io.resource.ResourceUtil;
import org.springframework.core.NestedIOException;
import sillybaka.springframework.core.io.Resource;
import sillybaka.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 提供了加载本地上下文所有property文件功能的基类
 * <p>Date: 2022/11/2
 * <p>Time: 19:41
 *
 * @Author SillyBaka
 **/
public class PropertiesLoaderSupport {

    private ResourceLoader resourceLoader;

    private String[] locations;

    public PropertiesLoaderSupport(){}

    public PropertiesLoaderSupport(String...locations){
        this.locations = locations;
    }

    /**
     * 从上下文中加载所有的properties 并返回一个合并的集合
     * @return 上下文中所有properties的集合
     */
    protected Properties mergeProperties() throws NestedIOException {

        Properties merge = new Properties();

        // 如果没有指定的路径 则自动扫描
        if(this.locations == null){
            this.locations = autoScanProperties();
        }

        for (String location : this.locations) {
            Resource resource = resourceLoader.getResource(location);
            try {
                Properties properties = new Properties();
                properties.load(resource.getInputStream());

                merge.putAll(properties);
            } catch (IOException e) {
                throw new NestedIOException("load properties [" + resource.getFileName() + "] error");
            }
        }

        return merge;
    }

    /**
     * 自动扫描类路径下的properties文件路径
     */
    protected String[] autoScanProperties(){
        List<String> locationList = new ArrayList<>();
        File file = new File("src/main/resources");
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null){
                for (File listFile : files) {
                    if(listFile.getName().endsWith(".properties")){
                        locationList.add(listFile.toURI().toString());
                    }
                }
            }
        }
        return locationList.toArray(new String[0]);
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setLocations(String...locations) {
        this.locations = locations;
    }
}
