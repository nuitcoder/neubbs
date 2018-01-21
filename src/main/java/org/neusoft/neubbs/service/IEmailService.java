package org.neusoft.neubbs.service;

/**
 * 邮箱业务接口
 *      - 获取激活邮件内容
 *      - 获取密码变更邮件内容
 *      - 发送邮件
 *
 * @author Suvan
 */
public interface IEmailService {

    /**
     * 获取激活邮件内容
     *      - HTML 格式
     *      - activateUrl 是需注入内容的激活链接（可设为 null，即用 neubbs.properties 的默认值）
     *
     * @param activateUrl 激活链接
     * @param token 激活密文token（Base64加密）
     * @return String 激活邮件内容
     */
    String getActivationMailContent(String activateUrl, String token);

    /**
     * 获取密码变更邮件内容
     *      - 提示用户密码已变更过，已变为临时随机密码
     *
     * @param email 用户邮箱
     * @param tempRandomPassword 新生成的临时随机密码
     * @return String 密码变更邮件内容
     */
    String getPasswordChangeMailContent(String email, String tempRandomPassword);

    /**
     * 发送邮件
     *      - 线程池另起线程发送邮件
     *
     * @param sendNickname 发送人昵称
     * @param receiveEmail 接收邮箱
     * @param sendSubject 发送主题
     * @param sendEmailContent 发送内容
     */
    void send(String sendNickname, String receiveEmail, String sendSubject, String sendEmailContent);
}
