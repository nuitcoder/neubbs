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
     * （方法级）是否存在指定注解
     *
     * @param handler
     * @return Boolean （true 存在，false-不存在）
     */
    public static Boolean hasMethodAnnotation(Object handler, Class c){
         //不属于方法级跳过（默认不存在）
        if(!(handler instanceof HandlerMethod)){
            return false;
        }

        //获取注解
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        Annotation annotation = method.getAnnotation(c);
        if(annotation == null){
                return false;
        }

        //存在
        return true;
    }

    /**
     * （类 or 接口级）是否存在指定注解
     *
     * @param objClass 类的运行时 Class
     * @param annotationClass 指定注解类的 Class
     * @return Boolean
     */
    public static Boolean hasClassAnnotation(Class objClass, Class annotationClass){
        Annotation annotation = objClass.getAnnotation(annotationClass);
        if (annotation != null) {
            return true;
        }

        return false;
    }
}
