package org.neusoft.neubbs.service;

import java.awt.image.BufferedImage;

/**
 * 验证码业务接口
 *
 * @author Suvan
 */
public interface ICaptchaService {

    /**
     * 获取验证码文本
     *
     * @return String 验证码文本字符串
     */
    String getCaptchaText();

    /**
     * 获取验证码图片
     *
     * @param captchaText 验证码文本
     * @return BufferedImage 验证码图片流
     */
    BufferedImage getCaptchaImage(String captchaText);

    /**
     * 判断输入验证码是否匹配 session 验证码
     *
     * @param inputCaptcha 输入验证码
     * @param sessionCaptcha session验证码
     */
    void judgeInputCaptchaWhetherSessionCaptcha(String inputCaptcha, String sessionCaptcha);
}
