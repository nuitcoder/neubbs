package org.neusoft.neubbs.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie 工具类
 *
 * @author Suvan
 */
public class CookieUtil {

    /**
     * 过期时间是（单位：s）
     */
    private final static Integer EXPIRETIME_THIRTY_DAY = 108000;
    private final static Integer EXPIRETIME_ZERO  = 0;

    private final static  String PATH = "/";

    private final static  Boolean HTTPONLY_TRUE = true;
    private final static  Boolean HTTPONLY_FALSE = false;


    /**
     * 保存 Cookie
     *
     * @param response http响应
     * @param cookieName Cookie名字
     * @param cookieValue Cookie值
     */
    public static void saveCookie(HttpServletResponse response,
                                    String cookieName,String cookieValue){
        Cookie cookie = new Cookie(cookieName,cookieValue);
            cookie.setMaxAge(EXPIRETIME_THIRTY_DAY);
            cookie.setPath(PATH);
            cookie.setHttpOnly(HTTPONLY_TRUE);

        response.addCookie(cookie);
    }

    /**
     * 删除Cookie（有效时间设为0,即表示删除）
     *
     * @param request http请求
     * @param response http响应
     * @param cookieName Cookie名字
     */
    public static void removeCookie(HttpServletRequest request,HttpServletResponse response,
                                        String cookieName){
        for(Cookie cookie : request.getCookies()){
            if(cookieName.equals(cookie.getName())){
                cookie.setMaxAge(EXPIRETIME_ZERO);
                cookie.setPath(PATH);
                cookie.setHttpOnly(HTTPONLY_TRUE);

                response.addCookie(cookie);
                break;
            }
        }
    }

    /**
     * 根据 Cookie 名，获取 Cookie 值
     *
     * @param request http请求
     * @param cookieName Cookie名
     * @return String Cookie值
     */
    public static String getCookieValue(HttpServletRequest request,String cookieName){
        for(Cookie cookie: request.getCookies()){
            if(cookieName.equals(cookie.getName())){
                return cookie.getValue();
            }
        }

        return null;
    }

    /**
     * 打印客户端所有 Cookie
     *
     * @param request
     */
    public static void printCookie(HttpServletRequest request){
        int count = 1;
        for(Cookie cookie: request.getCookies()){
            System.out.println("\n" + (count++) + " : " +cookie.getName() + "---" + cookie.getValue());
        }
    }
}
