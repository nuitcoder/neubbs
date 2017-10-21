# Neubbs
东软校内 BBS

# 技术栈
## 前端
+ WebPack 1.13.2
+ React 15.4.0
+ React-Redux 5.0.6
+ React-Router 3.0.5
+ Styled-components 2.1.2

详细依赖见 [package.json](./src/main/webapp/package.json)

## 后端
+ JDK1.8.0_65
+ Tomcat-8.0.41
+ Maven-3.3.9
+ MySQL-5.7
+ Redis-3.0.503


# 目录结构
```
neubbs
├──README.md
├──pom.xml                                                                                                 
├──src                                                                                         
│  ├─main                                                                                            
│  │  ├─java                                                                                         
│  │  │  └─org                                                                                       
│  │  │      └─neusoft                                                                               
│  │  │          └─neubbs                                                                            
│  │  │              ├─constant                                                                                                            
│  │  │              │  ├─ajax         
│  │  │              │  ├─api         
│  │  │              │  ├─log         
│  │  │              │  ├─secret         
│  │  │              ├─controller            
│  │  │              │  ├─annotation         
│  │  │              │  ├─api                
│  │  │              │  ├─exception                
│  │  │              │  ├─filter             
│  │  │              │  ├─handler             
│  │  │              │  └─interceptor                                                                                                                
│  │  │              │  └─listener                                                                                                                
│  │  │              ├─dao                                                                           
│  │  │              ├─dto                                                                           
│  │  │              ├─entity                                                                        
│  │  │              ├─service                                                                       
│  │  │              │  └─impl                                                                       
│  │  │              └─util                                                                          
│  │  ├─resources
│  │  │  └─mapping
│  │  │  │  jdbc.properties
│  │  │  │  log4j.properties
│  │  │  │  redis.properties
│  │  │  │  spring-context.xml
│  │  │  │  spring-mvc.xml
│  │  │  │  spring-mybatis-context.xml
│  │  │  └─spring-redis-context.xml                                                             
│  │  ├─sql                                                                                          
│  │  └─webapp                                                                                       
│  │      ├─app                                                                                      
│  │      │  ├─components                                                                            
│  │      │  ├─layouts                                                                               
│  │      │  └─reducers                                                                              
│  │      ├─resources                                                                                
│  │      │  └─css                                                                                   
│  │      └─WEB-INF                                                                                  
│  │          └─jsp                                                                                  
│  └─test                                                                                                                                                                                                                          
                                                                                                     
```
