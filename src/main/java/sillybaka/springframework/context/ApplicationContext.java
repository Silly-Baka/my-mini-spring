package sillybaka.springframework.context;

import sillybaka.springframework.beans.factory.HierarchicalBeanFactory;
import sillybaka.springframework.beans.factory.ListableBeanFactory;
import sillybaka.springframework.core.io.ResourceLoader;

/**
 * Date: 2022/10/20
 * Time: 15:35
 *
 * @Author SillyBaka
 * Description：Spring中的应用程序上下文中央接口，提供了一个上下文可执行的基本接口（获取上下文信息等）
 **/
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader {
}
