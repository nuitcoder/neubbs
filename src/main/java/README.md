# Neubbs 后端
+ 一. 环境搭建
    - 数据库环境
        - MySQL 数据库
        - Redis 缓存数据库
    - 搭建服务器
        - FTP 服务器
        - nginx 服务器
        - Tomcat 服务器
    - 代码运行环境
        - 安装 JDK 1.8.0_65
        - Maven（免安装版即可）
        - 使用 Intellij IDEA （2016.3.5）
        - 配置 IDEA 环境
+ 二. 后台代码目录结构
+ 三. 开发约定
+ 四. 代码检查（Java 代码规范）




## 一. 环境搭建
---
### 数据库环境

#### MySQL数据库（5.7）
```
    <a> 建库
             CREATE DATABASE neubbs;
    <b> 建表
             执行 SQL 脚本， ...\neubbs\src\main\sql\initBuildForumDataTables.sql
    <c> 导入测试数据
             执行 SQL 脚本， ...\neubbs\src\main\sql\initInsertTestDataScipt.sql
```


#### Redis 缓存数据库（3.2.100）
```
    - 自行搭建
    - 用于做数据缓存与消息通知（未实现）
    - 修改配置文件 ...\neubbs\src\main\resources\redis.properties

        redis.host = 
        redis.port = 
        redis.timeout = 
        
        redis.pool.maxWaitMillis = 
        redis.pool.maxIdle = 
        redis.pool.minIdle = 
        redis.pool.testOnBorrow = 
        redis.pool.testOnReturn =
```

### 搭建服务器
#### FTP 服务器
```
    - 自行搭建
    - 用户存放用户个人头像
    - 搭建完在 ...\neubbs\src\main\resources\neubbs.properties 配置
            ftp.ip = （FTP 服务器 ip）
            ftp.port = （端口号）
            ftp.username = （连接用户名）
            ftp.password = （连接密码）
```

#### nginx 代理服务器（1.13.7）
```
    - 自行搭建 (与 FTP 服务器在同一云机器上)
    - 指向相同 FTP 服务器根目录（页面无法显示 ftp 协议指向的图片资源，所以使用 http 代理服务器指定资源）
    - 修改 ...\nginx-版本号\conf\nginx.conf
            http {

                ....

                server {
                    listen       80;                    //设置 80 端口
                    server_name  localhost;             //本地服务器名

                    .....


                    location / {
                        root   C:\ftp\Neubbs;          // 本地路径 ftp 根目录
                        ......
                    }

                    .....

                }

                ....
            }

    - 配置 ...\neubbs\src\main\resources\neubbs.properties
        nginx.url = （云代替服务器地址，例：http://（服务器 ip | 域名）/）
MD
```

#### Tomcat 应用服务器
```
    - apache-tomcat-8.0.41
```


### 代码运行环境
#### 安装 JDK 1.8.0_65
#### 本地 Maven（免安装版即可）
```
    - 设置配置文件 ...\Maven\apache-maven-3.3.9\conf
            <?xml version="1.0" encoding="UTF-8"?>
            <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
             
              <!--
                1.全局配置：${M2_HOME}/conf/settings.xml 【对操作系统的所有使用者生效】
                2.用户配置：${user.home}/.m2/settings.xml
                【对当前操作用户的使用者生效】
                        两者都存在，内容合并，用户的settings会覆盖全局的settings

              -->
              <!-- 本地仓库位置 -->
              <localRepository>E:\Java\Maven\repository</localRepository>

              <!--搜索插件组织ID的列表 -->
               <pluginGroups>
              </pluginGroups>

              <!-- 用于来配置不同的代理[简单的设置profile id可以很容易的更换整个代理配置] -->
              <proxies>
              </proxies>

             　<!-- 配置服务端的设置 -->
              <servers>
              </servers>

              <!-- 为仓库列表配置下载的镜像列表【只要本地仓库没有，就到指定公共仓库下载】-->
              <mirrors>
                <!-- 阿里云仓库 -->
                <mirror>
                  <id>alimaven</id>
                  <mirrorOf>repositoryId</mirrorOf>
                  <name>aliyun maven</name>
                  <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
                </mirror>
                <!--Maven仓库1-->
                <mirror>
                    <id>UK</id>
                    <mirrorOf>central</mirrorOf>
                    <url>http://uk.maven.org/maven2</url>
                </mirror>  
              </mirrors>

              <!-- 根据环境参数来调整构造配置的列表 -->
              <profiles>
              </profiles>

              <!-- activeProfiles
               | List of profiles that are active for all builds.
               |
              <activeProfiles>
                <activeProfile>alwaysActiveProfile</activeProfile>
                <activeProfile>anotherAlwaysActiveProfile</activeProfile>
              </activeProfiles>
              -->
            </settings>
```
#### 使用 Intellij IDEA （2016.3.5）
#### 配置 IDEA 环境
    - 设置 JDK（指定 Project Settings（项目配置）的 Project SDK + 编译环境）
    - 设置 Project Settings 的 Modules（指定测试文件目录 + Dependencies 添加相应 Tomcat 的 Export）
    - 设置 Maven（选择 Maven 版本 + settings.xml 配置文件 + 本地仓库地址）
    - 设置 Git（指定路径为 ...\git-for-windows\cmd\git.exe））
    - 部署 Tomcat（Tomcat 本地路径 + 初始路径 + 端口号 + Deployment 指定 项目 war exploded 包）
    - （可选）安装 JRebel （热部署插件）


---


## 二. 后台代码目录结构
```
【java 源码目录】src/main/java
└─org
    └─neusoft
        └─neubbs
            ├─constant           [常量包]
            │  ├─ajax                [ajax 量]
            │  ├─api                 [api 常量]
            │  ├─log                 [日志常量（存放 log4j 本地 打印 warn 信息）]
            │  └─secret              [加密常量（加密信息，密钥）]
            │
            ├─controller         [控制器]
            │  ├─annotation            [自定义注解]
            │  ├─api                   [api 控制器（此处主要编写接口，提供给前端）]
            │  ├─data                  [数据管理（例：动态切换数据源）]
            │  ├─filter                [过滤器]
            │  ├─handler               [处理器（解析器）]
            │  ├─interceptor           [拦截器]
            │  └─listener              [监听器]
            │
            ├─dao                [DAO 接口（根据 mapping.xml，封装 SQL 语句操作）]
            │
            ├─dto                [数据传输对象]
            │
            ├─entity             [实体类（对应数据表，领域对象）]
            │  ├─properties            [自定义配置文件实体类]
            ├─exception             [自定义异常]
            │
            ├─service            [业务包（存放业务接口 + 业务实现类）]
            │  └─impl                  [实现类]
            │
            └─util               [开发工具包（自主开发工具类，应用用于各个模块）]


**********************************************************

【配置文件目录】src/main/java
	│
	└─mapping  						[mybatis mapping.xml 数据库访问接口映射文件]
	│
    ├─ jdbc.properties  			[MySQL 数据库配置文件（本地源 + 云数据源）]  
    │
    ├─ log4j.properties 			[log4j.properties 配置文件（文件记录 DEBUG  + 控制台 DEBUG 信息 + 每日 WARN 汇总）]
    │
    ├─ neubbs.properties            [neubbs 项目配置文件（用户自动登陆时间，配置邮箱服务账号，邮箱验证 URL，用户自动登陆时间，ftp 连接信息，nginx 连接信息）]
    │
    ├─ rebel.xml                    [idae jrebel 热部署配置]
    │
    ├─ redis.properties             [Redis 数据库配置文件（云数据源）]
    │
    ├─ spring-context.xml           [Spring IoC 容器全局上下文配置文件]
    │ 
    ├─ spring-mvc.xml               [Spring MVC 配置文件]
    │ 
    ├─ spring-mybatis.context.xml   [整合 Spring + Mybatis 配置文件]
    │ 
    ├─ spring-redis-context.xml     [整合 Spring + Redis 配置文件]




**********************************************************

【测试目录（可放置测试脚本，目前只含 java jUnit 测试）】
src/test         
└─test
    └─org
        └─neusoft
            └─neubbs
                ├─api   [api 测试 （模拟 HTTP，未完善）]
                ├─db    [数据库层测试（主要针对 DAO 和 Service）]
                ├─log   [日志测试（目前只时简单测试，log4j 打印日志信息）]
                └─util  [工具类测试（测试开发工具包内工具）]

```

---

## 三. 开发约定
1. 函数名能见名知意（不怕长），函数注释规范，完整说明（功能，参数语义，返回值语义）
2. 类中不要包含 main() 函数，请在 test 目录下，编写 TestCase，并注释说明测试目的
3. IDEA 中，按住 Ctrl + (鼠标左键点击类 or 方法)，可直接跳到引用类中
4. 提交前请用 CheckStyle 检查代码规范
5. 自定义异常请用 @ApiException 声明（项目自定义异常），如果需打印日志，请实现 IPrintLog 接口
6. 已实现接口,请参考：[后端 API 交互协议](https://github.com/nuitcoder/neubbs/wiki/%E5%90%8E%E7%AB%AF-API-%E4%BA%A4%E4%BA%92%E5%8D%8F%E8%AE%AE)


---

## 四. 代码检查（Java 代码规范）
```
A.安装
    File -> Settings -> Plugins -> 输入 CheckStyle-IDEA -> Install

B.设置快捷键
    File -> Settings -> KeyMap -> 输入 CheckStyle
            <1>设置“Check Current File”
            <2>设置“Check Project”
    
C.导入 Neubbs java CheckStyle.xml（代码检查配置文件）
    File -> Settings -> Other Settings -> CheckStyle
            -> 右界面，上部分，点击右边 '+' 号
            -> 弹窗界面，File，Browse 选择 “Neubbs Java 代码检查.xml”，顶部输入 Description，Next，Finsh
            -> 右界面，Scan Scope，设置设置为 “Only Java Sources（but not tests）”
    
D.使用
    - 文件检查：编辑代码窗体 -> 右键 Check Current File
    - 项目整体检查：
        <1>底部状态栏 -> Check Style 界面 -> 选择 Rules -> 点击相应按钮
        <2>“Check Project” 快捷键
```
