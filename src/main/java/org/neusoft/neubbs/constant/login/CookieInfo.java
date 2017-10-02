package org.neusoft.neubbs.constant.login;

/**
 * Cookie信息
 */
public interface CookieInfo {
    int EXPIRETIME_SERVEN_DAY = 604800;     //60 * 60 * 24 * 7 s, Cookie 过期时间
    int EXPIRETIME_ZERO  = 0;               //0

    String PATH = "/";                      //路径

    boolean HTTPONLY_TRUE = true;           //HttpOnly设置true
    boolean HTTPONLY_FALSE = false;         //HttpOnly设置false
}