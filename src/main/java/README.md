# Neubbs 后端

## 目录
+ 一. 开发约定
+ 二. 后端代码基础架构
    - java 源码目录
    - 配置文件目录
    - 测试目录
+ 三. 环境搭建
    - 数据库环境
        - MySQL 数据库
        - Redis 缓存数据库
    - 搭建服务器
        - Tomcat 应用服务器
        - FTP 文件服务器
        - Nginx 代理服务器
    - 开发环境
        - 安装 JDK
        - 安装 Maven
        - 安装 Intellij IDEA

---

## 一. 开发约定
1. 函数名能**见名知意**，函数注释规范，javadoc 请详细说明（函数用途功能，参数语义，返回值语义）
2. 类中请不要包含 `main()` 函数（单元测试请在 test 目录下，编写测试用例，并附带良好的注释）
3. IDEA 中，光标移动到指定位置（类名，函数名，变量类型，变量名），**Ctrl + b**，可直接调到引用类
4. 提交前请根据 'Neubbs Java CheckStyle.xml' 代码规约文件，配合 'CheckStyle' 插件检查代码规范
5. 若需自定义异常请用 @ApiException 声明类，如果需打印日志，请实现 IPrintLog 接口
6. 已实现接口,请参考：[后端 API 交互协议](https://github.com/nuitcoder/neubbs/wiki/%E3%80%90%E5%90%8E%E7%AB%AF%E3%80%91-API-%E4%BA%A4%E4%BA%92%E5%8D%8F%E8%AE%AE)
7. 配置**代码规范**检查 'CheckStyle' 插件
```
A.安装
    File -> Settings -> Plugins 
         -> 输入 CheckStyle-IDEA -> Install

B.设置快捷键
    File -> Settings -> KeyMap -> 输入 CheckStyle
        <1>设置“Check Current File”
        <2>设置“Check Project”
    
C.导入 Neubbs java CheckStyle.xml
    File -> Settings -> Other Settings -> CheckStyle
         -> 主界面，点击上窗体右边的 '+' 号
         -> 弹窗界面 File 栏目，点击 Browse 选择 “Neubbs Java 代码检查.xml”，顶部输入 Description -> Next -> Finsh
         -> 主界面 Scan Scope 设置为 “Only Java Sources（but not tests）”
    
D.使用
    1. 当前文件检查：
        编辑代码窗体 -> 右键弹出列表 -> Check Current File
    Z.整个项目 项目整体检查：
        <1>底部状态栏 -> CheckStyle 栏目 
                     -> 底部弹起界面，选择相应的 Rules = "Neubbs Java CheckStyle.java" 
                     -> 点击绿色播放键按钮执行
        <2>直接在 KeyMap 设置 "Check Project" 的快捷键
```

## 二. 后台代码基础架构
### java 源码目录
```
src/main/java
│
└─ org
    └─ neusoft
        └─ neubbs
            ├─ constant           [常量包]
            │  ├─ ajax                [异步请求常量（API 响应）包]
            │  ├─ api                 [API 常量包]
            │  ├─ log                 [日志枚举常量（存放 log4j 本地打印的日志警告信息）包]
            │
            ├─ controller         [控制层包]
            │  ├─ annotation            [自定义注解包]
            │  ├─ api                   [API 控制层核心代码（提供接口与前端交互）包]
            │  ├─ filter                [过滤器包]
            │  ├─ handler               [处理器包（自定义处理器）]
            │  ├─ interceptor           [拦截器包]
            │  └─ listener              [监听器包]
            │
            ├─ dao                [数据访问对象包（DAO 模型）]
            │
            ├─ dto                [数据传输对象包（DTO 模型）]
            │
            ├─ entity             [领域对象包（DO 模型，实体类）]
            │  ├─ properties            [自定义配置文件包]
            │
            ├─ exception          [自定义异常包]
            │
            ├─ service            [业务层包]
            │  └─ impl                  [业务接口实现包]
            │
            └─ util               [自定义工具包（提供给业务层，进行调用处理）]
```

### 配置文件目录
```
src/main/resources
│
├─ mapping  					[存放数据访问对象（DAO 接口）映射文件（mapping.xml）目录]
│
├─ jdbc.properties  			[MySQL 数据库配置文件（本地源 + 云数据源）]  
│
├─ log4j.properties 			[log4j 日志处理配置文件]
│                                   - 项目记录 DEBUG  
│                                   - 控制台 WARN 信息 + 文件存储 WARN 信息
│
├─ neubbs.properties            [neubbs 自定义项目配置文件]
│                                   - 可配置账户 Cookie 保存时间（自动登陆）
│                                   - 文件上传路径 
│                                   - 激活账户验证 URL
│                                   - 首页显示话题基本信息默认记录数
│                                   - 配置邮箱服务账号和授权码（执行发送）
│                                   - FTP 文件服务器连接嘻嘻你
│                                   - Nginx 代理服务器 URL
│
├─ rebel.xml                    [JRebel 热部署配置自动生成文件（无需配置）]
│
├─ redis.properties             [Redis 缓存数据库配置文件]
│
├─ spring-context.xml           [Spring IoC 容器全局上下文配置文件]
│                                   - 注入数据库配置文件
│                                   - 注册 Bean
│                                       - 自动扫描（自动注册相应 @Controller, @Service, @Repository）
│                                       - 自定义项目配置文件
│                                       - Spring 连接池
│                                       - 文件上传解析器
│                                       - 全局异常处理器
│                                       - Kaptcha 验证码
│                                       - AOP 事务配置
│                                       - TX 横切事务管理器
│ 
├─ spring-mvc.xml               [Spring MVC 配置文件]
│                                   - mvc 配置
│                                   - 配置试图解析器
│                                   - 配置 JSON 转换器
│                                   - 配置拦截器
│ 
├─ spring-mybatis.context.xml   [整合 Spring + Mybatis 配置文件]
│ 
└─ spring-redis-context.xml     [整合 Spring + Redis 配置文件]
```

### 测试目录
```
src/test     
│
└─ org
   └─ neusoft
      └─ neubbs
         ├─ api   [api 测试包（Mock HTTP Request）]
         ├─ dao   [数据库访问对象（DAO）测试包]
         ├─ data  [测试数据包（存放测试脚本）]
         └─ util  [工具类测试包]
```

---

## 三. 环境搭建

### 数据库环境

#### MySQL 数据库
1. 本地搭建 MySQL，[官网](https://www.mysql.com/cn/downloads/)，建议版本（5.7）
2. 部署数据库（可利用数据库库管理工具 Navicat Prenmium）
    - 执行初始化数据库，执行 `src\main\InitNeubbsForumDatabase.sql` 脚本
    - （可选）插入测试数据，执行 `src\main\InsertTestData.sql` 脚本

#### Redis 缓存数据库
1. 本地搭建 Redis，[官网](https://redis.io/)，建议版本（3.2.100）
2. 配置连接参数 `src\main\resources\redis.properties`


### 服务器环境

#### Tomcat 应用服务器
1. 本地搭建 Tomcat，[官网](http://tomcat.apache.org/)，建议版本（apache-tomcat-8.0.41）


#### FTP 文件服务器
1. 本地搭建 FTP，Window 即可开启 FTP服务，也可以使用工具例：Filezilla-[官网](https://filezilla-project.org/)
2. 配置连接参数 `src\main\resources\neubbs.properties`

#### Nginx 代理服务器
1. 本地搭建 Nginx，[官网](https://nginx.org/)，建议版本（1.13.7）
2. 配置连接 URL `src\main\resources\neubbs.properties`
3. 服务器配置，服务器上修改配置文件 `..\conf\nginx.conf`
```
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
```

### 开发环境
#### 安装 JDK
- [官网下载](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)，建议版本（1.8.0_65）

#### 安装 Maven
1. 安装 Maven，[官网下载](https://maven.apache.org/download.cgi)，建议版本(apache-maven-3.3.9)
2. 定义配置文件 `...\Maven\apache-maven-3.3.9\conf\settings.xml`
```
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
 
  <!--
    1.全局配置：${M2_HOME}/conf/settings.xml （对操作系统的所有用户生效）
    2.用户配置：${user.home}/.m2/settings.xml
    
    【注意】若两者都进行配置，内容合并，当前用户的 settings.xml 会覆盖全局的 settings.xml
  -->
  <!-- 本地仓库（存放各种依赖 jar 包） -->
  <localRepository>E:\Java\Maven\repository</localRepository>

  <!-- 远程 Maven 仓库配置（若项目的 pom.xml 配置依赖，本地仓库不存在，即去公共仓库下载）-->
  <mirrors>
    <!-- 阿里云仓库 -->
    <mirror>
      <id>alimaven</id>
      <mirrorOf>repositoryId</mirrorOf>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </mirror>
    
    <!--Maven 官方中央仓库-->
    <mirror>
        <id>UK</id>
        <mirrorOf>central</mirrorOf>
        <url>http://uk.maven.org/maven2</url>
    </mirror>  
  </mirrors>

  <!-- 插件分组列表 -->
  <pluginGroups>
  </pluginGroups>

  <!-- 代理列表（设置 profile id 可以快速在多个代理配置之间切换） -->
  <proxies>
  </proxies>

  <!-- 服务器列表 -->
  <servers>
  </servers>
  
  <!-- 构造配置列表 -->
  <profiles>
  </profiles>

</settings>
```

#### 安装 Intellij IDEA
1. 本地安装 Intellij IDEA，[官网](), 建议版本（ULTIMATE 2016.03 ，及以上）
2. 配置开发环境
```
a. 设置 JDK
    - 打开 Project Structure（快捷键：Ctrl + Shift + Alt + S）
    - 设置 Project Settings -> Project SDK
    
b. 设置项目依赖
    - Prject Structure -> Project Settings -> Modules
        - 指定测试文件目录
        - 在 Dependencies 添加相应 Tomcat 的 Export  依赖）
        
c. 设置 Maven
    - 选择 Maven 版本，指定 settings.xml 配置文件 + 设置本地仓库地址
    
d. 设置 Git
    - 指定路径为 ...\git-for-windows\cmd\git.exe
    
e. 部署 Tomcat
    - Tomcat 本地路径 + 初始路径 + 端口号 + Deployment 指定项目 war exploded 包
    
f. （可选）安装 JRebel （热部署插件）
```


