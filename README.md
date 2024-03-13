# sjcnh-base-framework

#### 介绍
当前框架是在工作中自我总结的开箱即用的基础架构信息，用于后续项目架构搭建和使用。
* ApiClient：模拟feign调用方式编写学习的代码，实现了Spring Boot中类似@EnableXxx的注解，只需要打上@ApiClient就能使用并且在启动类上打上@EnableApiClients注解即可扫描对应基础包中的@ApiClient注解，目前代理方法使用的是RestTemplate进行调用。
* sjcnh-abstract-web:为web基础包，sjcnh-mongo-core和sjcnh-web依赖此包，提供了基础的web功能，并抽象了基础的BaseService和BaseController;
  具体实现由对应的web包进行实现，在此包中还对redis的基本配置进行了处理，使用了自定义的序列化器、CacheManager定义、用户登录信息保存方式等。
* sjcnh-common为当前framework的基础工具包，包含了所有的基本的工具类和对request对象中token的获取支持org.springframework.http.server.reactive.ServerHttpRequest
  和javax.servlet.http.HttpServletRequest。
* sjcnh-data：主要是再次对Mybatis-plus的再次封装，保持之前的配置不变，增加了一些适用于本工程的自动填充字段、乐观锁插件和防全表更新插件等的配置。
* sjcnh-ftp：主要实现了ftp的基本功能，自定义了ftp连接池，目前不支持SFtp，仅支持ftp，连接池性能没有进行测试，只是自己学习后写的，并通过自动注入FtpClientService的方式使用上传下载功能。
* sjcnh-mongo-core：通过实现sjcnh-abstract-web中的基础类，并扩展了BaseAbstractController和对应的Service，引入了spring-boot-starter-data-mongodb实现了对mongodb的访问，提供其web能力，
  并实现了对mongo的模糊查询等，同时也提供了对mongodb文档的分页查询能力。
* sjcnh-web：主要是区分sjcnh-mongo-core解耦sjcnh-abstract-web，对BaseAbstractController扩展的是Mybatis-plus的功能，提供了BaseController和BaseService,对单张表的增删查改无需重复编写，提供其Api。
#### 软件架构
sjcnh-base-framework为父pom其余均为子pom，父pom只管理版本和依赖关系，子pom自己实现。
关系型数据库使用的是mybatis-plus，mongodb和redis都是使用的springboot自带的starter组件实现。


#### 安装教程
git克隆本仓库，maven install到本地仓库后面新建项目直接引用当前组件的坐标即可。


#### 参与贡献
1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request