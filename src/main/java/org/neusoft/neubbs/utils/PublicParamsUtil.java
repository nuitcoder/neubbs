package org.neusoft.neubbs.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 公共参数工具类
 *      - 设置 http 请求
 *      - 设置 http 响应
 *
 *      - 获取 http 请求
 *      - 获取 http 响应
 *      - 获取 session
 *      - 获取上下文 context
 *
 *      - 清空所有信息
 *
 * 【注意】该工具类使用 ThreadLocal （线程局部对象）保存变量，隐藏参数的同时，确保线程安全
 *
 * @author Suvan
 */
public final class PublicParamsUtil {

    private PublicParamsUtil() { }

    private static ThreadLocal<HttpServletRequest> requestTL = new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> responseTL = new ThreadLocal<>();

    /**
     * 设置线程局部变量
     *      - 设置 http 请求
     *      - 设置 http 响应
     */
    public static void setRequest(HttpServletRequest request) {
       requestTL.set(request);
    }
    public static void setResponse(HttpServletResponse response) {
       responseTL.set(response);
    }

    /**
     * 获取线程局部变量
     *      - 获取 http 请求
     *      - 获取 http 响应
     *      - 获取 session
     *      - 获取上下文 context
     */
    public static HttpServletRequest getRequest() {
        return requestTL.get();
    }
    public static HttpServletResponse getResponse() {
        return responseTL.get();
    }
    public static HttpSession getSession() {
        return requestTL.get().getSession();
    }
    public static ServletContext getContext() {
        return requestTL.get().getServletContext();
    }

    /**
     * 清空所有信息
     */
    public static void clearAllInfo() {
        requestTL.remove();
        responseTL.remove();
    }
}
