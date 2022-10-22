package sillybaka.springframework.entity;

import lombok.Data;
import sillybaka.springframework.beans.factory.ApplicationContextAware;
import sillybaka.springframework.beans.factory.BeanFactory;
import sillybaka.springframework.beans.factory.BeanFactoryAware;
import sillybaka.springframework.context.ApplicationContext;

/**
 * Description：
 * Date: 2022/10/22
 * Time: 20:57
 *
 * @Author SillyBaka
 **/
@Data
public class TestAware implements BeanFactoryAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
