package org.neusoft.neubbs.utils;

import org.neusoft.neubbs.constant.api.SetConst;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie 工具类
 *
 * @author Suvan
 */
public final class CookieUtil {

    private CookieUtil() { }

    /**
     * 保存 Cookie
     *
     * @param response http响应
     * @param cookieName Cookie名字
     * @param cookieValue Cookie值
     * @param maxAge 设置 Cookie 过期时间
     */
    public static void saveCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
            cookie.setMaxAge(maxAge);
            cookie.setPath("/");
            cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }

    /**
     * 删除Cookie（有效时间设为0,即表示删除）
     *
     * @param request http请求
     * @param response http响应
     * @param cookieName Cookie名字
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                cookie.setMaxAge(SetConst.EXPIRETIME_S_ZERO);
                cookie.setPath("/");
                cookie.setHttpOnly(true);

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
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        for (Cookie cookie: request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
