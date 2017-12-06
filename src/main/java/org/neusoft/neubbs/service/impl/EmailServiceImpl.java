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
    public String getEmailContentxtToActivateUserMailHtml(String activateUrl, String token) {
        if (activateUrl == null) {
            activateUrl = neubbsConfig.getAccountApiVaslidateUrl();
        }

        return StringUtil.createEmailActivationHtmlString(activateUrl + token);
    }

    @Override
    public String getEmailContentToWarnUserGeneratedRandomPassword(String email, String randomPassword) {
        return  email + " 邮箱用户临时密码为：" + randomPassword + " ，请登陆后尽快修改密码！";
    }

    @Override
    public void sendEmail(String sendUserName, String toEmail, String toSubject, String toEmailcontent) {
        taskExecutor.execute(
                () -> SendEmailUtil.sendEmail(sendUserName, toEmail, toSubject, toEmailcontent)
        );
    }
}
