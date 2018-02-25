package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.entity.properties.NeubbsConfigDO;
import org.neusoft.neubbs.exception.ServiceException;
import org.neusoft.neubbs.service.IEmailService;
import org.neusoft.neubbs.utils.SendEmailUtil;
import org.neusoft.neubbs.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * IEmailServiceImpl 实现类
 *
 * @author Suvan
 */
@Service("emailServiceImpl")
public class EmailServiceImpl implements IEmailService {

    private final ThreadPoolTaskExecutor taskExecutor;
    private final NeubbsConfigDO neubbsConfig;

    @Autowired
    public EmailServiceImpl(ThreadPoolTaskExecutor taskExecutor, NeubbsConfigDO neubbsConfig) {
        this.taskExecutor = taskExecutor;
        this.neubbsConfig = neubbsConfig;
    }


    @Override
    public void sendAccountActivateMail(String email, String emailToken) {
        String activateUrl = neubbsConfig.getAccountApiVaslidateUrl();
        if (activateUrl == null) {
           throw new ServiceException(ApiMessage.UNKNOWN_ERROR).log(LogWarnEnum.ES1);
        }
        String emailContent = StringUtil.generateActivateMailHtmlContent(activateUrl + emailToken);

        this.send(SetConst.EMAIL_SENDER_NAME, email, SetConst.EMAIL_SUBJECT_ACTIVATE, emailContent);
    }

    @Override
    public void sendResetTemporaryPasswordMail(String email, String temporaryRandomPassword) {
        String emailContent = email + " 邮箱用户临时密码为：" + temporaryRandomPassword + " ，请登陆后尽快修改密码！";
        this.send(
                SetConst.EMAIL_SENDER_NAME, email,
                SetConst.EMAIL_SUBJECT_TEMPORARY_PASSWORD, emailContent
        );
    }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /**
     * 发送邮件
     *      - 线程池另起线程发送邮件
     *
     * @param sendNickname 发送人昵称
     * @param receiveEmail 接收邮箱
     * @param sendSubject 发送主题
     * @param sendEmailContent 发送内容
     */
    public void send(String sendNickname, String receiveEmail, String sendSubject, String sendEmailContent) {
        taskExecutor.execute(
                () -> SendEmailUtil.send(sendNickname, receiveEmail, sendSubject, sendEmailContent)
        );
    }
}
