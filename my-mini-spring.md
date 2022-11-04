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



> **BeanPostProcessor** 也是Spring提供的**容器拓展机制**，该接口允许我们在 **Bean对象实例化以及依赖注入完成后**，但在**显式地调用初始化方法的前后** 添加我们自己的逻辑。**`（和Netty中的handler相似，对bean依次执行BeanPostProcessor链上的每一个处理器 初始化前执行相当于入站处理器，初始化后执行相当于出站处理器 ！！！ 职责链模式 ！！！）`**
>
> ​	**`BeanPostProcessor应该拥有各自执行的优先级（因为是一条处理链）`**
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
>
> **`特殊的BeanPostProcessor  `**
>
> **`--- InstantiationAwareBeanPostProcessor（实例化前后用的后置处理器，一般用于AOP生成代理Bean）`**
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

4. 自动注册**其他拓展组件（如国际信息组件、事件转播器组件...）**....

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

这里的回调也应该发生在`Bean实例化之后（要先有对象才能回调其函数）`，`Bean初始化之前（因为初始化的逻辑中可能会使用到）`，但由于创建是在内置的BeanFactory中进行的，而`BeanFactory无法感知到其上下文`（也可以设计成可感知），而ApplicationContext`在Bean实例化之前会自动注册BeanPostProcessor`，所以应当在注册BeanPostProcessor时将上下文记录。在**调用BeanPostProcessor时再回调注入到Bean中**

#### 4.3 当前Bean的生命周期

![img](https://github.com/DerekYRC/mini-spring/raw/main/assets/aware-interface.png)



#### 4.4 总结

1. `实现了Aware接口的Bean`都会在其`实例化后、初始化前`被注入相应的aware对象

2. `BeanFactory有关的Aware接口`都会在实例化Bean对象之后的 `invokeAwareMethods(String beanName，Object bean)中调用`

3. 而`ApplicationContextAware等和上下文相关的Aware接口`会在执行BeanPostProcessor的`前置处理方法`时，即在执行`applyBeanPostProcessorsBeforeInitialization(String beanName，Object bean)`时调用，

   **（由BeanPostProcessor放入，在注册特定的BeanPostProcessor时会放入）**



### 5、Bean的Scope作用域（实现prototype）

> 在前面的章节中，我们的Bean只实现了`单例（Singleton）模式`，这里我们将实现`多例（prototype）模式`
>
> 在Spring中，Bean的Scope拥有五种：
>
> 1. `singleton（单例，整个应用上下文只存在一个实例  --> IOC容器中会有一个单例对象表存所有bean的单例对象）`
> 2. `prototype（多例，每次获取bean都会创建一个新的实例  --> IOC容器中不会保存该实例，也不会处理它的自定义destroy方法 多例bean的生命周期由用户自己决定）`
> 3. request（一个http请求中存在一个实例）
> 4. session（一个session存在一个实例）
> 5. global-session（全部session共享这个实例，即整个WebApplicationContext）

#### 实现了prototype后的bean生命周期（多例bean不会执行自定义destroy方法）

> **`多例bean不会执行自定义的的destroy方法 原因分析`**
>
> `原因`：在`AbstractAutowireCapableBeanFactory#doCreateBean()的方法中`，在实例化bean并且初始化之后，Spring会对该bean实例执行一个`registerDisposableBeanIfNecessary()方法`，该方法用于检查该bean实例是否有自定义的destroy方法，如果有 并且`该bean不为多例` 则包装为一个`DisposableBeanAdapter`注入IOC容器的`DisposableBean注册表。` （在销毁该bean的时候会从注册表中取出`对应的DisposableBeanAdapter`，并执行destroy方法)
>
> `源码： AbstractAutowireCapableBeanFactory#registerDisposableBeanIfNecessary（）`
>
> ```java
> protected void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd) {
>    AccessControlContext acc = (System.getSecurityManager() != null ? getAccessControlContext() : null);
>     // 不是多例bean 并且 拥有自定义的destroy方法
>    if (!mbd.isPrototype() && requiresDestruction(bean, mbd)) {
>        
>        // 如果是单例bean
>       if (mbd.isSingleton()) {
>          // 将其包装为DisposableBeanAdapter并且注册进特殊的注册表中
>          registerDisposableBean(beanName, new DisposableBeanAdapter(
>                bean, beanName, mbd, getBeanPostProcessorCache().destructionAware, acc));
>       }
>       else {
>          // 如果是其他类型scope的bean（除了prototype）
>          Scope scope = this.scopes.get(mbd.getScope());
>          if (scope == null) {
>             throw new IllegalStateException("No Scope registered for scope name '" + mbd.getScope() + "'");
>          }
>           // 将其包装为DisposableBeanAdapter并且注册进特殊的注册表中
>          scope.registerDestructionCallback(beanName, new DisposableBeanAdapter(
>                bean, beanName, mbd, getBeanPostProcessorCache().destructionAware, acc));
>       }
>    }
> }
> ```
>
> 

![img](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/prototype-bean.png)



### 6、FactoryBean（工厂bean，是一个bean也是自己的工厂）

> `FactoryBean`是一种特殊的Bean，它的`生命周期由IOC容器进行管理`，但它`内部Bean实例的生命周期则由自己来管理`，所以IOC容器`不会自动调用` FactoryBean所管理的Bean的 `拓展方法`（比如DisposableBean.destroy()）

> **FactoryBean由`IOC容器直接管理`，所以我们通过BeanName就可以获得`FactoryBean对象`，但IOC容器如何知道你要获取的就是FactoryBean还是它的bean实例呢？**
>
> Spring在`BeanFactory接口`中提供了这样的一个静态属性：`FACTORY_BEAN_PREFIX` 放在beanName中，用于标识你实际上要获取的是FactoryBean还是它里面的bean实例。
>
> ![image-20221023162844987](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221023162844987.png)
>
> **`但设计这个功能太麻烦，还需要弄一个bean别名注册表，所以这里假设不需要获取FactoryBean，只需要获取内部的bean`**



### 7、Spring的事件机制（ApplicationContext提供的事件发布与事件监听处理功能）

> `Spring事件机制类似于MQ`，流程都是 **发布者发布事件 --> 广播者保存事件并转发给监听者 --> 监听者监听事件 --> 接收到事件后处理事件**，体现了`发布--订阅`的思想，可以在单机环境中实现一定程度的代码解耦
>
> **但Spring的事件机制很少用，因为在分布式流行的现在 MQ一家独大，它的功能特性就显得很蹩脚，但也得了解一下它底层所使用的设计模式 --> `观察者模式` 。**
>
> Spring 事件机制适合`单体应用`，同一个 JVM 且并发不大的情况，如果是`分布式应用，推荐使用MQ`。
>
> Spring 事件监听机制和 MQ 有相似的地方，也有不同的地方。`MQ` 允许跨 JVM，因为它本身是`独立于项目之外`的，切提供了消息持久化的特性，而 `Spring 事件机制`哪怕使用了异步，`本质是还是在本地JVM上进行方法调用`，本地JVM宕机了就没了。



> `思考一个问题，事件的发布与处理，需要哪些角色呢？`
>
> 1. 事件对象本身 --> 用户自己实现
> 2. 事件的发布者（用于发布事件）  --> 接口外放给用户使用
> 3. 事件的广播者（用于将事件转发给监听者） --> spring内部调用
> 4. 事件的监听者（用于监听并处理事件） --> 注册接口外放给用户使用，用户提供，spring内部调用

#### 1、ApplicationEvent（事件实体类）

> 顾名思义，`一个事件实体对象的产生` 就`表示一个事件的发生`，一个`事件实体类`就代表`一种类型的事件`
>

<img src="https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/ApplicationEvent%E7%B1%BB%E5%9B%BE.png" alt="ApplicationEvent类图" style="zoom:50%;" />

#### 2、ApplicationEventPublisher（事件发布者接口 由ApplicationContext实现）

> **提供的接口：**
>
> 1. 发布事件

#### 3、ApplicationEventMulticaster（事件广播者接口）

> **提供的接口：**
>
> 1. 将监听者（可以是一个**listener对象**也可以是一个**beanName** **从BeanFactory中获取listenerBean**）和某种类型事件绑定
> 2. 将事件发送给绑定它的类型的监听者们
>
> ```java
> public interface ApplicationEventMulticaster {
> 
>     /**
>      * 添加绑定listener
>      */
>     void addApplicationListener(ApplicationListener<?> applicationListener);
> 
>     /**
>      * 卸载指定listener
>      */
>     void removeApplicationListener(ApplicationListener<?> applicationListener);
> 
>     /**
>      * 添加applicationListenerBean，根据beanName从容器中取出再添加
>      */
>     void addApplicationListenerBean(String beanName);
> 
>     /**
>      * 根据beanName卸载指定listenerBean
>      */
>     void removeApplicationListenerBean(String beanName);
> 
>     /**
>      * 广播指定的事件
>      */
>     void multicastEvent(ApplicationEvent event);
> }
> ```
>
> 
>
> **具体类图**
>
> <img src="https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221024101416907.png" alt="image-20221024101416907" style="zoom:50%;" />

##### 3.1 AbstractApplicationEventMulticaster（抽象监听者，实现了注册监听器和判断是否监听的通用方法）

由于有一个接口需要根据beanName注册监听者bean，所以需要感知BeanFactory的功能，所以实现**BeanFactoryAware**接口

```java
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    /**
     * 用于保存绑定的监听者
     */
    protected final Set<ApplicationListener<?>> applicationListeners = new HashSet<>(16);

    private ConfigurableBeanFactory beanFactory;

    @Override
    public void addApplicationListener(ApplicationListener<?> applicationListener) {
        applicationListeners.add(applicationListener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> applicationListener) {
        applicationListeners.remove(applicationListener);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    /**
     * 检查目标监听器是否对指定事件感兴趣
     * @param listener 要检查的监听器
     * @param event 指定事件
     */
    //todo 这里的判断逻辑可以优化 可以先将event解析成type 再使用type去检查
    protected boolean supportsEvent(ApplicationListener<?> listener, ApplicationEvent event){
        // 检查当前listener监听的事件类型（检查泛型的类型）
        Type[] genericInterfaces = listener.getClass().getGenericInterfaces();

        Class<? extends ApplicationEvent> eventType = event.getClass();

        for (Type type : genericInterfaces) {
            // 如果是参数型类型
            if(type instanceof ParameterizedType){
                // 获得实际代表的类型列表
                Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
                for (Type actualType : actualTypes) {
                    String actualTypeName = actualType.getTypeName();
                    try {
                        if(Class.forName(actualTypeName).isAssignableFrom(eventType)){
                            return true;
                        }
                    } catch (ClassNotFoundException e) {
                        throw new BeansException("wrong event type name:"+actualTypeName,e);
                    }
                }
            }
        }
        return false;
    }
}
```

##### 3.2 SimpleApplicationEventMulticaster（简单的广播器，实现了广播的基本逻辑）

> **在这里要注意一个问题**
>
> Spring中的事件机制可以是**`同步的`**也可以是**`异步的`**（**`默认是同步`**的，需要设置**自定义的异步线程池**才是异步）

```java
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{

    /**
     * 异步处理任务的自定义线程池
     */
    private Executor taskExecutor;

    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener<?> listener : applicationListeners) {
            //todo 这里的判断逻辑可以优化 可以先将event解析成type 再使用type去检查
            if(supportsEvent(listener,event)){
                // 查看是否有自定义的线程池
                Executor executor = getTaskExecutor();

                // 有则异步处理事件
                if(executor != null){
                    executor.execute(()->{
                        invokeListener(listener,event);
                    });

                }else {
                // 无则同步处理事件
                    invokeListener(listener,event);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void invokeListener(ApplicationListener listener, ApplicationEvent event){
        listener.onApplicationEvent(event);
    }

    protected void setTaskExecutor(Executor executor){
        this.taskExecutor = executor;
    }

    protected Executor getTaskExecutor(){
        return this.taskExecutor;
    }
}
```

#### 4、ApplicationListener（事件监听者接口）

> **提供的接口：**
>
> 1. 处理事件**（只有一个接口，做成函数式接口）**
>
> ```java
> @FunctionalInterface
> public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
> 
>     /**
>      * 处理指定类型事件的函数式接口
>      */
>     void onApplicationEvent(E event);
> 
> }
> ```



#### 5、更新ApplicationContext接口及其抽象实现AbstractApplicationContext

> 1、ApplicationContext需要提供`发布事件`的功能**（面向用户）** --> 实现 `ApplicationEventPublish` 接口
>
> 2、ApplicationContext需要内置一个 `ApplicationEventMulticaster对象（内部创建）`，方便对事件进行传播**（面向Spring内部使用）**
>
> 3、监听器ApplicationListener要`如何注册进ApplicationContext，如何绑定ApplicationEventMulticaster？`
>
> 除了`Spring内置事件所需的ApplicationListener以外`，其余的都是`用户按需提供`的，而ApplicationListener会作为`特殊Bean`存留在IOC容器中。
>
> 所以`用户提供的ApplicationListener`会随着初始化上下文中的加载Bean定义、预实例化所有Bean对象 从而注册进上下文当中。但`绑定到ApplicationEventMulticaster`则需要在Bean实例化后通过`BeanPostProcessor`来处理
>
> （Spring内部提供了一个叫`ApplicationListenerDetector`的BeanPostProcessor来处理这件事）



**简单的ApplicationListenerDetector（只在bean实例化后处理）**

```java
public class ApplicationListenerDetector extends ApplicationContextAwareProcessor implements BeanPostProcessor {

    public ApplicationListenerDetector(ConfigurableApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public <T> T postProcessAfterInitialization(T bean, String beanName) {
        // 检查是否是监听器
        if(bean instanceof ApplicationListener){
            //若是 则注册进上下文中
            applicationContext.addApplicationListener((ApplicationListener<?>) bean);
        }
        return bean;
    }
}
```

在上下文刷新时，bean实例化之前 将**ApplicationListenerDetector**注入上下文

```java
public void refresh() {

    // 获取BeanFactory
    ConfigurableListableBeanFactory beanFactory = obtainBeanFactory();

    // 为新的内置BeanFactory填充特殊内置属性
    prepareBeanFactory(beanFactory);

    // 在实例化bean之前调用beanFactoryPostProcessor 看是否需要修改beanDefinition
    invokeBeanFactoryPostProcessors(beanFactory);

    // 注册BeanPostProcessors进当前内置的BeanFactory
    registerBeanPostProcessors(beanFactory);

    //---- 注册其他上下文应提供的拓展组件 ---

    //注册事件广播器
    initApplicationEventMulticaster();

    // ----------------------

    // 预实例化所有的bean
    beanFactory.preInitiateSingletons();
}

protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));
}
```



**至此，Spring的事件机制基本雏形就做好了，但现在支持注入事件监听器的方式只有**

1. 通过ApplicationContext接口提供的 **addApplicationListener** `手动注入`
2. 通过XML配置文件配置**addApplicationListenerBean**，在启动上下文时 `自动注入`
3. **`后面再开发 注解驱动配置（自动注入）`**





# AOP篇

## 1、前言

> **在学习AOP篇之前，我们先要了解几个概念**
>
> **`1、什么是AOP`**
>
> `AOP`(**Aspect Oriented Programing**)是指 `面向切面编程`，**从逻辑层面上** 就是在一个类、一个方法、一段代码`中间切开一个面，再把切面放入这个缺口中，而切面中则封装了我们提供的拓展逻辑`，**从实现层面**上是通过`预编译方式（静态代理）或者是运行期期间动态代理`实现`功能扩展`，而不用修改源代码。通过AOP技术，可以实现一种通用的逻辑解耦，解决一些系统层面的问题，如**日志，事务，权限**等，从而实现高可用的可重用性和可维护性。
>
> **`2、AOP和OOP的区别`**
>
> `OOP（面向对象编程）`通过引入封装、继承和多态性等概念来建立一种对象层次结构，用以模拟公共行为的一个集合。当我们需要`为分散的对象引入公共行为`的时候，OOP则显得无能为力。也就是说，`OOP允许你定义从上到下的关系`，但`并不适合定义从左到右的关系`。例如日志功能。日志代码往往水平地散布在所有对象层次中，而与它所散布到的对象的核心功能毫无关系。对于其他类型的代码，如安全性、异常处理和透明的持续性也是如此。**`这种散布在各处的无关的代码被称为横切（cross-cutting）代码，在OOP设计中，它导致了大量代码的重复，而不利于各个模块的复用。`**
>
> 而`AOP`的出现就是为了解决这样的问题，AOP恰好就利用了`“横切”`的技术
>
> **AOP代表的是一个横向的关系，如果说“对象”是一个空心的圆柱体，其中封装的是对象的属性和行为；那么面向切面编程的方法，就仿佛一把利刃，将这些空心圆柱体剖开，以获得其内部的消息 并往里面加盐加料。而`剖开的面以及我们往里面添加的料`，也就是所谓的`“切面”`了。然后它又将这些剖开的切面复原，不留痕迹。** **`（在不修改代码的基础上增强代码功能，符合开闭原则）`**
>
> **`3、AOP基本术语`**
>
> - **Advice（通知）**：表示我们对原有功能`需要添加的拓展功能逻辑（增强）`，通知`按照触发范围`又分为前置、后置、环绕、异常、返回值等类型
> - **JoinPoint（连接点、织入点）**：用于`连接通知的具体的点`，可以是**程序执行过程中的任意位置**，比如说可以在方法执行前、方法执行后、抛出异常等等
> - **PointCut（切入点、横切点）**：按照定义`可以织入切面的点`，是多个`特定的连接点的集合`，在`Spring中`表现为一个`匹配连接点的表达式`（更具体的说可以是匹配特定的方法，而这个方法拥有**多个连接点**）
> - **Aspect（切面）**：切面可以是`一个类`，这个类里面`定义了切入点及其对应的通知、以及它们之间的具体连接关系`
> - **织入**：织入是指将增强的 `通知 连接到 具体的连接点 上的过程`；



## 2、简单的AOP实现（重要在于整个Aop模型的实现）

### 2.1 Pointcut（切入点 这里只实现表达式类型的切入点）

> **Pointcut是Spring对AOP切点的顶层抽象**
>
> **`在Spring AOP中，是基于代理的模型来处理AOP的，所以也就只支持在方法级别上的切入点`**
>
> 所以Spring中的一个PointCut通常由一个`切入点表达式`组成，而该表达式就匹配了其定义的指定方法
>
> 比如说`execution(* com.silly.baka.aop.*.*(..))`，该表达式就`匹配了 com.silly.baka.aop包下的所有类的所有权限的任意参数的方法`
>
> 所以在PointCut中，要包含**`类过滤器，方法过滤器等方法`**

这里我只实现了简单的表达式切入点，并且使用AspectJ提供的api来实现表达式分析

![image-20221027214207288](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221027214207288.png)



### 2.2 Advice和`Advisor`

**Advice是AOP联盟提供的一个用于通知的标识接口，是`AOP联盟中对通知的顶层抽象`**

> **Advisor是在此基础上更加`通用的一个通知接口，是Spring AOP中切面的最小单位（一个Advisor是一个小切面包含了一个Pointcut以及一个advice）`，适用于所有类型的通知，并且可以`用于管理Advice和Pointcut`**
>
> Advisor能够`提高切面的可重用性`，它`将一个切面划分为了多个小切面`，`Spring以小切面为单位来管理AOP`

```java
public interface Advisor {

    /**
     * 空白的通知对象
     */
    Advice EMPTY_INSTANCE = new Advice() {};

    /**
     * 若配置了通知则返回通知对象，否则返回空白对象
     */
    Advice getAdvice();
}
```

**Spring中Advisor的继承体系如下**

![image-20221028134633083](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221028134633083.png)

`PointcutAdvisor`：适用于`几乎所有类型`的pointcut，以及几乎所有类型的advice

`IntroductionAdvisor`：只适用于`类级别`的pointcut和advice



### 2.3 Interceptor（拦截器）

> 这个接口代表一个**通用的拦截器**。 通用拦截器可以**拦截**基本程序中发生的**运行时事件**。 这些**`事件由（具体化）连接点实现，所以拦截器用于拦截连接点`**。
>  运行时连接点可以是调用、字段访问、异常… 该接口不直接使用。 使用子接口拦截特定事件。
>
> **`Interceptor用于拦截连接点，并在拦截的连接点处执行Advice的逻辑（实际上就是一个特定处理器）`**

### 2.4 MethodInvocation（方法调用）

> `MethodInvocation`接口是AOP联盟对 `方法调用` 的抽象，本质上是一个`连接点`，可以`被拦截器拦截`，在方法被调用时传递`MethodInvocation`给拦截器

![在这里插入图片描述](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/watermark%252Ctype_ZHJvaWRzYW5zZmFsbGJhY2s%252Cshadow_50%252Ctext_Q1NETiBATXIuQVo%253D%252Csize_20%252Ccolor_FFFFFF%252Ct_70%252Cg_se%252Cx_16.png)



### ==2.5 AdvisorAdapter和MethodBeforeAdviceAdapter==

> **`AdvisorAdapter`**是Spring提供的用于**`拓展`**外部**`自定义Advice和Advisor`**的顶层接口
>
> ```java
> public interface AdvisorAdapter {
> 
>    // 当前适配器是否支持目标类型的通知，能否为它创建相应的拦截器
>    boolean supportsAdvice(Advice advice);
> 
>    // 创建并获取指定Advisor所适配的拦截器
>    MethodInterceptor getInterceptor(Advisor advisor);
> }
> ```
>
> **`MethodBeforeAdviceAdapter`**是Spring内部用于**拓展MethodBeforeAdvice**这种类型的通知而创建的**适配器类**
>
> ```java
> class MethodBeforeAdviceAdapter implements AdvisorAdapter, Serializable {
> 
>    @Override
>    public boolean supportsAdvice(Advice advice) {
>       return (advice instanceof MethodBeforeAdvice);
>    }
> 
>    @Override
>    public MethodInterceptor getInterceptor(Advisor advisor) {
>       MethodBeforeAdvice advice = (MethodBeforeAdvice) advisor.getAdvice();
>       return new MethodBeforeAdviceInterceptor(advice);
>    }
> 
> }
> ```
>
> **`MethodBeforeAdviceInterceptor`**也是一个适配器类，将`MethodBeforeAdvice`及其`拦截器`适配到一起，方便拦截器执行通知的逻辑



### 2.6 织入（动态代理）

> 在前面我们定义了切入点（连接点）和通知，那么我们要`如何将通知织入连接点`呢？
>
> 采用**`动态代理`**的方式，根据提供的`通知、连接点以及它们之间的关系（还要有被织入的源方法、源对象等等很多东西）`，来创建一个**`代理对象`**

#### 2.5.1 Aop代理的配置类（AdvisedSupport)

> 为了避免动态代理类中`用于配置Aop代理`的代码过于冗余，基于职责划分，诞生了此类 `AdvisedSupport`
>
> `AdvisedSupport`类中必须提供的属性：**动态代理的方式**、**将被Aop代理的连接点源对象**、**绑定在该连接点的通知链** 等等

#### 2.5.2 基于JDK动态代理实现的Aop代理

`Spring中JdkDynamicAopProxy类 invoke方法（代理后执行的方法）的部分源码`

![image-20221028131830700](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221028131830700.png)



**从源码可知动态代理的处理流程如下：**

`Spring中使用JDK动态代理处理Aop代理的流程如下`

![image-20221028131540034](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221028131540034.png)



**代码实现**

```java
public class JdkDynamicAopProxy implements AopProxy,InvocationHandler{

    /**
     * 用于配置代理类
     */
    private final AdvisedSupport advisedSupport;

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),advisedSupport.getInterfaces(), this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //todo 需要通知的具体逻辑以及执行时机 --> 需要通知对象

        TargetSource targetSource = advisedSupport.getTargetSource();
        Object target = targetSource.getTarget();

        Class<?> targetClass = targetSource.getTargetClass();

        Class<?> actualClass = (targetClass != null ? targetClass : target.getClass());

        Object retVal;

        // 获取连接点目标对象的拦截器链
        List<Object> chain = advisedSupport.getAdvisorChainFactory().getInterceptorsAndDynamicInterceptionAdvice(advisedSupport, method, actualClass);

        // 若拦截器链为空 则直接执行原方法
        if(chain.isEmpty()){

            method.setAccessible(true);
            retVal = method.invoke(target,args);

        }else{
        // 否则执行拦截器链 再执行原方法
            ReflectiveMethodInvocation reflectiveMethodInvocation = new ReflectiveMethodInvocation(proxy, target, method, args, chain);
            retVal = reflectiveMethodInvocation.proceed();
        }

        return retVal;
    }
}
```



#### 2.5.3 基于Cglib动态代理实现的Aop代理

这里的流程和Jdk动态代理类似，但不同的是，在Cglib中可以通过 `proxy.invoke()`这个方法来`调用原方法`，以这种方法调用方法`比使用反射调用效率更高`

**代码实现**

```java
public class CglibAopProxy implements AopProxy{

    private final AdvisedSupport advisedSupport;

    public CglibAopProxy(AdvisedSupport config){
        this.advisedSupport = config;
    }

    @Override
    public Object getProxy() {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advisedSupport.getTargetClass());
        enhancer.setCallback(new CglibMethodInterceptor());

        return enhancer.create();
    }

    private class CglibMethodInterceptor implements MethodInterceptor{

        /**
         * 这里的实现和jdk动态代理一模一样
         */
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            TargetSource targetSource = advisedSupport.getTargetSource();
            Object target = targetSource.getTarget();
            Class<?> targetClass = targetSource.getTargetClass();

            List<Object> chain = advisedSupport.getAdvisorChainFactory().getInterceptorsAndDynamicInterceptionAdvice(advisedSupport, method, targetClass);

            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(obj, target, method, args, chain, proxy);

            Object retVal;

            if(chain.isEmpty()){
                retVal = methodInvocation.invokeJoinPoint();
            }else {
                retVal = methodInvocation.proceed();
            }
            return retVal;
        }
    }

    private class CglibMethodInvocation extends ReflectiveMethodInvocation{

        private final MethodProxy methodProxy;

        public CglibMethodInvocation(Object proxy, Object target, Method method, Object[] args, List<Object> interceptorAndAdviceList,
                                     MethodProxy methodProxy) {
            super(proxy, target, method, args, interceptorAndAdviceList);
            this.methodProxy = methodProxy;
        }

        /**
         * 利用Cglib提供的原方法调用 效率比反射调用快
         */
        @Override
        public Object invokeJoinPoint() {
            try {
                return methodProxy.invoke(this.target,this.args);
            } catch (Throwable e) {
                throw new AopConfigException("被代理的原方法执行失败",e);
            }
        }
    }
}
```

#### 2.5.4 Jdk动态代理和Cglib动态代理的底层原理

`Jdk动态代理只支持代理实现了接口的类，而Cglib动态代理可以支持几乎所有的类（不用final修饰）`

##### 1、Jdk动态代理

如果我们要为target类创建一个【JDK动态代理对象】，那么我们必须要传入如下三个核心参数

- 加载target类的**类加载器**
- target类**实现的接口**
- **InvocationHandler**

`那为什么需要这三个参数呢？`

我们先提供一个接口及其简单实现类，然后再实现其动态代理**（不会的自己去百度）**

```java
public interface Subject {
    void doSomething();
    String sayHello(String str);
}
```

**`接下来分析动态代理类的结构`**

1. 动态代理类`继承了Proxy类`，实现了`Subject接口（被代理的类实现的接口）`

   **`这也就是为什么Jdk动态代理只能支持实现了接口的类，因为Java是单继承 多接口的机制，动态代理类已经继承了Proxy类，就无法再继承其他的类`**

2. 可以看到动态代理类中每一个方法的执行 都会是同样的逻辑

   `super.h.invoke(this, mehotd, new Object[]{var})`

   那么这个**`super.h属性`**到底是什么呢？

   熟悉Jdk动态代理的人应该知道`InvocationHandler接口`，而这个接口中就有这样一个方法

   `public Object invoke(Object proxy, Method method, Object[] args)`

看起来这个方法的结构跟`super.h.invoke(this, mehotd, new Object[]{var})`很相似，那会不会就是一个invocationHandler呢？

```java
public final class $Proxy0 extends Proxy implements Subject {

    private static Method m1;
    private static Method m3;
    private static Method m4;
    private static Method m2;
    private static Method m0;
  
    static {
          try {
              m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
              m3 = Class.forName("com.stone.design.mode.proxy.jdk.Subject").getMethod("doSomething");
              m4 = Class.forName("com.stone.design.mode.proxy.jdk.Subject").getMethod("sayHello", Class.forName("java.lang.String"));
              m2 = Class.forName("java.lang.Object").getMethod("toString");
              m0 = Class.forName("java.lang.Object").getMethod("hashCode");
          } catch (NoSuchMethodException var2) {
              throw new NoSuchMethodError(var2.getMessage());
          } catch (ClassNotFoundException var3) {
              throw new NoClassDefFoundError(var3.getMessage());
          }
      }

    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }

    public final boolean equals(Object var1) throws  {
        try {
            // 这里的h属性实际上是InvocationHandler
            return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }
    // 实现接口的方法
    public final void doSomething() throws  {
        try {
            // 这里的h属性实际上是InvocationHandler
            super.h.invoke(this, m3, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }
    // 实现接口的方法
    public final String sayHello(String var1) throws  {
        try {
            // 这里的h属性实际上是InvocationHandler
            return (String)super.h.invoke(this, m4, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final String toString() throws  {
        try {
            // 这里的h属性实际上是InvocationHandler
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final int hashCode() throws  {
        try {
            // 这里的h属性实际上是InvocationHandler
            return (Integer)super.h.invoke(this, m0, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

}

```

**接下来再查看Jdk动态代理的核心方法**

`Proxy.newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h)`

可以看到`newProxyInstance`方法的流程

1. 根据`类加载器`以及`被代理类实现的接口`，`获取代理类`
2. 获取代理类的`有参构造器`并且以`传入的InvocationHandler作为参数`创建代理类对象，并返回

所以`代理对象每一次执行方法`，都会调用`InvocationHanlder.invoke()`的逻辑

```java
public class Proxy implements java.io.Serializable {
// h属性，保存我们传递进来的InvocationHandler
protected InvocationHandler h;
  
// 【有参构造器】注意这里的参数
protected Proxy(InvocationHandler h) {
  Objects.requireNonNull(h);
  this.h = h;
}

// 生成代理对象的方法
public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces,InvocationHandler h)
    throws IllegalArgumentException{

  	// 1、InvocationHandler强制不允许为空
    Objects.requireNonNull(h);
    // 获取到目标接口
    final Class<?>[] intfs = interfaces.clone();

    /*
     * 2、获取到代理类的Class对象（也就是Proxy）
     */
    Class<?> cl = getProxyClass0(loader, intfs);

    /*
     * 通过反射执行 cl 的有参构造，也就是下面这个，可以看到通过反射执行Proxy有参构造，
     * 将InvocationHandler赋值到了h属性上
     */
    try {
        // 3、获取到有参构造器
        final Constructor<?> cons = cl.getConstructor(constructorParams);
        // 4、通过构造器来创建一个代理对象并返回，这里传入的参数h 就是我们的【InvocationHandler】
        return cons.newInstance(new Object[]{h});
    } catch (IllegalAccessException|InstantiationException e) {
        // 省略....
    }
}
}
```
**`Jdk动态代理的本质`**

Jdk动态代理为被代理类**`创建了一个代理类`**，这个代理类**`继承了Proxy类、实现了被代理类实现的所有接口`**

代理类中`所有接口实现方法的逻辑`，都是`调用`创建代理对象时传入的**`InvocationHanlder所实现的invoke()方法`**



##### 2、Cglib动态代理

> cglib的原理是通过`字节码技术`为一个类**`创建子类`**，并在子类中采用**`方法拦截`**的技术**`拦截所有父类方法的调用`**。由于是`通过创建子类来代理父类`，因此不能代理被**`final`**修饰的类(代理`final`修饰的类会抛异常，代理`final`修饰的方法只会原样执行委托类的方法而不能做任何拦截)。
>
> 但是cglib有一个很致命的缺点：cglib的底层是采用著名的`ASM`[字节码](https://so.csdn.net/so/search?q=字节码&spm=1001.2101.3001.7020)生成框架，使用字节码技术生成代理类，也就是通过操作字节码来生成的新的.class文件，而我们在`android`中加载的是`优化后的.dex文件`，也就是说我们需要可以动态生成.dex文件代理类，因此cglib在`Android`中是不能使用的。



#### ==2.5.5 AOP与动态代理的关系==

> `AOP`是**面向切面编程**，是一种`编程思想`，是面向对象编程的`补充`
>
> `动态代理`是一种在程序运行时，创建目标对象的`代理对象`，并对目标对象中的方法进行`功能性增强`的`一种技术`
>
> 在`Spring AOP`中，实现了动态代理来实现AOP，动态代理只是AOP的一种`实现方式`



## ==3、将AOP融入Bean的生命周期==

### 3.1 如何融入Bean的生命周期

> **首先分析一下**
>
> `如果AOP要融入Bean的生命周期，Spring要做些什么？`
>
> 1. `读取`用户定义的切入点、连接点以及通知等的`定义`
> 2.  根据切入点的定义，`代理被切入点所涉及到的Bean实例（为其生成代理对象）`
> 3.  以代理对象`替代原Bean实例`，作为`代理Bean`来使用
>
> **再从上面三点继续分析**
>
>  `如何具体实现以上三点 又不改变其他非代理Bean的生命周期（融入生命周期）？`
>
> **Spring中的做法：**
>
>  在`执行原实例化策略之前`，给这个Bean一次机会，看看它`能否被实例化为代理Bean`
>
> 具体看以下源码，`AbstractAutowireCapableBeanFactory#createBean（）` 
>
> ![Spring中createBean的部分源码](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221101154909224.png)
>
> 
>
> `AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation（）`
>
> 这里使用到了 `特殊的BeanPostProcessor--InstantiationAwareBeanPostProcessor（实例化前后的后置处理器）`
>
> ```java
> 	// 在实例化Bean之前 尝试能否将其实例化为代理Bean的逻辑
> protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
>    Object bean = null;
>  	 // 实例化后置处理器是否已启动
>    if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
>       	// 当前的BeanFactory是否拥有 实例化用的后置处理器
>       if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
>          Class<?> targetType = determineTargetType(beanName, mbd);
>          if (targetType != null) {
>              // 执行实例化的后置处理器  实例化前 的方法 （其实就是实例化成代理Bean 并且初始化）
>             bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
>             if (bean != null) {
>                 // 对实例化完的代理Bean 执行 初始化后 的方法 （那初始化交给谁了？）
>                bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
>             }
>          }
>       }
>       mbd.beforeInstantiationResolved = (bean != null);
>    }
>    return bean;
> }
> ```



### 3.2 自动创建代理Bean的具体逻辑（`AbstractAutoProxyCreator`）

Spring中的`AbstractAutoProxyCreator`的**继承关系图**：

![image-20221101193517743](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221101193517743.png)



> **源码分析：**
>
> **`重点方法：`** 
>
> `AbstractAutoProxyCreator#postProcessBeforeInstantiation()`
>
> ![image-20221101175659704](C:\Users\86176\Desktop\JAVA\my-mini-spring\my-mini-spring.assets\image-20221101175659704.png)
>
> 
>
> `AbstractAutoProxyCreator#createProxy()`  **`这里还没看懂 后续更新？这里的类关系太复杂了`**
>
> ```java
> protected Object createProxy(Class<?> beanClass, @Nullable String beanName,
>       @Nullable Object[] specificInterceptors, TargetSource targetSource) {
> 
>    if (this.beanFactory instanceof ConfigurableListableBeanFactory) {
>       AutoProxyUtils.exposeTargetClass((ConfigurableListableBeanFactory) this.beanFactory, beanName, beanClass);
>    }
> 
>    ProxyFactory proxyFactory = new ProxyFactory();
>    proxyFactory.copyFrom(this);
> 
>    if (proxyFactory.isProxyTargetClass()) {
>       // Explicit handling of JDK proxy targets and lambdas (for introduction advice scenarios)
>       if (Proxy.isProxyClass(beanClass) || ClassUtils.isLambdaClass(beanClass)) {
>          // Must allow for introductions; can't just set interfaces to the proxy's interfaces only.
>          for (Class<?> ifc : beanClass.getInterfaces()) {
>             proxyFactory.addInterface(ifc);
>          }
>       }
>    }
>    else {
>       // No proxyTargetClass flag enforced, let's apply our default checks...
>       if (shouldProxyTargetClass(beanClass, beanName)) {
>          proxyFactory.setProxyTargetClass(true);
>       }
>       else {
>          evaluateProxyInterfaces(beanClass, proxyFactory);
>       }
>    }
> 
>    Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
>    proxyFactory.addAdvisors(advisors);
>    proxyFactory.setTargetSource(targetSource);
>    customizeProxyFactory(proxyFactory);
> 
>    proxyFactory.setFrozen(this.freezeProxy);
>    if (advisorsPreFiltered()) {
>       proxyFactory.setPreFiltered(true);
>    }
> 
>    // Use original ClassLoader if bean class not locally loaded in overriding class loader
>    ClassLoader classLoader = getProxyClassLoader();
>    if (classLoader instanceof SmartClassLoader && classLoader != beanClass.getClassLoader()) {
>       classLoader = ((SmartClassLoader) classLoader).getOriginalClassLoader();
>    }
>    return proxyFactory.getProxy(classLoader);
> }
> ```



**`我的逻辑：比Spring简化了很多`**

`简单的从IOC容器中获取所有已注册的AdvisorBean，然后依次检查 Advisor的切入点 是否适配当前bean，若适配则将其加入AOP配置中，最后再根据AOP配置 使用动态代理生成代理对象`

```java
public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {

    // 检查当前bean类型是否为AOP基础构件
    if(isInfrastructureClass(beanClass)){
        // 如果是则无法生成代理对象
        return null;
    }

    BeanDefinition<?> beanDefinition = beanFactory.getBeanDefinition(beanName, beanClass);
  
   	// 正常这里应该是从IOC容器内置bean中获取（不供外使用、只允许在AOP中获取） 但我偷懒直接实例化
    TargetSource targetSource = new TargetSource(new SimpleInstantiationStrategy().instantiation(beanDefinition));

    return createProxy(beanClass,beanName,targetSource);
}

    /**
     * 为指定bean创建代理对象的实际逻辑
     */
    protected Object createProxy(Class<?> beanClass,String beanName,TargetSource targetSource){
        // 创建IOC配置类对象
        AdvisedSupport advisedSupport = new AdvisedSupport();

        advisedSupport.setTargetSource(targetSource);
        
        // 默认采用Cglib动态代理
        advisedSupport.setProxyTargetClass(true);

        // todo 这里先获取大家都通用的Advisor，后面应该可能有指定给哪个Bean的Advisor
        String[] advisorNames = beanFactory.getBeanNamesForType(Advisor.class, true);

        List<Object> advisors = new ArrayList<>();
        for (String advisorName : advisorNames) {
            Object advisorObject = beanFactory.getBean(advisorName);
            // 匹配每一个Advisor
            if(advisorObject instanceof AspectJExpressionPointcutAdvisor){
                AspectJExpressionPointcutAdvisor advisor = (AspectJExpressionPointcutAdvisor) advisorObject;
                // 获取切入点
                Pointcut pointcut = advisor.getPointcut();
                ClassFilter classFilter = pointcut.getClassFilter();
                // 判断该切入点是否匹配当前bean
                if(classFilter.matches(beanClass)){
                    advisors.add(advisor);
                }
            }
        }
        // 如果没有对应的Advisor 则说明无法生成代理对象 返回null
        if(advisors.size() == 0){
            return null;
        }
        advisedSupport.addAdvisors(advisors.toArray(new Advisor[0]));

        AopProxyFactory aopProxyFactory = new DefaultAopProxyFactory();
        // 创建代理对象并返回
        return aopProxyFactory.createAopProxy(advisedSupport).getProxy();
    }
```



### 3.3 当前Bean生命周期



![image-20221101164830189](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221101164830189.png)



### 3.4 测试

`TestAutoProxy.java`

```java
public class TestAutoProxy {

    @Test
    public void test1(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:testAutoProxy.xml");

        Object object = applicationContext.getBean("helloService", HelloService.class);

        HelloService helloService = (HelloService) object;
        helloService.hello();
    }
}
```

`testAutoProxy.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context  http://www.springframework.org/schema/context/spring-context.xsd">

    <bean id="autoProxyAdivisor1" class="sillybaka.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor">
        <property name="advice" ref="testAdvice1"/>
        <property name="pointcut" ref="testPointcut"/>
    </bean>
    <bean id="autoProxyAdivisor2" class="sillybaka.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor">
        <property name="advice" ref="testAdvice2"/>
        <property name="pointcut" ref="testPointcut"/>
    </bean>
    <bean id="testAdvice1" class="sillybaka.springframework.aop.TestMethodBeforeAdvice"/>
    <bean id="testPointcut" class="sillybaka.springframework.aop.aspectj.AspectJExpressionPointcut">
        <property name="expression" value="execution(* sillybaka.springframework.aop.*.*(..))"/>
    </bean>
    <bean id="testAdvice2" class="sillybaka.springframework.aop.TestMethodAfterAdvice"/>
    <bean id="helloService" class="sillybaka.springframework.aop.HelloServiceImpl">

    </bean>
</beans>
```





# 拓展篇

## 1、处理bean定义中的占位符（PropertyResourceConfigurer 和 PropertyPlaceholderConfigurer）

> 在上文中提到了`BeanFactoryPostProcessor`，也介绍了它在Spring中主要的用途：`对Bean定义中的占位符进行替换`。
>
> 今天就来分析它的源码以及具体实现流程。这里先实现简单的`properties+占位符`替换，后面实现使用`注解@Values`注入

### **1.1 PropertiesResourceConfigurer实现**

**`分析源码`**

> `PropertyResourceConfigurer#postProcessBeanFactory（实现BeanFactoryPostProcessor接口的方法）`
>
> ```java
> @Override
> public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
>    try {
>        // 1、加载上下文中定义的所有properties文件，并将其合并在同一个Properties中
>       Properties mergedProps = mergeProperties();
>        // 2、将合并的Properties按照一定规则转化（没定义别的规则 可能以后拓展用？）
>       convertProperties(mergedProps);
> 	   // 3、将合并后的properties注入到BeanFactory的BeanDefinition中
>        // 重点方法
>       processProperties(beanFactory, mergedProps);
>    }
>    catch (IOException ex) {
>       throw new BeanInitializationException("Could not load properties", ex);
>    }
> }
> ```
>
> `processProperties(beanFactory, mergedProps)`是一个`抽象方法`，实际的`填充逻辑`会交给`子类实现`
>
> **`PropertiyOverrideConfiguer`**: 直接使用Property中的指定属性`替换BeanDefinition中的指定属性`
>
> ​							properties文件中的格式： `"bean.属性名称=属性值"`
>
> **`PropertyPlaceholderConfiguer（最常用）`**: 使用Property中的指定属性 `替换BeanDefinition中`**`使用了占位符的指定属性`**
>
> ​							BeanDefinition中的格式：`<property value="${propertyName}"/>`
>
> ​							properties文件中的格式： `propertyName=value`
>
> ![image-20221102180207973](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221102180207973.png)



**我的实现**

```java
public abstract class PropertiesResourceConfigurer extends PropertiesLoaderSupport implements BeanFactoryPostProcessor{

    /**
     * 模板方法 具体逻辑子类实现
     * @param beanFactory 上下文中所使用的工厂对象
     */
    @Override
    public void postProcessBeanFactory(ConfigurableBeanFactory beanFactory){
        // 1、读取上下文的property
        Properties props = null;
        try {
            props = loadProperties();
        } catch (NestedIOException e) {
            log.error("加载上下文配置文件时出错",e);
        }
        // 2、将property应用于BeanFactory
        processProperties(beanFactory,props);
    }

    /**
     * 将给定的property中的属性应用于BeanFactory中的bean定义
     * @param beanFactory 指定的BeanFactory
     * @param props 属性
     */
    protected abstract void processProperties(ConfigurableBeanFactory beanFactory, Properties props);
}

public class PropertiesLoaderSupport {

    private ResourceLoader resourceLoader;

    private String[] locations;

    public PropertiesLoaderSupport(){}

    public PropertiesLoaderSupport(String...locations){
        this.locations = locations;
    }

    /**
     * 从上下文中加载所有的properties
     * @return 上下文中所有properties的集合
     */
    protected Properties loadProperties() throws NestedIOException {

        Properties properties = new Properties();

        for (String location : locations) {
            Resource resource = resourceLoader.getResource(location);
            try {
                properties.load(resource.getInputStream());
            } catch (IOException e) {
                throw new NestedIOException("load properties [" + resource.getFileName() + "] error");
            }
        }

        return properties;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setLocations(String[] locations) {
        this.locations = locations;
    }
}
```



### 1.2 PropertyPlaceholderConfigurer实现`（用于替换bean定义中的占位符）`

> **`首先分析一下我们起初定义的BeanDefinition结构，它被加载进来的时候是什么样子的？（实例化+自动装配属性之前）`**
>
> BeanDefinition中`每一个属性都是一个PropertyValue`， 属性名-属性值，并且都是`String类型`的
>
> 所以这里的实现就很简单了**（在String中则是委托给了`StringValueResolver`来实现）**
>
> `PropertySourcesPlaceholderConfigurer#processProperties()的源码`
>
> ![image-20221103173416366](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221103173416366.png)
>
> `PlaceholderConfigurerSupport#doProcessProperties()的源码`
>
> ![image-20221103174045375](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221103174045375.png)



**我的思路：**`扫描properties中的每一个Property，扫描每一个beanDefinition中的每一个属性值，判断其是否是${propertyName}格式的，若是 则取出propertyName，并从properties中取出属性值并替换。`

**我的实现**

```java
public class PropertyPlaceholderConfigurer extends PropertyResourceConfigurer {

    private static final String PLACEHOLDER_PREFIX = "${";
    private static final String PLACEHOLDER_SUFFIX = "}";

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
        for (PropertyValue pv : propertyValues.getPropertyValues()) {
            String propertyValue = (String) pv.getPropertyValue();
            // 如果属性值是占位符 则替换
            if(propertyValue.startsWith(PLACEHOLDER_PREFIX) && propertyValue.endsWith(PLACEHOLDER_SUFFIX)){
                int length = propertyValue.length();
                propertyValue = propertyValue.substring(PLACEHOLDER_PREFIX.length(),length-PLACEHOLDER_SUFFIX.length());

                // 作为key从props中获取属性
                propertyValue = props.getProperty(propertyValue);
            }
            propertyValues.addPropertyValue(new PropertyValue(pv.getPropertyName(),propertyValue));
        }
        beanDefinition.setPropertyValues(propertyValues);
    }
}
```



### 1.3 单元测试

```java
public class testPlaceholder {

    @Test
    public void test1(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testPlaceholder.xml");
        Object testCar = classPathXmlApplicationContext.getBean("testCar");
        System.out.println(testCar);
    }

    @Test
    public void test2(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testPlaceholder2.xml");
        Object carRoll = classPathXmlApplicationContext.getBean("carRoll");
        System.out.println(carRoll);
    }
}
```

`testPlaceholder.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context  http://www.springframework.org/schema/context/spring-context.xsd">

    <bean id="testCar" class="sillybaka.springframework.entity.Car">
        <property name="brand" value="${brand}"/>
        <property name="price" value="${price}"/>
        <property name="owner" value="${owner}"/>
        <property name="carRoll" ref="carRoll"/>
    </bean>
    <bean id="carRoll" class="sillybaka.springframework.entity.CarRoll">
        <property name="brand" value="牛马"/>
    </bean>
</beans>
```

`testPlaceholder2.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context  http://www.springframework.org/schema/context/spring-context.xsd">

   <bean id="placeholderPostProcessor"
         class="sillybaka.springframework.beans.factory.config.PlaceholderConfigurerSupport">
      <property name="location" value="classpath:testPlaceholder2.properties"/>
   </bean>

   <bean id="carRoll" class="sillybaka.springframework.entity.CarRoll">
      <property name="brand" value="${niubi}"/>
   </bean>
</beans>
```



## 2、包扫描（Component-Scan）

> ### `Component-Scan介绍`
>
> Spring中提供了一个`自动扫描指定包下所有Bean组件（即带有@Component、@Controller、@Service 等接口的类）`的功能 ———— **`Component-Scan`**。 这个功能能够**`允许自动扫描Bean，减少我们在XML文件中诸如定义Bean之类的大量且频繁的操作。`**
>
> **`那么它的底层实现逻辑是什么样的呢？`**
>
> ![image-20221103210710547](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221103210710547.png)



### 调用时机

`既然Component-Scan是用于避免在XML文件中定义大量的Bean，那么Component-Scan应当也是用于扫描bean定义`

**`所以应当在扫描XML文件中bean定义的同时 扫描指定包下的所有@Component bean`**

### 代码实现

```java
/**
 * 提供了从指定basePackage中读取所有候选Component组件（即带有@Component或其子注解的类）的基本实现
 * <p>Date: 2022/11/3
 * <p>Time: 21:09
 *
 * @Author SillyBaka
 **/
public class ClassPathScanningCandidateComponentProvider {

    /**
     * 从指定的包读取所有候选components的bean定义
     * @param basePackage 指定的包
     * @return 指定包下所有Component的bean定义
     */
    protected Set<BeanDefinition<?>> scanCandidateComponents(String basePackage){
        Set<Class<?>> classSet = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        Set<BeanDefinition<?>> beanDefinitionSet = new HashSet<>();
        for (Class<?> clazz : classSet) {
            beanDefinitionSet.add(parseClassToBeanDefinition(clazz));
        }
        return beanDefinitionSet;
    }

    /**
     * 将指定类转换成相应的BeanDefinition
     * @param clazz 指定类
     */
    protected BeanDefinition<?> parseClassToBeanDefinition(Class clazz){
        BeanDefinition<?> beanDefinition = new BeanDefinition<>();
        beanDefinition.setType(clazz);

        // 还要将该bean的所有属性注册进IOC容器
        PropertyUtils.addAllPropertyDescriptor(clazz);

        return beanDefinition;
    }
}

/**
 * 扫描器，用于获取指定包内所有加了特定注解的Bean定义，并将其包装成一个完整的Bean定义
 * <p>Date: 2022/11/3
 * <p>Time: 21:08
 *
 * @Author SillyBaka
 **/
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    public Set<BeanDefinition<?>> doScan(String[] basePackages){

        Set<BeanDefinition<?>> beanDefinitionSet = new HashSet<>();
        for (String basePackage : basePackages) {
            for (BeanDefinition<?> candidate : scanCandidateComponents(basePackage)) {
                String scope = resolveBeanScope(candidate);
                if(StrUtil.isNotBlank(scope)){
                    candidate.setScope(scope);
                }
                beanDefinitionSet.add(candidate);
            }
        }

        return beanDefinitionSet;
    }

    public String resolveBeanScope(BeanDefinition<?> beanDefinition){
        Class<?> clazz = beanDefinition.getType();
        Scope scope = clazz.getAnnotation(Scope.class);
        if(scope != null){
            return scope.value();
        }
        // 默认单例
        return "singleton";
    }

    public String determineBeanName(BeanDefinition<?> beanDefinition){
        Class<?> clazz = beanDefinition.getType();
        Component annotation = clazz.getAnnotation(Component.class);
        String value = annotation.value();
        if(StrUtil.isNotBlank(value)){
            return value;
        }
        // 默认beanName为 类名首字母小写
        return StrUtil.lowerFirst(clazz.getSimpleName());
    }
}
```



## 3、@Value注解（属性注入）

> ### `@Value注解介绍`
>
> **在Spring中，@Value注解一般用于对Bean注入外部化的属性（从配置文件中读取）**
>
> `Spring的@Value注解定义`
>
> ![image-20221104110533233](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221104110533233.png)
>
> `Spring中@Value注解的多种用法`
>
> 1. `@Value("propertyValue")`    直接将**propertyValue**注入给当前属性
> 2. `@Value("${propertyName}")`  从**Properties文件中读取**属性名为propertyName的属性值，并注入给当前属性
> 3. `@Value("#{spEL表达式}")`     `解析spEL表达式`，根据表达式运算后的属性来注入属性
>
> **这里我们只实现前面两种**

### `调用时机`

**在Spring中，使用@Value注解标注的属性通常优先级更高，即比在XML文件中的Bean定义优先级更高，所以实现时要`考虑注解中的属性覆盖Bean定义中的同名属性`**

**所以调用的时机应该`在Bean实例化之后（之前都行），在为Bean自动装配属性之前`**  `--- 参考源码AbstractAutowireCapableBeanFactory#populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw)`

**流程图**

![image-20221104205816371](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221104205816371.png)

### 代码实现

```java
/**
 * 用于在Bean实例化之后装配属性之前，自动解析注解@Autowired和@Value的BeanPostProcessor
 * <p>Date: 2022/11/4
 * <p>Time: 16:13
 *
 * @Author SillyBaka
 **/
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor, InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    public AutowiredAnnotationBeanPostProcessor(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 在Bean实例化之后，自动装配属性之前调用，用于修改@Value注解定义的属性
     * @param pvs 属性列表
     * @param bean bean实例
     * @param beanName bean名字
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();

        StringValueResolver[] embeddedValueResolvers = beanFactory.getEmbeddedValueResolvers();

        List<PropertyValue> propertyValueList = pvs.getPropertyValueList();

        // 处理字段上的@Value注解
        for (Field field : beanClass.getDeclaredFields()) {
            Value value = field.getAnnotation(Value.class);
            String fieldName = field.getName();

            if(value != null){
                String propertyValue = value.value();
                // 解析字符串里的占位符
                for (StringValueResolver embeddedValueResolver : embeddedValueResolvers) {
                    propertyValue = embeddedValueResolver.resolveStringValue(propertyValue);
                }
                // 如果解析完不为空 则注入bean的属性中 替换掉Bean定义中的同名属性 或直接放入
                if(StrUtil.isNotBlank(propertyValue)){
//                    BeanUtil.setFieldValue(bean,field.getName(),propertyValue);
                    int length = propertyValueList.size();

                    boolean hasSame = false;
                    // 查找pvs中的同名属性，将其替换 防止冲突
                    for (int i = 0; i < length; i++) {
                        PropertyValue pv = propertyValueList.get(i);
                        if(pv.getPropertyName().equals(fieldName)){
                            pv.setPropertyValue(propertyValue);

                            propertyValueList.set(i,pv);

                            hasSame = true;
                            break;
                        }
                    }
                    // 若没有同名属性，则直接放入
                    if(!hasSame){
                        propertyValueList.add(new PropertyValue(fieldName,propertyValue));
                    }
                }
            }
        }

        return pvs;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }
}
```



## 4、@Autowired注解（依赖注入）





# 结尾篇

## ==Spring中循环依赖的解决方案==

















