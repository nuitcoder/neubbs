package org.neusoft.neubbs.service;

import java.awt.image.BufferedImage;

/**
 * 验证码业务接口
 *      - 获取验证码文本
 *      - 获取验证码图片
 *      - 校验（验证码）
 *
 * @author Suvan
 */
public interface ICaptchaService {

    /**
     * 获取验证码文本
     *      - 随机生成验证码（目前是使用 Kaptcha 库，在 spring-context.xml 内配置 DefaultKaptcha 对象）
     *      - 5 位字符长度数字
     *
     * @return String 验证码文本字符串
     */
    String getCaptchaText();

    /**
     * 获取验证码图片
     *      - 根据验证码文本，生成具备干扰线的图片（同样使用 Kaptcha，在 spring-context.xml 内配置）
     *
     * @param captchaText 验证码文本
     * @return BufferedImage 验证码图片流
     */
    BufferedImage getCaptchaImage(String captchaText);

    /**
     * 校验（验证码）
     *      - 判断验输入验证码，是否匹配已生成的 session 验证码
     *
     * @param inputCaptcha 输入验证码
     * @param sessionCaptcha session 验证码
     */
    void validate(String inputCaptcha, String sessionCaptcha);
}
