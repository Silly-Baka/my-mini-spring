package sillybaka.springframework.core.io;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Date: 2022/10/16
 * Time: 14:39
 *
 * @Author SillyBaka
 * Description：默认的ResourceLoader实现类，根据location前缀的不同，使用策略模式获取Resource
 **/
public class DefaultResourceLoader implements ResourceLoader{

    private static final String CLASSPATH_PREFIX = "classpath:";

    @Override
    public Resource getResource(String location) {
        // 如果以classpath开头，则返回ClassPathResource
        if(location.startsWith(CLASSPATH_PREFIX)){
            return new ClassPathResource(location.substring(CLASSPATH_PREFIX.length()));
        }else {
        // 否则先尝试转换成url
            try {
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException e) {
            // url格式错误，再尝试使用file体系
                return new FileSystemResource(location);
            }
        }
    }
}
