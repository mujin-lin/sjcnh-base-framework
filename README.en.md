# sjcnh-base-framework

#### Introduction
The current framework is the out-of-the-box infrastructure information that is self-summarized on the job for subsequent project architecture construction and use.
* ApiClient: Simulate the feign call to write learning code, to implement Spring Boot similar to @EnableXxx annotation, Just type @ApiClient to use it and annotate @EnableApiClients on the startup class to scan the @ApiClient annotation in the corresponding base package. Currently, the proxy method is called using RestTemplate.
* sjcnh-abstract-web: The web base package, sjcnh-mongo-core and sjcnh-web rely on this package, provide the basic web functionality, and abstract the basic BaseService and BaseController;
  The specific implementation is implemented by the corresponding web package. In this package, the basic configuration of redis is also processed, using a custom serializer, CacheManager definition, and user login information saving method.
* sjcnh-common is the basic toolkit of the current framework. Contains all the basic tools and support for the request object access token in the org. Springframework.. The HTTP server. The reactive. ServerHttpRequest
  And javax.mail. Servlet. HTTP. It.
* sjcnh-data: It is mainly to re-package Mybatis plus, keep the previous configuration unchanged, and add some configurations of auto-fill fields, optimistic lock plug-in and anti-full table update plug-in suitable for this project.
* sjcnh-ftp: mainly implements the basic functions of ftp, and defines the ftp connection pool. Currently, SFtp is not supported, only ftp is supported. The connection pool performance is not tested, but written after learning by myself, and the upload and download function is used by automatically injecting FtpClientService.
* sjcnh-mongo-core: By implementing the basic class in sjcnh-abstract-web, extending BaseAbstractController and corresponding Service, and introducing spring-boot-starter-data-mongodb, mongodb access is realized. Provide its web capabilities,
  The fuzzy query to mongo is realized, and the paging query to mongodb documents is also provided.
* sjcnh-web: It mainly distinguishes sjcnh-mongo-core decouple sjcnh-abstract-web, and extends the function of Mybatis plus to BaseAbstractController. BaseController and BaseService are provided, and the addition and deletion of a single table does not need to be written repeatedly, and its Api is provided.
#### Software Architecture
sjcnh-base-framework is the parent pom. All other POMs are child POMs. The parent pom only manages versions and dependencies, and the child pom implements it by itself.
The relational database uses mybatis plus, while mongodb and redis are implemented using the springboot starter component.


#### installation tutorial
git clone this repository, maven install to the local repository behind the new project directly reference the coordinates of the current component.


#### Contribute
1. Fork the local warehouse
2. Create a branch of Feat_xxx
3. Submit the code
4. Create a Pull Request