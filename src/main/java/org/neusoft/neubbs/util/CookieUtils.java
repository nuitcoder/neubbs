package org.neusoft.neubbs.util;

import org.neusoft.neubbs.constant.login.CookieInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie 工具类
 */
public class CookieUtils {
    /**
     * 插入Cookie
     * @param response
     * @param cookieName
     * @param cookieValue
     */
    public static void saveCookie(HttpServletResponse response,
                                    String cookieName,String cookieValue){
        Cookie cookie = new Cookie(cookieName,cookieValue);
            cookie.setMaxAge(CookieInfo.MAXAGE_SERVENDAY);
            cookie.setPath(CookieInfo.PATH);
            cookie.setHttpOnly(CookieInfo.HTTPONLY_TRUE);

        response.addCookie(cookie);
    }

    /**
     * 删除Cookie
     * @param request
     * @param response
     * @param cookieName
     */
    public static void removeCookie(HttpServletRequest request,HttpServletResponse response,
                                        String cookieName){
        for(Cookie cookie : request.getCookies()){
            if(cookieName.equals(cookie.getName())){
                cookie.setMaxAge(CookieInfo.MAXAGE_ZERO);//有效时间设为0,即是删除
                cookie.setPath(CookieInfo.PATH);
                cookie.setHttpOnly(CookieInfo.HTTPONLY_TRUE);

                response.addCookie(cookie);
                break;
            }
        }
    }

    /**
     * 打印客户端所有Cookie
     * @param request
     */
    public static void printCookie(HttpServletRequest request){
        int count = 1;
        for(Cookie cookie: request.getCookies()){
            System.out.println("\n" + (count++) + " : "
                                + cookie.getName() + "---"
                                + cookie.getValue());
        }
    }

    public static String getCookieValue(HttpServletRequest request,String cookieName){
        for(Cookie cookie: request.getCookies()){
            if(cookieName.equals(cookie.getName())){
                return cookie.getValue();
            }
        }

        return null;
    }

}
