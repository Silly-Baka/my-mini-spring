package sillybaka.springframework.beans.factory.support;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import sillybaka.springframework.beans.factory.*;
import sillybaka.springframework.beans.factory.config.*;
import sillybaka.springframework.exception.BeansException;
import sillybaka.springframework.utils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


/**
 * Description：抽象的自动装配Bean工厂 --> 负责处理创建Bean、填充Bean（将Bean装配成一个完整的对象）的实际逻辑
 * Date: 2022/10/13
 * Time: 19:40
 *
 * @Author SillyBaka
 **/
@Slf4j
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private static final InstantiationStrategy INSTANTIATION_STRATEGY = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName,BeanDefinition<?> beanDefinition) {

        // 先给它一次机会，看能否实例化为代理对象
        Object bean = resolveBeforeInstantiation(beanName,beanDefinition);
        if(bean != null){
            return bean;
        }

        // 若无法生成代理对象，则执行原实例化策略
        return doCreateBean(beanName,beanDefinition);
    }


    /**
     * 创建Bean实例的实际逻辑
     */
    public Object doCreateBean(String beanName,BeanDefinition<?> beanDefinition){

        // 实例化
        Object beanInstance = INSTANTIATION_STRATEGY.instantiation(beanDefinition);

        // 自动装配属性
        populateBean(beanName,beanDefinition,beanInstance);

        // 执行bean的初始化方法
        try {
            beanInstance = initializeBean(beanName,beanInstance,beanDefinition);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new BeansException("bean initialize error",e);
        }

        // 检查当前bean是否有自定义的destroy方法，若有则需要注册进注册表
        registerDisposableBeanIfNecessary(beanName,beanInstance,beanDefinition);

        return beanInstance;
    }


    /**
     * 将bean定义中的属性以及@Value中的属性注入给指定bean实例
     * @param beanName bean名字
     * @param beanDefinition bean定义
     * @param wrappedBean 需要被注入的bean实例
     * @return 注入属性后的bean实例
     */
    protected void populateBean(String beanName,BeanDefinition<?> beanDefinition,Object wrappedBean){

        PropertyValues pvs = beanDefinition.getPropertyValues();
        if(pvs == null){
            pvs = new PropertyValues();
        }

        // 看是否有注册实例化后置处理器
        boolean hasInsBeanPostProcessor = hasInstantiationAwareBeanPostProcessors();

        // 有则后置处理属性
        if(hasInsBeanPostProcessor){
            PropertyValues result;
            for (InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor : getInstantiationAwareBeanPostProcessorCache()) {
                result = instantiationAwareBeanPostProcessor.postProcessPropertyValues(pvs,wrappedBean,beanName);
                if(result != null){
                    pvs = result;
                }
            }
        }
        // 装配属性
        autoWirePropertyValues(beanName,wrappedBean,beanDefinition,pvs);

    }
    /**
     * 为bean实例对象自动装配属性 (底层使用setter注入）
     * @param beanName bean名字
     * @param existingBean 实例对象
     * @param beanDefinition bean定义
     */
    @Override
    public void autoWirePropertyValues(String beanName, Object existingBean, BeanDefinition<?> beanDefinition,PropertyValues pvs){

        if(pvs == null){
            pvs = beanDefinition.getPropertyValues();
        }
        if(pvs == null){
            return;
        }

        Class<?> clazz = existingBean.getClass();

        //todo 获取该Bean类的所有PropertyDescriptor --> 生命周期开始为在XMl扫描时按需调用
        Map<String, PropertyDescriptor> beanPropertyMap = PropertyUtils.getBeanPropertyMap(clazz);

        Map<String, Class<?>> propertyTypeMap = PropertyUtils.getPropertyTypeMap(clazz);


        for(PropertyValue pv : pvs.getPropertyValues()){

            String propertyName = pv.getPropertyName();
            Object propertyValue = pv.getPropertyValue();

            try {
                //todo xml配置中 普通属性不能配置类型 应该以其他方式获取
//                Class<?> propertyType = clazz.getDeclaredField(propertyName).getType();
                PropertyDescriptor pd = beanPropertyMap.get(propertyName);

                // 从属性类型map中获取属性类型
                Class<?> propertyType = propertyTypeMap.get(propertyName);

                Method setterMethod = pd.getWriteMethod();
                // 引用类型
                if(propertyType == BeanReference.class){

                    BeanReference beanReference = (BeanReference) propertyValue;
                    String referBeanName = beanReference.getBeanName();
                    //todo 获取Bean实例 --> 1、有可能该Bean实例尚未创建 应该重写为阻塞等待 （不会有这种情况，因为是懒汉式的设计）
                    //                    2、有可能会发生循环依赖，在后面再解决
                    Object innerBean = getBean(referBeanName);

                    setterMethod.invoke(existingBean,innerBean);

                }else if(propertyType == BeanDefinition.class){
                // Bean类型（级联定义了一个Bean）
                    //todo 先根据Bean定义创建Bean实例  --> 有可能破坏单例 需要保存在多例注册表中
                    BeanDefinition<?> innerBeanDefinition = (BeanDefinition<?>) propertyValue;
                    Object innerBean = createBean(beanName,innerBeanDefinition);
                    setterMethod.invoke(existingBean,innerBean);
                }else {
                // 普通属性 直接使用Setter方法注入
                    //todo 需要一个类型转换适配器，否则任何propertyValue都是String类型的

                    setterMethod.invoke(existingBean,
                            PropertyUtils.propertyValueTypeConversion((String)propertyValue,propertyType));
                }

            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化bean的逻辑方法
     */
    public Object initializeBean(String beanName, Object bean, BeanDefinition<?> beanDefinition) throws InvocationTargetException, IllegalAccessException {

        // 回调Aware接口的方法
        invokeAwareMethods(beanName,bean);
        // 初始化之前执行后置处理器
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean,beanName);
        // 执行自定义初始化方法
        invokeInitMethods(wrappedBean,beanName,beanDefinition);
        // 初始化之后执行后置处理器
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }

    /**
     * 检查指定Bean是否实现了Aware接口，并回调其方法
     */
    private void invokeAwareMethods(String beanName, Object bean) {
        if(bean instanceof Aware){
            if(bean instanceof BeanFactoryAware){
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            if(bean instanceof BeanNameAware){
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }
    }

    /**
     * 在bean初始化之前 执行它绑定的bean后置处理器
     * @param existingBean 已实例化的bean对象
     * @param beanName bean名字
     * @return 处理过后的bean对象
     */
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName){

        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();

        Object result = existingBean;

        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            Object current = beanPostProcessor.postProcessBeforeInitialization(result,beanName);
            if(current == null){
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 在bean初始化之后 执行它绑定的bean后置处理器
     * @param existingBean 已实例化的bean对象
     * @param beanName bean名字
     * @return 处理过后的bean对象
     */
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName){

        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();

        Object result = existingBean;

        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            Object current = beanPostProcessor.postProcessAfterInitialization(result,beanName);
            if(current == null){
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 对指定bean执行自定义的初始化方法
     * @param existingBean 已实例化的bean
     * @param beanName bean名字
     */
    public void invokeInitMethods(Object existingBean, String beanName, BeanDefinition<?> beanDefinition) throws InvocationTargetException, IllegalAccessException {

        boolean isInitializingBean = (existingBean instanceof InitializingBean &&
                !"afterPropertiesSet".equals(beanDefinition.getInitMethodName()));

        if(isInitializingBean){
            ((InitializingBean) existingBean).afterPropertiesSet();
        }

        String initMethodName = beanDefinition.getInitMethodName();
        if(StrUtil.isNotBlank(initMethodName)){
            Method initMethod = ClassUtil.getPublicMethod(existingBean.getClass(), initMethodName);
            if(initMethod == null){
                throw new BeansException("the bean named [" + beanName + "] specify initialization method ["+ initMethodName +"] does not exist");
            }
            initMethod.invoke(existingBean);
        }
    }

    /**
     * 判断bean是否实现了DisposableBean接口 或者指定了destroy()方法，若是则包装成DisposableBeanAdapter注册进注册表中，只有单例Bean才会注册
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition<?> beanDefinition){

        // 只有单例bean才能执行自定义的destroy 所以prototype的bean是没有这个适配器的，无法执行
        if(beanDefinition.isSingleton()){
            // 当bean实现了DisposableBean接口 或者指定了destroy()方法时
            if(bean instanceof DisposableBean || StrUtil.isNotBlank(beanDefinition.getDestroyMethodName())){
                registerDisposableBean(beanName,new DisposableBeanAdapter(beanName,bean,beanDefinition));
            }
        }
    }

    /**
     * 在具体实例化bean之前调用，看该bean能否被实例化为代理对象
     * @param beanName 目标bean名字
     * @param beanDefinition 目标bean定义
     * @return 不为null - 该bean的代理对象
     *         null - 无法生成代理对象
     */
    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition<?> beanDefinition){
        //todo 判断是否有实例化用的BeanPostProcessor

        Class<?> beanClass = beanDefinition.getType();

        // 使用实例化处理器的前置方法 创建代理对象
        Object bean = applyBeanPostProcessorBeforeInstantiation(beanClass,beanName);

        if(bean != null){
            // 执行初始化后的处理器
            return applyBeanPostProcessorsAfterInitialization(bean, beanName);

        }
        return null;
    }

    /**
     * 在实例化之前调实例化处理器的前置方法，生成代理对象
     * @param beanClass bean的类型
     * @param beanName bean的名字
     * @return 不为null - 该bean的代理对象
     *         null - 无法生成代理对象
     */
    protected Object applyBeanPostProcessorBeforeInstantiation(Class<?> beanClass,String beanName){

        Object bean;
        for(BeanPostProcessor beanPostProcessor : getBeanPostProcessors()){
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                bean = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass,beanName);

                if(bean != null){
                    return bean;
                }
            }
        }
        return null;
    }

}
