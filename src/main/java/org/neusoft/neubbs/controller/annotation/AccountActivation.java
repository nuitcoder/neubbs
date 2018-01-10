package org.neusoft.neubbs.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 账户激活注解（@AccountActivation）
 *      - 标记访问 api 的账户，需要激活状态
 *
 * @author Suvan
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountActivation { }
