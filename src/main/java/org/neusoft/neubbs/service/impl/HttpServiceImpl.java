package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.entity.properties.NeubbsConfigDO;
import org.neusoft.neubbs.exception.AccountErrorException;
import org.neusoft.neubbs.service.IHttpService;
import org.neusoft.neubbs.utils.CookieUtil;
import org.neusoft.neubbs.utils.PublicParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * IHttpService 实现类
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
    public void saveCookie(String cookieName, String cookieValue) {
        //get cookie max day in neubbs.properties
        CookieUtil.saveCookie(PublicParamsUtil.getResponse(), cookieName,
                cookieValue, neubbsConfig.getCookieAutoLoginMaxAgeDay());
    }

    @Override
    public void saveAuthenticationCookie(String authentication) {
       this.saveCookie(ParamConst.AUTHENTICATION, authentication);
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
    public String getAuthenticationCookieValue() {
        return CookieUtil.getCookieValue(PublicParamsUtil.getRequest(), ParamConst.AUTHENTICATION);
    }

    @Override
    public boolean isLoggedInUser() {
        return this.getAuthenticationCookieValue() != null;
    }

    @Override
    public void incOnlineLoginUserNumber() {
        ServletContext context = PublicParamsUtil.getContext();
        context.setAttribute(ParamConst.LOGIN_USER, this.getOnlineLoginUserNumber() + 1);
    }

    @Override
    public void decOnlineLoginUserNumber() {
        ServletContext context = PublicParamsUtil.getContext();
        context.setAttribute(ParamConst.LOGIN_USER, this.getOnlineLoginUserNumber() - 1);
    }

    @Override
    public int getOnlineVisitUserNumber() {
        return (int) PublicParamsUtil.getContext().getAttribute(ParamConst.VISIT_USER);
    }

    @Override

    public int getOnlineLoginUserNumber() {
        return (int) PublicParamsUtil.getContext().getAttribute(ParamConst.LOGIN_USER);
    }

    @Override
    public void setPageResponseHeaderToImageType() {
        HttpServletResponse response = PublicParamsUtil.getResponse();
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setContentType("image/jpeg");
    }

    @Override
    public void outputPageImageJPGFormat(BufferedImage outputImage) {
        try {
            ServletOutputStream out = PublicParamsUtil.getResponse().getOutputStream();
            ImageIO.write(outputImage, "jpg", out);

            out.flush();
            out.close();
        } catch (IOException e) {
            throw new AccountErrorException(ApiMessage.GENERATE_CAPTCHA_FAIL).log(LogWarn.USER_17);
        }
    }

    @Override
    public String getSessionCaptchaText() {
        return (String) PublicParamsUtil.getSession().getAttribute(SetConst.SESSION_CAPTCHA);
    }

    @Override
    public void setSessionToSaveCaptchaText(String captchaText) {
        PublicParamsUtil.getRequest().setAttribute(SetConst.SESSION_CAPTCHA, captchaText);
    }
    @Override

    public void destroySession () {
        PublicParamsUtil.getSession().invalidate();
    }
}
