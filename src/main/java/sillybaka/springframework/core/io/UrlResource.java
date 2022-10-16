package sillybaka.springframework.core.io;

import sillybaka.springframework.exception.NestedIOException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Date: 2022/10/16
 * Time: 13:32
 *
 * @Author SillyBaka
 * Description：基于Url获取的资源（使用原url包）
 **/
public class UrlResource extends AbstractResource{

    private final URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new NestedIOException("不存在该url路径对应的资源");
        }
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public String getFileName() {
        return url.getFile();
    }
}
