package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.entity.properties.NeubbsConfigDO;
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
    public String getActivationMailContent(String activateUrl, String token) {
        // if activateUrl == null, use 'neubbs.proerties' default value
        if (activateUrl == null) {
            activateUrl = neubbsConfig.getAccountApiVaslidateUrl();
        }

        return StringUtil.generateActivationMailHtmlContent(activateUrl + token);
    }

    @Override
    public String getPasswordChangeMailContent(String email, String tempRandomPassword) {
        return  email + " 邮箱用户临时密码为：" + tempRandomPassword + " ，请登陆后尽快修改密码！";
    }

    @Override
    public void send(String sendNickname, String receiveEmail, String sendSubject, String sendEmailContent) {
        taskExecutor.execute(
                () -> SendEmailUtil.send(sendNickname, receiveEmail, sendSubject, sendEmailContent)
        );
    }
}
