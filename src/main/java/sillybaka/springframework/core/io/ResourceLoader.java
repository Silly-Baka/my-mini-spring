package sillybaka.springframework.core.io;

/**
 * Date: 2022/10/16
 * Time: 14:36
 *
 * @Author SillyBaka
 * Description：资源查找定位策略的抽象 用于根据路径获取资源
 **/
public interface ResourceLoader {
    /**
     * 根据资源路径加载相应的Resource
     * @param location 资源路径
     * @return
     */
    Resource getResource(String location);
}
