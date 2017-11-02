package org.neusoft.neubbs.utils;

import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解工具类
 *
 * @author Suvan
 */
public final class AnnotationUtil {

    private AnnotationUtil() { }

    /**
     * （方法级）（拦截器）是否存在指定注解
     *
     * @param handler 方法对象（拦截器内的 Object handler）
     * @param c 注解的运行时Class
     * @return boolean （true 存在，false-不存在）
     */
    public static boolean hasMethodAnnotation(Object handler, Class c) {
         //不属于方法级跳过（默认不存在）
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }

        //获取注解
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Annotation annotation = method.getAnnotation(c);
        if (annotation == null) {
            return false;
        }

        //存在
        return true;
    }

    /**
     * （类 or 接口级）是否存在指定注解
     *
     * @param objClass 类.class
     * @param annotationClass 注解.lass
     * @return boolean 存在结果
     */
    public static boolean hasClassAnnotation(Class objClass, Class annotationClass) {
        Annotation annotation = objClass.getAnnotation(annotationClass);

        return annotation != null;
    }
}
