package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.entity.properties.NeubbsConfigDO;
import org.neusoft.neubbs.exception.ServiceException;
import org.neusoft.neubbs.service.IHttpService;
import org.neusoft.neubbs.utils.CookieUtil;
import org.neusoft.neubbs.utils.PublicParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * IHttpService 实现类
 *      - request, response 运用 ThreadLocal 隐藏（ApiFilter 存入，PublicParamsUtil 去出）
 *
 * @author Suvan
 */
@Service("httpServiceImpl")
public class HttpServiceImpl implements IHttpService {

    private final NeubbsConfigDO neubbsConfig;

    @Autowired
    public HttpServiceImpl(NeubbsConfigDO neubbsConfig) {
        this.neubbsConfig = neubbsConfig;
    }

    @Override
    public void setCaptchaImageTypeResponseHeader() {
        HttpServletResponse response = PublicParamsUtil.getResponse();
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setContentType("image/jpeg");
    }

    @Override
    public void outputCaptchaImage(BufferedImage captchaImage) {
        try {
            ServletOutputStream outputStream = PublicParamsUtil.getResponse().getOutputStream();

            ImageIO.write(captchaImage, "jpg", outputStream);

            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new ServiceException(ApiMessage.GENERATE_CAPTCHA_FAIL).log(LogWarnEnum.US17);
        }
    }

    @Override
    public void saveCookie(String cookieName, String cookieValue) {
        //get cookie max day in neubbs.properties
        CookieUtil.saveCookie(PublicParamsUtil.getResponse(), cookieName,
                cookieValue, neubbsConfig.getCookieAutoLoginMaxAgeDay());
    }

    @Override
    public void removeCookie(String cookieName) {
        CookieUtil.removeCookie(PublicParamsUtil.getRequest(), PublicParamsUtil.getResponse(), cookieName);
    }

    @Override
    public String getCookieValue(String cookieName) {
        return CookieUtil.getCookieValue(PublicParamsUtil.getRequest(), cookieName);
    }

    @Override
    public void saveAuthenticationCookie(String authentication) {
       this.saveCookie(ParamConst.AUTHENTICATION, authentication);
    }

    @Override
    public String getAuthenticationCookieValue() {
        return CookieUtil.getCookieValue(PublicParamsUtil.getRequest(), ParamConst.AUTHENTICATION);
    }

    @Override
    public boolean isUserLoginState() {
        return this.getAuthenticationCookieValue() != null;
    }

    @Override
    public void saveCaptchaText(String captchaText) {
        PublicParamsUtil.getSession().setAttribute(SetConst.SESSION_CAPTCHA, captchaText);
    }

    @Override
    public String getCaptchaText() {
        return (String) PublicParamsUtil.getSession().getAttribute(SetConst.SESSION_CAPTCHA);
    }

    @Override
    public void destroySession() {
        PublicParamsUtil.getSession().invalidate();
    }

    @Override
    public void increaseOnlineLoginUserNumber() {
        PublicParamsUtil.getContext().setAttribute(ParamConst.LOGIN_USER, this.getOnlineLoginUserNumber() + 1);
    }

    @Override
    public void decreaseOnlineLoginUserNumber() {
        PublicParamsUtil.getContext().setAttribute(ParamConst.LOGIN_USER, this.getOnlineLoginUserNumber() - 1);
    }

    @Override
    public int getOnlineLoginUserNumber() {
        return (int) PublicParamsUtil.getContext().getAttribute(ParamConst.LOGIN_USER);
    }
}
