package org.neusoft.neubbs.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            cookie.setMaxAge(60 * 60 * 24 * 7);
            cookie.setPath("/");
            cookie.setHttpOnly(true);

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
                cookie.setMaxAge(0);//周期设为0,即是删除
                cookie.setPath("/");
                cookie.setHttpOnly(true);

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
            System.out.println((count++) + " : "
                                + "CookieName = " + cookie.getName()
                                + ";\tCookieValue = " + cookie.getValue());
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
