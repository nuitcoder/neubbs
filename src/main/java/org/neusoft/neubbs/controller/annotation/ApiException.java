package org.neusoft.neubbs.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Api 异常注解（@ApiException）
 *      - 标记异常，为 neubbs 项目自定义异常
 *
 * @author Suvan
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiException { }
