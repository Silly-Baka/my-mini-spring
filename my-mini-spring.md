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





## 2、复杂的IOC容器（从BeanFactory过度到ApplicationContext）

### 1、更新BeanFactory的继承关系

![img](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/xml-file-define-bean-16659210080936.png)

![img](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/up-743c3654916a300a7d537c5a39ad625aefb.png)

​											**现继承关系**

#### 1.1 HierarchicalBeanFactory（分层的BeanFactory）

> `HierarchicalBeanFactory`为BeanFactory提供了分层的能力，让实现这个接口的BeanFactory之间拥有`层级关系（子级BeanFactory无法获得父级BeanFactory定义的Bean）`，为BeanFactory提供了可以`获取父级BeanFactory`的接口

#### 1.2 ListableBeanFactory（可列表化的BeanFactory）

> 实现了`ListableBeanFactory`接口的BeanFactory可以`一次性列出所有需要的Bean的信息`，提供了**查询Bean**的能力

#### 1.3 ConfigurableBeanFactory（可配置的BeanFactory）

> `ConfigurableBeanFactory`为BeanFactory提供了多个`配置BeanFactory`的接口，允许框架开发者**对BeanFactory进行自定义配置**

#### 1.4 AutowireCapableBeanFactory（可自动装配的BeanFactory）

> `AutowireCapableBeanFactory`为BeanFactory提供了`自动装配Bean属性`的接口

#### 1.5 ConfigurableListableBeanFactory

> `ConfigurableListableBeanFactory`接口**`整合了BeanFactory所需的所有特性`**，同时还**提供了分析和修改Bean的工具，也提供了解决循环依赖的方法（预定义Bean实例）**



### 2、 读取配置文件的Bean定义

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

#### 2.1 BeanDefinitionReader

> 流程：
>
> 1. 通过配置文件的**locations**，获取配置文件的资源**Resource**
> 2. **读取**每一个资源**Resource **`（需要ResourceLoader）`
> 3. 解析读取到的内容，**转化为BeanDefinition**
> 4. 将BeanDefinition **注册到BeanDefinitionRegistry** 中  `（需要BeanDefinitionRegistry）`

#### 2.2 XMLBeanDefinitionReader（实现读取XML文件中bean的逻辑）

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





### ==3、BeanFactoryPostProcessor 和 BeanPostProcessor（后置处理器）==

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



### ==4、 ApplicationContext（代表上下文的抽象接口，提供了获取上下文信息的基本接口）==

> **ApplicationContext**是Spring提供的**比BeanFactory更为先进的IoC容器实现**，ApplicationContext除了**拥有BeanFactory支持的所有功能之外**，还进一步扩展了基本容器的功能，**包括BeanFactoryPostProcessor、 BeanPostProcessor以及其他特殊类型bean的自动识别`（在BeanFactory中需要手动注册，在ApplicationContext中实现了自动注册）`**、**容器启动后bean实例的自动初始化`（在BeanFactory采用的是懒汉式加载）`**、**国际化的信息支持**、**容器内事件发布**等。
>
> **`本质上：ApplicationContext只是内置了一个BeanFactory，所以拥有BeanFactory的所有功能。同时ApplicationContext还实现了其他接口 拓展了其他功能。`**
>
> 
>
> **继承关系：**
>
> ![在这里插入图片描述](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/e1cc6cf9e3f14c76b7a8898eb29cc7f7.png)



#### 4.1 ConfigurableApplicationContext（可配置的上下文）

> **继承关系：**
>
> ![image-20221020161646845](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221020161646845.png)
>
> **ConfigurableApplicationContext** 提供了`对ApplicationContext进行配置的接口`，同时提供了**`管理ApplicationContext的生命周期（启动以及销毁）的接口`**，是**Spring中许多实现类的SPI接口**



#### 4.2 AbstractApplicationContext

> **继承关系：**
>
> ![image-20210414143029532](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/bfc38eaeff6de47ab1f9be89787a1d7a.png)
>
> **AbstractApplicationContext** 是 **ApplicationContext接口**的抽象实现，简单地实现了其中的通用上下文方法。
>
> 同时使用`模板方法设计模式`，定义了某些方法的抽象逻辑，其中`最重要的是refresh()方法`，**具体逻辑由其子类来实现**

##### ==refresh（启动上下文）执行流程（ApplicationContext中实例化bean并放入ioc容器的地方）==

![refresh流程图](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/refresh%E6%B5%81%E7%A8%8B%E5%9B%BE.png)

1. 创建上下文（**创建内置的beanFactory**，并**加载所有BeanDefinition**） --> 这一部分委托给子类 `AbstractRefreshableApplicationContext` 来进行实现

2. 在bean实例化之前，**调用BeanFactoryPostProcessor** **（执行修改beanDefinition的逻辑）**

   `疑问1：这里的BeanFactoryPostProcessor是什么时候注册进去的？（在前面一个步骤加载bean定义的时候）`

3. 在bean实例化之前，**自动注册 BeanPostProcessor** **（注册拓展组件）**

   `疑问2：BeanPostProcessor算是一个特殊bean，那么它们的生命周期与普通bean相同吗？（出生比普通bean早一点，因为要负责其他bean实例化的前后置处理，但其他周期都一样）`

4. 自动注册其他拓展组件....

5. **实例化所有的bean**

6. 上下文启动完毕



#### 4.3 AbstractRefreshableApplicationContext（实现了refreshBeanFactory的实际逻辑）

##### **refreshBeanFactory执行流程**

1. 判断内置的BeanFactory是否为空
2. 若不为空，则**清空内置BeanFactory的所有bean实例**，并**关闭内置的BeanFactory**
3. 创建新的BeanFactory（其父级BeanFactory为当前的父级上下文的内置BeanFactory）
4. **加载所有beanDefinition**进新的BeanFactory（`loadBeanDefinitions(beanFactory) 交给子类AbstractXmlApplicationContext 来进行实现`）



#### 4.4 AbstractXmlApplicationContext（实现了loadBeanDefinitions的实际逻辑）

![image-20221020204317054](https://raw.githubusercontent.com/Silly-Baka/my-pics/main/img/image-20221020204317054.png)

**FileSystemXMLApplicationContext（提供给用户使用的实现类）**：按照文件系统的路径来读取XML配置文件

**ClassPathXMLApplicationContext（提供给用户使用的实现类）**：按照类的根路径来读取XML配置文件

