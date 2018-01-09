package org.neusoft.neubbs.service;

import java.awt.image.BufferedImage;

/**
 * Http 服务接口
 *
 * @author Suvan
 */
public interface IHttpService {

    /**
     * 【Cookie】保存 Cookie
     *
     * @param cookieName cookie名
     * @param cookieValue cookie值
     */
    void saveCookie(String cookieName, String cookieValue);

    /**
     * 【Cookie】保存认证 Cookie
     *      - 经过 SercretService （JWT 加密）后的 token
     *      - 包含用户信息（加密后的 UserDO[id, name, rank, state] 信息）
     *
     * @param authentication Cookie用户认证信息
     */
    void saveAuthenticationCookie(String authentication);

    /**
     * 【Cookie】删除 Cookie
     *
     * @param cookieName 要删除Cookie名
     */
    void removeCookie(String cookieName);

    /**
     * 【Cookie】获取 Cookie值
     *
     * @param cookieName Cookie名
     * @return String Cookie值
     */
    String getCookieValue(String cookieName);

    /**
     * 【Cookie】获取 Cookie 内的 key=Authentication 的值
     *
     * @return String Cookie的Authentication的值
     */
    String getAuthenticationCookieValue();

    /**
     * 【Cookie】判断用户是否已经登陆
     *
     * @return boolean 用户是否已登录（true-已登录，false-未登录）
     */
    boolean isLoggedInUser();

    /**
     * 【Application】增加在线登录人数（+1）
     *
     */
    void incOnlineLoginUserNumber();

    /**
     * 【Application】减少在线登录人数（-1）
     *
     */
    void decOnlineLoginUserNumber();

    /**
     * 【Application】获取在线登录人数
     *
     * @return int 在线登录人数
     */
    int getOnlineLoginUserNumber();

    /**
     * 【application】获取在线访问人数
     *
     * @return int 在线访问人数
     */
    int getOnlineVisitUserNumber();

    /**
     * 【Response】设置页面响应为图片类型
     *
     */
    void setPageResponseHeaderToImageType();

    /**
     * 【Response】输出页面图片（jpg 格式） *
     *
     * @param outputImage 输出图片流
     */
    void outputPageImageJPGFormat(BufferedImage outputImage);

    /**
     * 【Session】设置 Session 保存验证码文本
     *
     * @param captchaText 验证码文本
     */
    void setSessionToSaveCaptchaText(String captchaText);

    /**
     * 【Session】获取 Session 已经存在的验证码文本
     *
     * @return String Session内验证码文本
     */
    String getSessionCaptchaText();

    /**
     * 【Session】销毁当前 Session
     */
    void destroySession();
}
