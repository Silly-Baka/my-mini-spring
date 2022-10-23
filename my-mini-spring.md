# IOC篇

## Bean的生命周期

![https://img-blog.csdnimg.cn/bc9990a8b19544388b6d193b745135a2.png#pic_center](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/bc9990a8b19544388b6d193b745135a2.png)

## 1、简单的IOC容器（beanFactory 仅支持创建bean、获取bean）

**当前Bean的生命周期为：获取bean定义 --> 实例化bean --> 装配属性**

![img](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/bean-definition-and-bean-definition-registry-16659134397361.png)

### 1. 工厂模式实现BeanFactory

`这里采用懒汉式的设计，要先将Bean定义注册进注册表（但不创建Bean对象），在首次获取Bean时才创建`

#### 1.1 BeanDefinition（Bean需要定义）

> **用于定义bean信息的类，包含bean的class类型、构造参数、属性值等信息，每个bean对应一个**

#### 1.2 BeanRegistry（Bean实例的注册表）

> **Bean的实例在创建之后，需要一个表来缓存，方便以后通过beanName和beanType来获取bean实例对象**

#### 1.3 BeanFactory（获取并创建Bean实例）

> **BeanFactory封装了对不同类型的bean的创建逻辑，方便对bean实例的创建**

#### 1.4 BeanDefinitionRegistry（Bean定义的注册表）

> **每个Bean有各自的定义，它的定义也要用一个表存起来，方便通过beanName或beanType来获取**

#### 1.5 AbstractAutowireCapableBeanFactory（自动装配工厂 装配Bean属性）

> **每个Bean都有各自的属性、参数类型、不同的参数值（封装在Bean定义中），不同的名字，我们要有一个装配工厂将Bean的定义封装进其实例对象中。**

#### 1.6 DefaultListableBeanFactory（默认的能够生成完整Bean实例的工厂）

> **BeanDefinition存放在BeanDefinitionRegistry（注册表）中，而BeanFactory只负责Bean实例的创建逻辑，我们要如何获取到Bean的定义呢？**
>
> 将注册表与工厂相结合，这样工厂即能用来注册bean定义，也能够用来生成bean实例对象



### 2、 doCreateBean的策略模式

> Bean的实例化模式可以分为两个：
>
> 1. 使用jdk的反射获取类的构造函数来实例化
> 2. 使用Cglib动态代理来生成实例对象

![instantiation-strategy-16656693115683-16656693131115.png](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/instantiation-strategy-16656693115683-16656693131115-16659138097499.png)



### 3、 为Bean对象注入普通属性（底层使用setter注入）

> **使用PropertyValue类存每一对属性（Key-Value类型） 比用Map存更灵活**
>
> **使用PropertyValues存每一个对象的所有PropertyValue（相当于一个工具类）**
>
> **`使用 属性名 通过反射获取属性的类型然后再获取该属性的Setter方法，再反射调用Setter方法进行注入`**
>
> 
>
> **`但实际上Spring中使用的是PropertyDescritor（底层原理，将Bean类的所有属性类型、名字读取到一个hashmap里，然后就可以通过属性名字来获取某属性的setter、getter等方法）`**



### 4、 为Bean对象注入Bean属性（有循环依赖风险，后面解决）

> 在Spring的xml Bean配置中，是使用**beanName**和**beanType**来配置嵌套bean属性的
>
> ```xml
> <bean id="xxx" class="xxx" >
> 	<property name="xxx" value="xxx"></property>
> 	// 第一种 直接注入（级联）
> 	<bean id="xxxx" class="xxxx">
> 	</bean>
>  // 第二种 使用引用注入
>  <property name="xxx" ref="refXXX"></property>
> </bean>
> <bean id="refXXX" class="xxx">
> </bean>
> ```
>
> 1. 若是**直接注入（级联、内嵌）**，则相当于再定义了一个Bean，用**BeanDefinition**存信息，先创建这个内嵌Bean，再赋值给外层Bean**（内部Bean一般是多例的，所以要以多例模式的方式创建）**
> 2. 若是**引用注入**，则使用一个**BeanReference**类，记录引用的信息，再获取引用的那个Bean，再复制**（要考虑循环依赖的情况）**（**懒汉式，所以不用考虑引用的Bean是否已创建）**



### 5、资源的抽象及获取（读取XML配置文件）

#### 5.1、Resource接口（Spring中所有资源的抽象）

> **前言：**在Spring中，通常是通过XML文件来配置Bean、定义Bean的信息，同时也用来引入第三方框架的配置。那么Spring要如何为XML配置文件的加载提供支持呢？



> Java的标准`java.net.URL`类和各种URL前缀的标准处理程序无法满足所有对`low-level`资源的访问.
>
> 比如:没有标准化的URL实现类用于获取根据`ServletContext`的类路径。并且缺少某些Spring所需要的功能，例如**检测某资源是否存在**等，`java.net.url`只提供了基于标准URL来访问资源的方法**（实际上只针对网络上发布的web资源）**，而**不能基于特定的路径来访问特定的资源（无法针对文件资源、类路径资源）**
>
> Java SE对于资源定位提供了URL这一类，即Uniform Resource Locator，虽然号称统一资源定位，实际上只对通过网络发布的资源提供查找和定位功能；
>
> 实际上，**资源这个词的范围比较广义，资源可以任何形式存在**，如以二进制对象形式存在、以字节流形式存在、以文件形式存在等；而且，资源也可以存在于任何场所，如存在于文件系统、存在于Java应用的Classpath中，甚至存在于URL可以定位的地方。
>
> 其次，该类的功能职责划分不清，资源的查找和表示没有清晰的界限；当前情况是，资源查找后返回的形式多种多样，没有一个统一的抽象。**理想情况下，资源查找完成后，返回给客户端的应该是一个统一的资源抽象接口，**客户端要对资源进行什么样的处理，应该**由资源抽象接口来界定，而不应该成为资源的定位者和查找者同时要关心的事情**。
>
> ​	**`Resource接口`**是**Spring中所有资源的抽象和访问接口**，用于**解决URL接口的不足**
>
> ![img](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/webp.webp?token=ATHSE325TZSKGAKIZFQFW7TDJO7O2)								**Spring中Resource接口的UML关系图**

![resource-16658411461912.png](C:\Users\86176\Desktop\笔记\my-mini-spring.assets\resource-16658411461912-166591384489411.png)

​	`使用策略模式，在获取资源时先获取当前的上下文，再根据上下文的类型来获取相应类型的Resource类对象`

##### 1、 FileSystemResource（文件系统资源）

> `FileSystemResource`指的是**基于文件系统**获取的资源，一般是服务器本地的文件资源

##### 2、 ClassPathResource（类路径资源）

> `ClassPathResource`表示**从类路径获取的资源**，通常使用线程上下文的ClassLoader进行资源加载.
> 我们的Web项目通常编译后，会将class文件存储在`WEB-INF/classes`下，Spring就可以通过`ClassPathResource`来访问这些文件.

##### 3、 UrlResource（Url资源）

> `UrlResource`表示**基于URL路径获取**的资源，一般是一种web资源。



#### 5.2 ResourceLoader接口（Spring中定位资源策略的抽象）

> ![img](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/webp-16658927138937-16658928104089.webp?token=ATHSE34CPXGTKI23G2OLPRDDJO7PI)
>
> ​							 **Spring中ResourceLoader接口的UML关系图**
>
> ![image-20221016120015378](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221016120015378-166589281704010.png?token=ATHSE3YBY3CS27JWJTM2PATDJO7PQ)
>
> 资源是有了，但**如何去查找和定位这些资源，则就是ResourceLoader的职责所在**了。
>
> **`ResourceLoader接口是资源查找定位策略的统一抽象`**，具体的资源查找定位策略则由相应的ResourceLoader实现类给出

#####  1、DefaultResourceLoader

> **DefaultResourceLoader**是**`资源加载策略模式的实现`**，**可根据路径的前缀返回不同类型的Resource**







### 6、更新BeanFactory的继承关系

![img](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/xml-file-define-bean-16659210080936.png)

![img](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/up-743c3654916a300a7d537c5a39ad625aefb.png)

​											**现继承关系**

#### 6.1 HierarchicalBeanFactory（分层的BeanFactory）

> `HierarchicalBeanFactory`为BeanFactory提供了分层的能力，让实现这个接口的BeanFactory之间拥有`层级关系（子级BeanFactory无法获得父级BeanFactory定义的Bean）`，为BeanFactory提供了可以`获取父级BeanFactory`的接口

#### 6.2 ListableBeanFactory（可列表化的BeanFactory）

> 实现了`ListableBeanFactory`接口的BeanFactory可以`一次性列出所有需要的Bean的信息`，提供了**查询Bean**的能力

#### 6.3 ConfigurableBeanFactory（可配置的BeanFactory）

> `ConfigurableBeanFactory`为BeanFactory提供了多个`配置BeanFactory`的接口，允许框架开发者**对BeanFactory进行自定义配置**

#### 6.4 AutowireCapableBeanFactory（可自动装配的BeanFactory）

> `AutowireCapableBeanFactory`为BeanFactory提供了`自动装配Bean属性`的接口

#### 6.5 ConfigurableListableBeanFactory

> `ConfigurableListableBeanFactory`接口**`整合了BeanFactory所需的所有特性`**，同时还**提供了分析和修改Bean的工具，也提供了解决循环依赖的方法（预定义Bean实例）**



### 7、 读取配置文件的Bean定义

> 有了**`Resource和ResourceLoader`**，就可以**读取配置文件**，现在可以开始**实现使用配置文件定义Bean的功能**了
>
> ![在这里插入图片描述](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/74e8782a6ef54d11927d8d9bd6a6fbca.png)
>
> ​					**Spring中读取Bean定义的接口BeanDefinitionReader的继承关系**
>
> 
>
> **在本项目my-mini-spring中，只实现以XML文件的类型配置Bean的定义**
>
> 所以继承关系如下：
>
> ![image-20221016203946158-166619434120210](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221016203946158-166619434120210.png)

#### 7.1 BeanDefinitionReader

> 流程：
>
> 1. 通过配置文件的**locations**，获取配置文件的资源**Resource**
> 2. **读取**每一个资源**Resource **`（需要ResourceLoader）`
> 3. 解析读取到的内容，**转化为BeanDefinition**
> 4. 将BeanDefinition **注册到BeanDefinitionRegistry** 中  `（需要BeanDefinitionRegistry）`

#### 7.2 XMLBeanDefinitionReader（实现读取XML文件中bean的逻辑）

XML示例

```xml
<bean id="xxx" class="xxx" >
	<property name="xxx" value="xxx"></property>
	// 第一种 直接注入（级联）
	<bean id="xxxx" class="xxxx">
	</bean>
	// 第二种 使用引用注入
	<property name="xxx" ref="refXXX"></property>
</bean>
<bean id="refXXX" class="xxx">
</bean>
```



## 2、复杂的IOC容器（从BeanFactory过度到ApplicationContext）

### ==1、BeanFactoryPostProcessor 和 BeanPostProcessor（后置处理器）==

> **BeanFactoryPostProcessor **是Spring提供的**容器拓展机制**，SpringIOC容器允许 **BeanFactoryPostProcessor** 在`**beanDefinition加载完之后，bean被实例化之前**，修改它的beanDefinition（方便拓展）`
>
> 有一个很**常见的应用场景**：
>
> 在XML配置文件的bean定义中添加占位符（引入properties配置文件中的值，**减少bean定义的硬编码**）
>
> ```xml
> <bean id="black" class="com.ipluto.demo.BlackCat">
>  <property name="name">
>      <value>${cat.name}</value>
>  </property>
> </bean>
> ```
>
> 按照一般读取XML bean的流程，最终这个bean中名为name的property的value=${cat.name}
>
> 但**实际上我们需要将占位符替换为properties配置文件中的实际数据**，而在这个场景下`BeanFactoryPostProcessor`便是用于**后置处理填充实际数据**的情况
>
> **BeanFactoryPostProcessor的源码如下：**
>
> ```java
> @FunctionalInterface
> public interface BeanFactoryPostProcessor {
> 
> 	/**
> 	 * Modify the application context's internal bean factory after its standard
> 	 * initialization. All bean definitions will have been loaded, but no beans
> 	 * will have been instantiated yet. This allows for overriding or adding
> 	 * properties even to eager-initializing beans.
> 	 * @param beanFactory the bean factory used by the application context
> 	 * @throws org.springframework.beans.BeansException in case of errors
> 	 */
> 	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
> 
> }
> ```



> **BeanPostProcessor** 也是Spring提供的**容器拓展机制**，该接口允许我们在 **Bean对象实例化以及依赖注入完成后**，但在**显式地调用初始化方法的前后** 添加我们自己的逻辑。
>
> **Spring中的使用场景：**
>
> 1. 用于处理 @Value，@Autowired 等注解
> 2. 主要用于处理Bean内部的注解实现
> 3. 在AOP中，before方法用于寻找所有切面并解析切面，after方法用于生成动态代理类 并且return
>
> **源码如下：**	
>
> ```java
> public interface BeanPostProcessor {
> 	
>     // 显式地初始化之前调用
> 	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;
> 
>     // 显式地初始化之后调用
> 	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
> 
> }
> ```
>
> 



### ==2、 ApplicationContext（代表上下文的抽象接口，提供了获取上下文信息的基本接口）==

`BeanFactory是spring的基础设施，面向spring本身；而ApplicationContext面向spring的使用者，应用场合使用ApplicationContext。`

> **ApplicationContext**是Spring提供的**比BeanFactory更为先进的IoC容器实现**，ApplicationContext除了**拥有BeanFactory支持的所有功能之外**，还进一步扩展了基本容器的功能，**包括BeanFactoryPostProcessor、 BeanPostProcessor以及其他特殊类型bean的自动识别`（在BeanFactory中需要手动注册，在ApplicationContext中实现了自动注册）`**、**容器启动后bean实例的自动初始化`（在BeanFactory采用的是懒汉式加载）`**、**国际化的信息支持**、**容器内事件发布**等。
>
> **`本质上：ApplicationContext只是内置了一个BeanFactory，所以拥有BeanFactory的所有功能。同时ApplicationContext还实现了其他接口 拓展了其他功能`**
>
> **`（比如AbstractApplicationContext这个实现类就实现了刷新上下文的方法 refresh()，里面实现了自动加载所有bean定义、自动注册特殊bean、自动实例化所有bean的逻辑）。`**
>
> 
>
> **继承关系：**
>
> ![在这里插入图片描述](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/e1cc6cf9e3f14c76b7a8898eb29cc7f7.png)



#### 2.1 ConfigurableApplicationContext（可配置的上下文）

> **继承关系：**
>
> ![image-20221020161646845](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221020161646845.png)
>
> **ConfigurableApplicationContext** 提供了`对ApplicationContext进行配置的接口`，同时提供了**`管理ApplicationContext的生命周期（启动以及销毁）的接口`**，是**Spring中许多实现类的SPI接口**



#### 2.2 AbstractApplicationContext

> **继承关系：**
>
> ![image-20210414143029532](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/bfc38eaeff6de47ab1f9be89787a1d7a.png)
>
> **AbstractApplicationContext** 是 **ConfigurableApplicationContext接口**的抽象实现，简单地实现了其中的通用上下文方法。
>
> 使用`模板方法设计模式`，**定义了ApplicationContext的启动步骤(`refresh()方法`)，但是不提供具体每步的实现，由子类提供。**

##### ==refresh（启动上下文）执行流程（ApplicationContext中实例化bean并放入ioc容器的地方）==

![refresh流程图](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/refresh%E6%B5%81%E7%A8%8B%E5%9B%BE.png)

1. 创建上下文（**创建内置的beanFactory**，并**加载所有BeanDefinition**） --> 这一部分委托给子类 `AbstractRefreshableApplicationContext` 来进行实现

2. 在bean实例化之前，**调用BeanFactoryPostProcessor** **（执行修改beanDefinition的逻辑）**

   `疑问1：这里的BeanFactoryPostProcessor是什么时候注册进去的？（在前面一个步骤加载bean定义的时候？）`

3. 在bean实例化之前，**自动注册 BeanPostProcessor** **（注册拓展组件）**

   `疑问2：BeanPostProcessor算是一个特殊bean，那么它们的生命周期与普通bean相同吗？（出生比普通bean早一点，因为要负责其他bean实例化的前后置处理，但其他周期都一样）`

4. 自动注册其他拓展组件....

5. **实例化所有的bean --> 按照定义装配属性 --> 前置处理 --> 初始化 --> 后置处理**

6. 上下文启动完毕

##### 当前bean的生命周期

![img](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/application-context-life-cycle.png)





#### 2.3 AbstractRefreshableApplicationContext（实现了refreshBeanFactory的实际逻辑）

##### **refreshBeanFactory执行流程**

1. 判断内置的BeanFactory是否为空
2. 若不为空，则**清空内置BeanFactory的所有bean实例**，并**关闭内置的BeanFactory**
3. 创建新的BeanFactory（其父级BeanFactory为当前的父级上下文的内置BeanFactory）
4. **加载所有beanDefinition**进新的BeanFactory（`loadBeanDefinitions(beanFactory) 交给子类AbstractXmlApplicationContext 来进行实现`）



#### 2.4 AbstractXmlApplicationContext（实现了loadBeanDefinitions的实际逻辑）

![image-20221020204317054](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221020204317054.png)

**FileSystemXMLApplicationContext（提供给用户使用的实现类）**：按照文件系统的路径来读取XML配置文件

**ClassPathXMLApplicationContext（提供给用户使用的实现类）**：按照类的根路径来读取XML配置文件

 



### 3、bean的初始化以及销毁

> 在前文中提到了，bean在实例化、自动装配属性后还需要进行**初始化**，`1、那么用户要如何定义初始化方法，并且让Spring执行呢？`
>
> 同时，在前文bean的生命周期中，缺少了**bean的销毁**这一过程，`2、简单地销毁bean方法会由spring提供，而用户需要提供自定义的销毁方法。`

#### **1、初始化**

##### 1.1 Spring中初始化bean的几种方式

**Spring中初始化bean逻辑的源码**

```java
protected void invokeInitMethods(String beanName, Object bean, @Nullable RootBeanDefinition mbd)
      throws Throwable {

    // 检查当前bean是否实现了InitializingBean接口
   boolean isInitializingBean = (bean instanceof InitializingBean);
    
    // 如果当前bean实现了InitializingBean接口，并且指定的init-method不为afterPropertiesSet
   if (isInitializingBean && (mbd == null || !mbd.hasAnyExternallyManagedInitMethod("afterPropertiesSet"))) {
      if (logger.isTraceEnabled()) {
         logger.trace("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
      }
      if (System.getSecurityManager() != null) {
         try {
            AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
               ((InitializingBean) bean).afterPropertiesSet();
               return null;
            }, getAccessControlContext());
         }
         catch (PrivilegedActionException pae) {
            throw pae.getException();
         }
      }
      else {
          // 执行bean类实现的 InitializingBean接口的afterPropertiesSet()方法 （自定义初始化方法）
         ((InitializingBean) bean).afterPropertiesSet();
      }
   }

   if (mbd != null && bean.getClass() != NullBean.class) {
      String initMethodName = mbd.getInitMethodName();
       // 如果当前bean没有实现InitializingBean接口，但bean定义中指定了init-method，同时也没有外部的初始化方法与init-method的名字相同
      if (StringUtils.hasLength(initMethodName) &&
            !(isInitializingBean && "afterPropertiesSet".equals(initMethodName)) &&
            !mbd.hasAnyExternallyManagedInitMethod(initMethodName)) {
          // 那么就执行bean定义中指定的初始化方法
         invokeCustomInitMethod(beanName, bean, mbd);
      }
   }
}
```

> 通过看源码可以知道，spring初始化bean有以下三种方法（**不可同时执行，以实现InitializingBean接口优先**）：
>
> - 实现**InitializingBean接口**，并**实现其afterPropertiesSet（）**方法
> - 在**bean定义**（xml配置文件）中指定初始化方法，属性为：**init-method**
> - （此源码中无法看出，源码中说的是外部的初始化方法）在方法上**加注解PostConstruct和PreDestroy**
>
> **这里只实现前两种，第三种后面再实现（因为尚未知道原理）**



##### 1.2 实现初始化bean的前两种方式

- BeanDefinition中需要增加属性**表示初始化方法的名字**

- ```java
  /**
   * 对指定bean执行自定义的初始化方法
   * @param existingBean 已实例化的bean
   * @param beanName bean名字
   */
  public <T> void invokeInitMethods(T existingBean, String beanName, BeanDefinition<T> beanDefinition) throws InvocationTargetException, IllegalAccessException {
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
  ```





#### 2、销毁

##### 2.1 Spring中销毁bean的几种方式

**Spring中销毁bean的源码（和初始化的源码相似）**

**（这里Spring采用了适配器模式，DisposableBeanAdapter适配了`DisposableBean接口`（实现destroy()方法），和`Runnable接口`（`使其可以作为异步线程任务执行destroy()方法`）**

```java
// BeanFactory中销毁bean调用的方法
protected void destroyBean(String beanName, Object bean, RootBeanDefinition mbd) {
    // 创建一次性的bean适配器 --> 用于处理bean的销毁逻辑
		new DisposableBeanAdapter(
				bean, beanName, mbd, getBeanPostProcessorCache().destructionAware, getAccessControlContext()).destroy();
}

public DisposableBeanAdapter(Object bean, String beanName, RootBeanDefinition beanDefinition,
List<DestructionAwareBeanPostProcessor> postProcessors, @Nullable AccessControlContext acc) {

    Assert.notNull(bean, "Disposable bean must not be null");
    this.bean = bean;
    this.beanName = beanName;
    this.nonPublicAccessAllowed = beanDefinition.isNonPublicAccessAllowed();
    
    // 判断传入的bean是否实现了DisposableBean接口、bean定义中有无外部的destroy方法
    this.invokeDisposableBean = (bean instanceof DisposableBean &&
                                 !beanDefinition.hasAnyExternallyManagedDestroyMethod(DESTROY_METHOD_NAME));

    // 试着使用bean对象（其实现的接口）和bean定义来推断destroy方法的名字 
    // --> 其实就是找xml的bean定义中有无指定destroy-method
    String destroyMethodName = inferDestroyMethodIfNecessary(bean, beanDefinition);
    
    // 如果有指定destroy-method 并且没有实现DisposableBean接口、也没有外部的destroy方法
    if (destroyMethodName != null &&
        !(this.invokeDisposableBean && DESTROY_METHOD_NAME.equals(destroyMethodName)) &&
        !beanDefinition.hasAnyExternallyManagedDestroyMethod(destroyMethodName)) {

        // 有无实现AutoCloseable接口，并且看看destroyMethodName是否与自动关闭的方法名字相同
        this.invokeAutoCloseable = (bean instanceof AutoCloseable && CLOSE_METHOD_NAME.equals(destroyMethodName));
        .............
        .............
            
        // 最后会将此指定的destroy-method 设置为 destroy()的实际逻辑
            this.destroyMethod = destroyMethod;
        }
    }

	// 加载beanPostProcessors
    this.beanPostProcessors  = filterPostProcessors(postProcessors, bean);
    this.acc = acc;
}


// 执行destroy的实际逻辑
public void destroy() {
    
   	// 如果用于Destroy的Processors不为空（即外部的destroy方法），则执行
    if (!CollectionUtils.isEmpty(this.beanPostProcessors)) {
        for (DestructionAwareBeanPostProcessor processor : this.beanPostProcessors) {
            processor.postProcessBeforeDestruction(this.bean, this.beanName);
        }
    }
    // 如果实现了DisposableBean接口 并且bean定义指定的destroy方法不叫destroy
    if (this.invokeDisposableBean) {
        if (logger.isTraceEnabled()) {
            logger.trace("Invoking destroy() on bean with name '" + this.beanName + "'");
        }
        try {
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
                    ((DisposableBean) this.bean).destroy();
                    return null;
                }, this.acc);
            }
            else {
                // 执行已经实现的DisposableBean接口的destroy方法
                ((DisposableBean) this.bean).destroy();
            }
        }
        catch (Throwable ex) {
            String msg = "Invocation of destroy method failed on bean with name '" + this.beanName + "'";
            if (logger.isDebugEnabled()) {
                logger.warn(msg, ex);
            }
            else {
                logger.warn(msg + ": " + ex);
            }
        }
    }

    // 实现了AutoCloseable接口 并且自动关闭的方法名为destroy
    if (this.invokeAutoCloseable) {
        if (logger.isTraceEnabled()) {
            logger.trace("Invoking close() on bean with name '" + this.beanName + "'");
        }
        try {
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
                    ((AutoCloseable) this.bean).close();
                    return null;
                }, this.acc);
            }
            else {
                ((AutoCloseable) this.bean).close();
            }
        }
        catch (Throwable ex) {
            String msg = "Invocation of close method failed on bean with name '" + this.beanName + "'";
            if (logger.isDebugEnabled()) {
                logger.warn(msg, ex);
            }
            else {
                logger.warn(msg + ": " + ex);
            }
        }
    }
    // 否则执行在bean定义中指定的destroy-method
    else if (this.destroyMethod != null) {
        invokeCustomDestroyMethod(this.destroyMethod);
    }
    // 否则执行在bean定义中指定的destroy-method
    else if (this.destroyMethodName != null) {
        Method destroyMethod = determineDestroyMethod(this.destroyMethodName);
        if (destroyMethod != null) {
            invokeCustomDestroyMethod(ClassUtils.getInterfaceMethodIfPossible(destroyMethod, this.bean.getClass()));
        }
    }
}
```

> 通过看源码可以知道，**在Spring中销毁bean的方式有四种:**
>
> - 实现了**DisposableBean接口**，并且**实现了destroy()**方法
> - 在xml配置文件中的**bean定义**，指定了**destroy-method**
> - 实现了**AutoCloseable接口**，并且**指定了自动关闭执行的方法，且该方法名为destroy**()
> - 定义**DestructionAwareBeanPostProcessor**，用于在外部控制（拓展）bean的destroy
>
> **在这里也只实现前面两种，第三种后续再实现**

##### 

##### 2.2 实现destroy bean的前两种方式

在Spring中，`BeanFactory将destroy bean的逻辑全权交给了DisposableBeanAdapter`，这里我也会采用这样的实现

> 在这里需要先思考`一个问题`：`destroyBean的调用时机是什么，由谁来负责调用，由又谁来负责执行实际逻辑呢？`
>
> **调用时机**
>
> 1. `关闭或者刷新IOC容器时`，需要删除所有的bean
> 2. Bean实例`被覆盖或者过期`时
>
> **关于DestroyBean的实际逻辑**
>
> IOC容器是`可拓展`的容器（实现了ConfigurableBeanFactory接口） --> 委托`DisposableAdapter`来处理实际的逻辑`（即会判断是否有 自定义的destroy-method 或 使用注解添加的destroy （BeanProcessor））`
>
> **实际上Spring中的destroyBean有两套逻辑**
>
> 1. `AbstractAutoCapableBeanFactory`中的 **destroyBean**(Object existingBean)`（不用这个方法，这个是AutoCapableBeanFactory提供的接口  --> 几乎不用这个方法）`
>
>    传入一个Bean对象，然后`包装成DisposableBeanAdapter`再处理destroy方法
>
>    ```java
>    @Override
>    public void destroyBean(Object existingBean) {
>        // 委托给DisposableBeanAdapter进行处理
>       new DisposableBeanAdapter(
>             existingBean, getBeanPostProcessorCache().destructionAware, getAccessControlContext()).destroy();
>    }
>    ```
>
> 2. `DefaultSingletonRegistry`中会有一个特殊的注册表，保存了**所有实现了DisposableBean接口或者bean定义中指定了destroy-method的单例bean包装而成的DisposableAdapter对象（`在Bean注册时注入`，单例bean才会有，相当于缓存了对所有单例bean的destroy判断）**
>
>    `DefaultSingletonRegistry`中的`destroySingleton方法`和`destroySingletons方法`，是**`destroyBean`**的模板逻辑`（ConfigurableBeanFactory中定义的接口 --> Spring常用这个）`
>
>    ```java
>    // 销毁指定的单例bean
>    public void destroySingleton(String beanName) {
>           
>        // 删除缓存中的bean
>        removeSingleton(beanName);
>       
>        DisposableBean disposableBean;
>           
>        // 从特殊的注册表中取出该bean对应的DisposableAdapter
>        synchronized (this.disposableBeans) {
>            disposableBean = (DisposableBean) this.disposableBeans.remove(beanName);
>        }
>           
>       	// 实际destroy逻辑
>        destroyBean(beanName, disposableBean);
>    }
>       
>    // 销毁所有的单例bean
>    public void destroySingletons() {
>        if (logger.isTraceEnabled()) {
>            logger.trace("Destroying singletons in " + this);
>        }
>        synchronized (this.singletonObjects) {
>            this.singletonsCurrentlyInDestruction = true;
>        }
>       
>        String[] disposableBeanNames;
>        synchronized (this.disposableBeans) {
>            disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
>        }
>        // 倒序取出所有的DisposableAdapter
>        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
>            // 再一个个处理删除单个的逻辑
>            destroySingleton(disposableBeanNames[i]);
>        }
>       
>      	// 清除所有的缓存
>        this.containedBeanMap.clear();
>        this.dependentBeanMap.clear();
>        this.dependenciesForBeanMap.clear();
>       
>        clearSingletonCache();
>    }
>       
>    // 销毁一个bean的实际逻辑
>    protected void destroyBean(String beanName, @Nullable DisposableBean bean) {
>       
>        Set<String> dependencies;
>        synchronized (this.dependentBeanMap) {
>            dependencies = this.dependentBeanMap.remove(beanName);
>        }
>        if (dependencies != null) {
>            if (logger.isTraceEnabled()) {
>                logger.trace("Retrieved dependent beans for bean '" + beanName + "': " + dependencies);
>            }
>            // 销毁该bean所有依赖的bean
>            for (String dependentBeanName : dependencies) {
>                destroySingleton(dependentBeanName);
>            }
>        }
>       
>        if (bean != null) {
>            try {
>                // 真正执行当前bean的自定义destroy方法
>                bean.destroy();
>            }
>            catch (Throwable ex) {
>                if (logger.isWarnEnabled()) {
>                    logger.warn("Destruction of bean with name '" + beanName + "' threw an exception", ex);
>                }
>            }
>        }
>        ....................
>    ```

#### 3、修改XmlBeanDefinitionReader的逻辑

Spring中的xml格式

```XML
<bean class="xxx" init-method="initMethod", destroy-method="destroyMethod"></bean>
```

所以当前的**XmlBeanDefinitionReader**需要增加对`init-method`、`destroy-method`等属性的判断

 

#### 4、当前bean的生命周期



![img](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/init-and-destroy-method.png)



### 4、Aware接口

> Aware-意识到、知道的、察觉到的，`Aware接口`是Spring中`用于标识的接口`，实现了该接口的类可以获得`感知Spring内部容器`的能力。
>
> 比如说`BeanFactoryAware`和`ApplicationContextAware`接口，实现了这两个接口的Bean可以分别`感知到它所属的BeanFactory和ApplicationContext`（即获得这些对象），进而使用它们所拥有的功能。
>
> 有时候你的Bean需要**通过Spring来获取某些东西**（**以Bean为主动**，而不是我们程序员），那就需要实现这个接口

#### 4.1 BeanFactoryAware（使Bean能感知到其所属的BeanFactory）

这里的回调应该发生在`Bean实例化之后（要先有对象才能回调其函数）`，`Bean初始化之前（因为初始化的逻辑中可能会使用到）`

#### 4.2 ApplicationContextAware（使Bean能感知到其所属的ApplicationContext）

这里的回调也应该发生在`Bean实例化之后（要先有对象才能回调其函数）`，`Bean初始化之前（因为初始化的逻辑中可能会使用到）`，但由于创建是在内置的BeanFactory中进行的，而`BeanFactory无法感知到其上下文`（也可以设计成可感知），而ApplicationContext`在Bean实例化之前会自动注册BeanPostProcessor`，所以可以在注册BeanPostProcessor时将上下文记录。在**调用BeanPostProcessor时再回调注入到Bean中**

#### 4.3 当前Bean的生命周期

![img](https://github.com/DerekYRC/mini-spring/raw/main/assets/aware-interface.png)





### 5、FactoryBean（工厂bean，是一个bean也是自己的工厂）

> `FactoryBean`是一种特殊的Bean，它的`生命周期由IOC容器进行管理`，但它`内部Bean实例的生命周期则由自己来管理`，所以IOC容器`不会自动调用` FactoryBean所管理的Bean的 `拓展方法`（比如DisposableBean.destroy()）

> **FactoryBean由`IOC容器直接管理`，所以我们通过BeanName就可以获得`FactoryBean对象`，但IOC容器如何知道你要获取的就是FactoryBean还是它的bean实例呢？**
>
> Spring在`BeanFactory接口`中提供了这样的一个静态属性：`FACTORY_BEAN_PREFIX` 放在beanName中，用于标识你实际上要获取的是FactoryBean还是它里面的bean实例。
>
> ![image-20221023162844987](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221023162844987.png)
>
> **`但设计这个功能太麻烦，还需要弄一个bean别名注册表，所以这里假设不需要获取FactoryBean，只需要获取内部的bean`**



### 6、Spring的事件机制（ApplicationContext提供的事件发布与监听功能）

> `Spring事件机制类似于MQ`，流程都是 **发布者发布事件 --> 广播者保存事件并转发给监听者 --> 监听者监听事件 --> 接收到事件后处理事件**，体现了`发布--订阅`的思想，可以在单机环境中实现一定程度的代码解耦
>
> **但Spring的事件机制很少用，因为在分布式流行的现在 MQ一家独大，它的功能特性就显得很蹩脚，但也得了解一下它底层所使用的设计模式 --> `观察者模式` 。**
>
> Spring 事件机制适合`单体应用`，同一个 JVM 且并发不大的情况，如果是`分布式应用，推荐使用MQ`。
>
> Spring 事件监听机制和 MQ 有相似的地方，也有不同的地方。`MQ` 允许跨 JVM，因为它本身是`独立于项目之外`的，切提供了消息持久化的特性，而 `Spring 事件机制`哪怕使用了异步，`本质是还是在本地JVM上进行方法调用`，本地JVM宕机了就没了。



> `思考一个问题，事件的发布与处理，需要哪些角色呢？`
>
> 1. 事件对象本身
> 2. 事件的发布者（用于发布事件）
> 3. 事件的广播者（用于将事件转发给监听者）
> 4. 事件的监听者（用于监听并处理事件）

#### 1、ApplicationEvent（事件实体类）

顾名思义，一个事件实体对象 就`表示一个事件的发生`



#### 2、ApplicationEventPublisher（事件发布者接口）

> **提供的接口：**
>
> 1. 发布事件

#### 3、ApplicationEventMulticaster（事件广播者接口）

> **提供的接口：**
>
> 1. 将监听者和某种类型事件绑定
> 2. 将事件发送给绑定它的类型的监听者们

#### 4、ApplicationListener（事件监听者接口）

> **提供的接口：**
>
> 1. 处理事件
