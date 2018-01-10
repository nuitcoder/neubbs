package org.neusoft.neubbs.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 管理员权限注解（@AdminRank）
 *      - 标记访问 api 的账户，需具备管理员权限
 *  @author Suvan
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminRank { }
