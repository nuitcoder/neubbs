package org.neusoft.neubbs.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 管理员权限注解 @AdminRank
 *      1.修饰方法
 *      2.运行时注解
 *
 *  @author Suvan
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminRank {}