package org.neusoft.neubbs.constant;

/**
 * Cookie 信息
 */
public interface CookieInfo {
    Integer EXPIRETIME_SERVEN_DAY = 604800;     //60 * 60 * 24 * 7 s, Cookie 过期时间
    Integer EXPIRETIME_ZERO  = 0;               //0

    String PATH = "/";                      //指明项目所有路径有效

    Boolean HTTPONLY_TRUE = true;           //HttpOnly设置true
    Boolean HTTPONLY_FALSE = false;         //HttpOnly设置false
}