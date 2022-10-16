# IOC篇

## 1、简单的IOC容器

![img](C:/Users/86176/Desktop/%E7%AC%94%E8%AE%B0/my-mini-spring.assets/bean-definition-and-bean-definition-registry-16654901781672.png)

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

![img](my-mini-spring.assets/instantiation-strategy-16656693115683-16656693131115.png)



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
> 比如:没有标准化的URL实现类用于获取根据`ServletContext`的类路径。并且缺少某些Spring所需要的功能，例如**检测某资源是否存在**等，`java.net.url`只提供了基于标准URL来访问资源的方法**（针对web资源）**，而**不能基于特定的路径来访问特定的资源（无法针对文件资源、类路径资源）**
>
> ​	**`Resource接口`**是**Spring中所有资源的抽象和访问接口**，用于**解决URL接口的不足**
>
> ![img](my-mini-spring.assets/webp.webp)								**Spring中Resource接口的UML关系图**

![img](my-mini-spring.assets/resource-16658411461912.png)

​	`使用策略模式，在获取资源时先获取当前的上下文，再根据上下文的类型来获取相应类型的Resource类对象`

##### 1、 FileSystemResource（文件系统资源）

##### 2、 ClassPathResource（类路径资源）

> `ClassPathResource`表示从类路径获取的资源，通常使用线程上下文的ClassLoader进行资源加载.
> 我们的Web项目通常编译后，会将class文件存储在`WEB-INF/classes`下，Spring就可以通过`ClassPathResource`来访问这些文件.

##### 3、 UrlResource（Url资源）



#### 5.2 ResourceLoader接口（Spring中定位资源策略的抽象）

> ![img](my-mini-spring.assets/webp-16658927138937-16658928104089.webp)
>
> ​							 **Spring中ResourceLoader接口的UML关系图**
>
> ![image-20221016120015378](my-mini-spring.assets/image-20221016120015378-166589281704010.png)
>
>   **`ResourceLoader接口是资源加载定位策略的抽象，根据上下文使用策略模式获取对应的Resource实现类对象`**

##### 1、 DefaultResourceLoader

> **DefaultResourceLoader**是**`策略模式的实现`**，**可根据路径的前缀返回不同类型的Resource**
