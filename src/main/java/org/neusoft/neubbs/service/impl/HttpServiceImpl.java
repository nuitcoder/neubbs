package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.exception.AccountErrorException;
import org.neusoft.neubbs.entity.properties.NeubbsConfigDO;
import org.neusoft.neubbs.service.IHttpService;
import org.neusoft.neubbs.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
    public String getCookieValue(HttpServletRequest request, String cookieName) {
        return CookieUtil.getCookieValue(request, ParamConst.AUTHENTICATION);
    }

    @Override
    public void saveCookie(HttpServletResponse response, String cookieName, String cookieValue, Integer cookieMaxTime) {
        if (cookieMaxTime == null) {
            cookieMaxTime = neubbsConfig.getCookieAutoLoginMaxAgeDay();
        }
        CookieUtil.saveCookie(response, cookieName, cookieValue, cookieMaxTime);
    }

    @Override
    public void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        CookieUtil.removeCookie(request, response, cookieName);
    }

    @Override
    public void addOnlineLoginUserNumber(HttpServletRequest request) {
        ServletContext context = request.getServletContext();
        context.setAttribute(ParamConst.LOGIN_USER, this.getOnlineLoginUserNumber(request) + 1);
    }

    @Override
    public void cutOnlineLoginUserNumber(HttpServletRequest request) {
        ServletContext context = request.getServletContext();
        context.setAttribute(ParamConst.LOGIN_USER, this.getOnlineLoginUserNumber(request) - 1);
    }

    @Override
    public int getOnlineLoginUserNumber(HttpServletRequest request) {
        return (int) request.getServletContext().getAttribute(ParamConst.LOGIN_USER);
    }

    @Override
    public int getOnlineVisitUserNumber(HttpServletRequest request) {
        return (int) request.getServletContext().getAttribute(ParamConst.VISIT_USER);
    }

    @Override
    public void setPageResponseHearderToImageType(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
    }

    @Override
    public void outputPageImageToJPGFormat(HttpServletResponse response, BufferedImage outputImage) {
        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(outputImage, "jpg", out);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new AccountErrorException(ApiMessage.GENERATE_CAPTCHA_FAIL).log(LogWarn.ACCOUNT_17);
        }
    }

    @Override
    public void setSessionToSaveCaptchaText(HttpServletRequest request, String captchaText) {
        request.getSession().setAttribute(SetConst.SESSION_CAPTCHA, captchaText);
    }

    @Override
    public String getSessionCaptchaText(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(SetConst.SESSION_CAPTCHA);
    }
}
