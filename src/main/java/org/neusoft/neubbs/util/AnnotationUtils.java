package org.neusoft.neubbs.util;

import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解工具类
 */
public class AnnotationUtils {
    /**
     * 是否存在指定注解（检测方法对象，是否被指定注解声明）
     * @param handler
     * @return
     */
    public static Boolean hasMethodAnnotation(Object handler,Class t){
         //不是方法级跳过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        //获取注解
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        Annotation annotation = method.getAnnotation(t);
        if(annotation != null){
                return true;
        }

        return false;
    }
}
