# Neubbs
东软校内 BBS

# 技术栈
## 后端
+ JDK1.8.0_65
+ Tomcat8.0.41
+ Maven-3.3.9
+ MySQL5.7


# 目录结构

```
.
├── README.md
├── logs                                          [日志]
├── pom.xml                                       [Maven 配置]
└── src
    ├── main
    │   ├── java                                  [Java 源码]
    │   ├── resources                             [配置文件]
    │   │   ├── jdbc.properties                   [JDBC 数据库配置]
    │   │   ├── log4j.properties                  [log4j 日志配置]
    │   │   ├── mapping
    │   │   ├── rebel.xml
    │   │   ├── spring-applicationContext.xml     [SpringMVC 配置]
    │   │   └── spring-mybatis.xml                [Mybatis 配置]
    │   ├── sql                                   [数据库脚本]
    │   └── webapp
    │       ├── WEB-INF
    │       ├── css
    │       ├── html
    │       ├── index.jsp
    │       └── js
    └── test                                      [测试]
```
