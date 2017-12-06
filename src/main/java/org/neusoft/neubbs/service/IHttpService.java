package org.neusoft.neubbs.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * Http 服务接口
 *
 * @author Suvan
 */
public interface IHttpService {

    /**
     * 获取 Cookie值
     *
     * @param request http请求
     * @param cookieName Cookie名
     * @return String Cookie值
     */
    String getCookieValue(HttpServletRequest request, String cookieName);

    /**
     * 保存 Cookie
     *
     * @param response http响应
     * @param cookieName cookie名
     * @param cookieValue cookie值
     * @param cookieMaxTime cookie最大时间
     */
    void saveCookie(HttpServletResponse response, String cookieName,
                            String cookieValue, Integer cookieMaxTime);

    /**
     * 删除 Cookie
     *
     * @param request http请求
     * @param response http响应
     * @param cookieName 要删除Cookie名
     */
    void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName);

    /**
     * 增加在线登录人数（+1）
     *
     * @param request http请求
     */
    void addOnlineLoginUserNumber(HttpServletRequest request);

    /**
     * 减少在线登录人数（-1）
     *
     * @param request http请求
     */
    void cutOnlineLoginUserNumber(HttpServletRequest request);

    /**
     * 获取在线登录人数
     *
     * @param request http请求
     * @return int 在线登录人数
     */
    int getOnlineLoginUserNumber(HttpServletRequest request);

    /**
     * 获取在线访问人数
     *
     * @param request http 请求
     * @return int 在线访问人数
     */
    int getOnlineVisitUserNumber(HttpServletRequest request);

    /**
     * 设置页面响应为图片类型
     *
     * @param response http响应
     */
    void setPageResponseHearderToImageType(HttpServletResponse response);

    /**
     * 输出页面图片（jpg 格式）
     *
     * @param response http响应
     * @param outputImage 输出图片流
     */
    void outputPageImageToJPGFormat(HttpServletResponse response, BufferedImage outputImage);

    /**
     * 设置 Session 保存验证码文本
     *
     * @param request http请求
     * @param captchaText 验证码文本
     */
    void setSessionToSaveCaptchaText(HttpServletRequest request, String captchaText);

    /**
     * 获取 Session 已经存在的验证码文本
     *
     * @param request http请求
     * @return String Session内验证码文本
     */
    String getSessionCaptchaText(HttpServletRequest request);
}
