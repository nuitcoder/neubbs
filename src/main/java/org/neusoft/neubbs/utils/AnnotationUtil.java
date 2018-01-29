package org.neusoft.neubbs.utils;

import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解工具类
 *      - 方法是否存在指定注解
 *      - 类是否存在指定注解r
 *
 * @author Suvan
 */
public final class AnnotationUtil {

    private AnnotationUtil() { }

    /**
     * 方法是否存在指定注解
     *      - 针对方法级
     *      - 拦截器
     *
     * @param handler 方法对象（拦截器内的 Object handler）
     * @param c 注解的运行时Class对象
     * @return boolean 存在结果（true-存在，false-不存在）
     */
    public static boolean hasMethodAnnotation(Object handler, Class c) {
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Annotation annotation = method.getAnnotation(c);

        return annotation != null;
    }

    /**
     * 类是否存在指定注解
     *      - 针对类，接口级
     *
     * @param objClass 指定类对象
     * @param annotationClass 指定注解的Class对象
     * @return boolean 存在结果（true-存在，false-不存在）
     */
    public static boolean hasClassAnnotation(Class objClass, Class annotationClass) {
        Annotation annotation = objClass.getAnnotation(annotationClass);
        return annotation != null;
    }
}
