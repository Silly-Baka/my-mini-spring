package sillybaka.springframework.core.io;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ClassUtil;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Date: 2022/10/16
 * Time: 14:28
 *
 * @Author SillyBaka
 * Description：
 **/
public class testResourceAndResourceLoader {

    /**
     * 测试DefaultResourceLoader
     */
    @Test
    public void testDefaultResourceLoader() throws FileNotFoundException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        // 测试加载classpath下的资源
        Resource resource = resourceLoader.getResource("classpath:test.txt");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(resource);
        System.out.println(content);
        System.out.println("-------------");

        // 测试加载文件系统的资源
        // 相对路径
        resource = resourceLoader.getResource("src/test/resources/test.txt");
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(resource);
        System.out.println(content);
        System.out.println("-------------");
        // 绝对路径
        resource = resourceLoader.getResource("C:/Users/86176/Desktop/JAVA/my-mini-spring/src/test/resources/test.txt");
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(resource);
        System.out.println(content);
        System.out.println("-------------");

        // 测试加载基于URL的资源
        resource = resourceLoader.getResource("https://www.baidu.com");
        inputStream = resource.getInputStream();
        System.out.println(resource);
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
        System.out.println("-------------");
    }
}
