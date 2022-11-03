package sillybaka.springframework.beans.factory.config;

import sillybaka.springframework.beans.factory.ApplicationContextAware;
import sillybaka.springframework.beans.factory.ConfigurableListableBeanFactory;
import sillybaka.springframework.context.ApplicationContext;

import java.util.Properties;

/**
 * 根据properties文件中的属性，对BeanDefinition中的占位符进行替换
 * <p>Date: 2022/11/2
 * <p>Time: 18:12
 *
 * @Author SillyBaka
 **/
public class PropertyPlaceholderConfigurer extends PropertyResourceConfigurer implements ApplicationContextAware {

    private static final String PLACEHOLDER_PREFIX = "${";
    private static final String PLACEHOLDER_SUFFIX = "}";

    private ApplicationContext applicationContext;

    // 测试用
    private String location;

    public PropertyPlaceholderConfigurer(){}
    public PropertyPlaceholderConfigurer(ApplicationContext applicationContext){
        setApplicationContext(applicationContext);
    }

    public PropertyPlaceholderConfigurer(ApplicationContext applicationContext,String...locations){
        this(applicationContext);
        setLocations(locations);
    }

    /**
     * 访问BeanFactory中的每一个bean的定义，尝试能否使用props文件中的属性来替换其中的属性占位符
     * @param beanFactory 指定的BeanFactory
     * @param props property属性集合
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition<?> beanDefinition = beanFactory.getBeanDefinitionByName(beanDefinitionName);
            resolvePropertyValues(beanDefinition,props);
        }
    }

    /**
     * 根据props解析并替换目标bean定义中的占位符<p>
     * 占位符格式： ${propertyName}
     * @param beanDefinition 目标bean定义
     * @param props property属性集合
     */
    protected void resolvePropertyValues(BeanDefinition<?> beanDefinition,Properties props){
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if(propertyValues == null){
            return;
        }
        for (PropertyValue pv : propertyValues.getPropertyValues()) {
            Object originValue = pv.getPropertyValue();
            if(originValue instanceof String){
                String propertyValue = (String) originValue;
                // 如果属性值是占位符 则替换
                if(propertyValue.startsWith(PLACEHOLDER_PREFIX) && propertyValue.endsWith(PLACEHOLDER_SUFFIX)){
                    int length = propertyValue.length();
                    propertyValue = propertyValue.substring(PLACEHOLDER_PREFIX.length(),length-PLACEHOLDER_SUFFIX.length());

                    // 作为key从props中获取属性
                    propertyValue = props.getProperty(propertyValue);
                }
                propertyValues.addPropertyValue(new PropertyValue(pv.getPropertyName(),propertyValue));
            }
        }
        beanDefinition.setPropertyValues(propertyValues);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        setResourceLoader(applicationContext);
    }

    public void setLocation(String location) {
        this.location = location;
        super.setLocations(location);
    }

    public String getLocation() {
        return location;
    }
}
