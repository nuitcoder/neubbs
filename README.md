# Neubbs
东软校内 BBS

# 技术栈
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
│  │  │              ├─controller            
│  │  │              │  ├─annotation         
│  │  │              │  ├─api                
│  │  │              │  ├─filter             
│  │  │              │  └─interceptor                                                                                                                
│  │  │              ├─dao                                                                           
│  │  │              ├─dto                                                                           
│  │  │              ├─entity                                                                        
│  │  │              ├─service                                                                       
│  │  │              │  └─impl                                                                       
│  │  │              └─util                                                                          
│  │  ├─resources
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
