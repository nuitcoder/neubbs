package org.neusoft.neubbs.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  管理员权限注解 @AdminRank
 */
@Target(ElementType.METHOD) //修饰方法
@Retention(RetentionPolicy.RUNTIME) //运行时注解
public @interface AdminRank {}