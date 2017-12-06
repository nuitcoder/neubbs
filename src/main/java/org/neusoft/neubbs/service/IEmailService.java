package org.neusoft.neubbs.service;

/**
 * 邮箱业务接口
 *
 * @author Suvan
 */
public interface IEmailService {

    /**
     * 获取邮件内容，关于激活用户邮件 HTML
     *
     * @param activateUrl 激活URL（http://...=）
     * @param token 激活密文 token
     * @return String 用户激活邮件内容HTML
     */
    String getEmailContentxtToActivateUserMailHtml(String activateUrl, String token);


    /**
     * 获取邮件内容，警告用户生产随机密码
     *
     * @param email 用户邮箱
     * @param ramdomPassword 随机数密码（新生成的临时密码）
     * @return String 邮件内容
     */
    String getEmailContentToWarnUserGeneratedRandomPassword(String email, String ramdomPassword);

    /**
     * 发送邮件
     *
     * @param sendUserName 发件人昵称
     * @param toEmail 发送至邮箱
     * @param toSubject 发送主题
     * @param toEmailcontent 发送内容
     */
    void sendEmail(String sendUserName, String toEmail, String toSubject, String toEmailcontent);
}
