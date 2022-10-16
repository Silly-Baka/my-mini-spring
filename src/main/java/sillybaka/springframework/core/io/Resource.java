package sillybaka.springframework.core.io;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * Date: 2022/10/15
 * Time: 21:44
 *
 * @Author SillyBaka
 * Description：所有资源的抽象（一个Resource代表一个资源）
 **/
public interface Resource {

    /**
     * 判断当前资源是否存在
     */
    boolean isExist();

    /**
     * 获取当前资源的二进制流 由具体Resource类实现
     */
    InputStream getInputStream() throws FileNotFoundException;

    /**
     * 获取当前资源的URL
     */
    URL getURL() throws FileNotFoundException;

    /**
     * 获取当前资源的URI
     */
    URI getURI();

    /**
     * 获取该资源文件的名字
     */
    String getFileName();
}
