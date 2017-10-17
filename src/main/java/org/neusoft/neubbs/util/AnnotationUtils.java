package org.neusoft.neubbs.util;

import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解工具类
 *
 * @author Suvan
 */
public class AnnotationUtils {
    /**
     * 是否存在指定注解（检测方法对象，是否被指定注解声明）
     *
     * @param handler
     * @return Boolean （true 存在，false-不存在）
     */
    public static Boolean hasMethodAnnotation(Object handler, Class t){
         //不属于方法级跳过（默认不存在）
        if(!(handler instanceof HandlerMethod)){
            return false;
        }

        //获取注解
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        Annotation annotation = method.getAnnotation(t);
        if(annotation == null){
                return false;
        }

        //存在
        return true;
    }
}
