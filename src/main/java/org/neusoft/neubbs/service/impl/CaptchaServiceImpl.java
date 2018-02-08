package org.neusoft.neubbs.service.impl;

import com.google.code.kaptcha.Producer;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.exception.ServiceException;
import org.neusoft.neubbs.service.ICaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * ICaptchaService 实现类
 *
 * @author Suvan
 */
@Service("captchaServiceImpl")
public class CaptchaServiceImpl implements ICaptchaService {

    private final Producer captchaProducer;

    @Autowired
    public CaptchaServiceImpl(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @Override
    public String getCaptchaText() {
        return captchaProducer.createText();
    }

    @Override
    public BufferedImage getCaptchaImage(String captchaText) {
        return captchaProducer.createImage(captchaText);
    }

    @Override
    public void compareCaptcha(String inputCaptcha, String sessionCaptcha) {
        //no generate captcha, session does not exist
        if (sessionCaptcha == null) {
            throw new ServiceException(ApiMessage.NO_GENERATE_CAPTCHA).log(LogWarnEnum.US8);
        }

        //captcha does not match
        if (!inputCaptcha.equals(sessionCaptcha)) {
            throw new ServiceException(ApiMessage.CAPTCHA_INCORRECT).log(LogWarnEnum.US9);
        }
    }
}
