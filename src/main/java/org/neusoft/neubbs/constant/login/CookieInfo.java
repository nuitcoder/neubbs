package org.neusoft.neubbs.constant.login;

/**
 * Cookie信息
 */
public interface CookieInfo {
    int MAXAGE_SERVENDAY = 60 * 60 * 24 * 7; //有效时间7天
    int MAXAGE_ZERO  = 0;                    //0

    String DOMAIN = "";            //域名
    String PATH = "/";             //路径

    boolean HTTPONLY_TRUE = true;  //HttpOnly设置true
    boolean HTTPONLY_FALSE = false;//HttpOnly设置false
}