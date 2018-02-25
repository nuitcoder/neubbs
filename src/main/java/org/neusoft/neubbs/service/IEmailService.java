package org.neusoft.neubbs.service;

/**
 * 邮箱业务接口
 *      - 发送账户激活邮件
 *      - 发送重置临时密码邮件
 *
 * @author Suvan
 */
public interface IEmailService {

    /**
     * 发送账户激活邮件
     *
     * @param email 用户邮箱
     * @param emailToken 加密后用户邮箱密文
     */

    void sendAccountActivateMail(String email, String emailToken);

    /**
     * 发送重置临时密码邮件
     *
     * @param email 用户邮箱
     * @param temporaryRandomPassword 临时随机密码
     */
    void sendResetTemporaryPasswordMail(String email, String temporaryRandomPassword);
}
