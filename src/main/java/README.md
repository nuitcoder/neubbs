## Neubbs 后端

### 开发约定
1. 函数名能见名知意（不怕长），函数注释规范，完整说明（功能，参数语义，返回值语义）
2. 类中不要包含 main() 函数，请在 test 目录下，编写 TestCase，并注释说明测试目的
3. IDEA 中，按住 Ctrl + (鼠标左键点击类 or 方法)，可直接跳到引用类中
4. 提交前请用 CheckStyle 检查代码规范
5. 自定义异常请用 @ApiException 声明（项目自定义异常），如果需打印日志，请实现 IPrintLog 接口
6. 已实现接口,请参考：[后端 API 交互协议](https://github.com/nuitcoder/neubbs/wiki/%E5%90%8E%E7%AB%AF-API-%E4%BA%A4%E4%BA%92%E5%8D%8F%E8%AE%AE)


### 代码检查
```
安装
    File -> Settings -> Plugins -> 输入 CheckStyle-IDEA -> Install

设置快捷键
    File -> Settings -> KeyMap -> 输入 CheckStyle
            <1>设置“Check Current File”
            <2>设置“Check Project”
    
导入 Neubbs 代码检查.xml
    File -> Settings -> Other Settings -> CheckStyle
    -> 右界面，上部分，点击右边 '+' 号
    -> 弹窗界面，File，Browse 选择 “Neubbs Java 代码检查.xml”，顶部输入 Description，Next，Finsh
    -> 右界面，Scan Scope，设置设置为 “Only Java Sources（but not tests）”
    
使用
    文件检查：编辑代码窗体 -> 右键 Check Current File
    项目整体检查：
        <1>底部状态栏 -> Check Style 界面 -> 选择 Rules -> 点击相应按钮
        <2>“Check Project” 快捷键
```


## 后台代码目录结构
```

【java源码目录】src/main/java
└─org
    └─neusoft
        └─neubbs
            ├─constant           [常量包]
            │  ├─ajax                [ajax常量]
            │  ├─api                 [api常量]
            │  ├─log                 [日志常量（存放 log4j 本地 打印 warn 信息）]
            │  └─secret              [加密常量（加密信息，密钥）]
            ├─controller         [控制器]
            │  ├─annotation            [自定义注解]
            │  ├─api                   [api 控制器（此处主要编写接口，提供给前端）]
            │  ├─exception             [自定义异常]
            │  ├─filter                [过滤器]
            │  ├─handler               [处理器（解析器）]
            │  ├─interceptor           [拦截器]
            │  └─listener              [监听器]
            ├─dao                [DAO接口（根据 mapping.xml，封装 SQL 语句操作）]
            ├─dto                [数据传输对象]
            ├─entity             [实体类（对应数据表，领域对象）]
            ├─service            [业务包（存放业务接口 + 业务实现类）]
            │  └─impl                  [实现类]
            └─util               [开发工具包（自主开发工具类，应用用于各个模块）]

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

