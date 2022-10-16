package sillybaka.springframework.core.io;

import sillybaka.springframework.exception.NestedIOException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Date: 2022/10/16
 * Time: 13:31
 *
 * @Author SillyBaka
 * Description：基于文件系统的资源，根据文件路径获取的资源（针对本地文件资源）
 **/
public class FileSystemResource extends AbstractResource{

    private final Path filePath;

    private final File file;

    public FileSystemResource(String filePath) {
        file = new File(filePath);
        this.filePath = Paths.get(file.toURI());
    }

    public FileSystemResource(Path filePath) {
        this.filePath = filePath;
        file = new File(filePath.toUri());
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new FileNotFoundException("文件系统中无法找到该资源:"+filePath);
        }
    }

    @Override
    public URL getURL() {
        try {
            return filePath.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new NestedIOException("不合法的文件路径或URI格式");
        }
    }

    @Override
    public URI getURI(){
        return filePath.toUri();
    }

    @Override
    public String getFileName() {
        return file.getName();
    }
}
