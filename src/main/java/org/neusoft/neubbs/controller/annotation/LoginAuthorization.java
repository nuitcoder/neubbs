package org.neusoft.neubbs.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  登录认证注解（@LoginAuthorization）
 *      - 标记访问 api 的账户，需要登陆状态
 *
 *  @author Suvan
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginAuthorization { }
