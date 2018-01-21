package org.neusoft.neubbs.service;

import java.awt.image.BufferedImage;

/**
 * Http 服务接口
 *      - Response
 *          - 设置验证码图片类型响应报头
 *          - 输出验证码图片
 *      - Cookie
 *          - 保存 Cookie
 *          - 删除 Cookie
 *          - 获取 Cookie 值
 *          - 保存认证 Cookie
 *          - 获取认证 Cookie 的值
 *          - 获取用户登陆状态
 *      - Session
 *          - 保存验证码文本
 *          - 获取验证码文本
 *          - 销毁当前 session
 *      - Application（Servlet Context）
 *          - 增加在线登陆用户人数（+1）
 *          - 减少在线登陆用户人数（-1）
 *          - 获取在线登陆人数
 *
 * 【注意】http 参数 request, response, cookie, session, context  运用 ThreadLocal 技术隐藏
 *          - ApiFilter.java 存入，PublicParamsUtil.java 取出
 *
 * @author Suvan
 */
public interface IHttpService {

    /*
     * ***********************************************
     * Response method
     * ***********************************************
     */

    /**
     * 设置验证码图片类型响应报头
     *      - 验证码图片页面的 response.header
     */
    void setCaptchaImageTypeResponseHeader();

    /**
     * 输出验证码图片
     *      - jpg 格式图片
     *
     * @param captchaImage 验证码图片
     */
    void outputCaptchaImage(BufferedImage captchaImage);

    /*
     * ***********************************************
     * Cooke method
     * ***********************************************
     */

    /**
     * 保存 Cookie
     *
     * @param cookieName cookie名
     * @param cookieValue cookie值
     */
    void saveCookie(String cookieName, String cookieValue);

    /**
     * 删除 Cookie
     *
     * @param cookieName Cookie名
     */
    void removeCookie(String cookieName);

    /**
     * 获取 Cookie 值
     *
     * @param cookieName Cookie名
     * @return String Cookie值
     */
    String getCookieValue(String cookieName);

    /**
     * 保存认证 Cookie
     *      - JWT 加密密文（ISecretService 加密）
     *      - 包含用户信息（UserDO[id, name, rank, state]）
     *      - Cookie，key=authentication, value=输入参数
     *
     * @param authentication 认证加密信息（密文）
     */
    void saveAuthenticationCookie(String authentication);

    /**
     * 获取认证 Cookie 的值
     *      - key=authentication, 获取 value
     *
     * @return String 认证加密信息（密文）
     */
    String getAuthenticationCookieValue();

    /**
     * 获取用户登陆状态
     *
     * @return boolean 用户登陆状态（true-已登录，false-未登录）
     */
    boolean isUserLoginState();

    /*
     * ***********************************************
     * Session method
     * ***********************************************
     */

    /**
     * 保存验证码文本
     *      - 保存至 session
     *      - 当前用户的验证码
     *      - 属性键值对: key=captcha, value= 输入参数
     *
     * @param captchaText 将存储验证码文本
     */
    void saveCaptchaText(String captchaText);

    /**
     * 获取验证码文本，来自 session
     *      - 从 session 获取
     *      - 当前用户的验证码
     *      - 属性键值对：key=captcha, 获取 value
     *
     * @return String 取出的验证码文本
     */
    String getCaptchaText();

    /**
     * 销毁当前 session
     *      - 当前线程用户的 session
     */
    void destroySession();

    /*
     * ***********************************************
     * Application(Servlet Context) method
     * ***********************************************
     */

    /**
     * 增加在线登陆用户人数（+1）
     */
    void increaseOnlineLoginUserNumber();

    /**
     * 减少在线登陆用户人数（-1）
     */
    void decreaseOnlineLoginUserNumber();

    /**
     * 获取在线登录人数
     *
     * @return int 在线登录人数
     */
    int getOnlineLoginUserNumber();
}
