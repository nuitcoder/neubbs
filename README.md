

# 目录
    1.项目介绍
    2.开发成员
    3.开发环境
    4.项目目录结构
    5.数据库设计
    6.期望功能
    
---




## 1.项目介绍
    东软校内论坛neubbs
    测试地址：http://localhost:8080/neubbs/index


## 2.开发成员
+ 【前台】ahonn
+ 【后台】kay,suvan
+ 【大数据】xiang
+ 【测试】jinru,lisa


## 3.开发环境
+ Java: jdk1.8.0_65
+ 应用服务器: Tomcat8.0.41
+ 项目管理:maven-3.3.9
+ 数据库:MySQL5.7


## 4.项目目录结构

```
neubbs
├──logs                                              [日志目录]
├──src                                               [源码目录]
|     ——main   
|           ——java                                   [java源码目录]
|               ——org.neusoft.neubbs
|                   ——controller
|                       ——filter
|                       ——interceptor
|                   ——dao
|                   ——entity
|                   ——utils
|                   ——service
|                       ——impl
|           ——resources                              [配置文件与映射目录]
|               ——mapping
|               ——jdbc.properties                    [数据库连接属性]
|               ——log4j.properties                   [log4j日志文件]
|               ——rebel.xml                          
|               ——spring-applicationContext.xml      [SpringMVC配置文件]
|               ——spring-mybatis.xml                 [Spring和Mybatis整合配置文件]
|           ——sql                                    [数据库脚本目录]
|           ——webapp                                 [静态页面目录]
|               ——css
|               ——extends
|               ——file
|               ——frame
|               ——img
|               ——js
|               ——WEB-INF
|                   ——jsp
|                   ——lib
|                   ——web.xml
|               ——index.jsp
|    ——test                                          [jUnit测试类目录]
|           ──test.org.neusoft.neubbs
├──neubbs.iml                            
├──pom.xml                                  [Maven配置文件]
├──README.md                                [项目说明书]
```



## 5.数据库设计
```

-- 1.创建数据库
CREATE DATABASE `neubbs`
	DEFAULT CHARACTER SET utf8
	COLLATE utf8_general_ci;

-- 2.创建用户表
CREATE TABLE user(
        u_id INT  AUTO_INCREMENT primary key,
        u_name VARCHAR(15) UNIQUE KEY,
        u_password VARCHAR(15) NOT NULL,
        u_sex  VARCHAR(2),
        u_birthday VARCHAR(20),
        u_address VARCHAR(15),
        u_phone VARCHAR(15),
        u_email  VARCHAR(50)  NOT NULL,
        u_registertime DATETIME DEFAULT NOW()
)ENGINE = innoDB DEFAULT CHARSET=utf8;

```


## 6.期望功能