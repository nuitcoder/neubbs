package org.neusoft.neubbs.utils;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.exception.UtilClassException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 发送邮件工具类
 *      - 发送（邮件）
 *
 * @author Suvan
 */
public final class SendEmailUtil {

    private SendEmailUtil() { }

    /**
     * 腾讯企业邮箱
     *      - 帐户名
     *      - 第三方授权码
     */
    private static final String FROM_USERNAME;
    private static final String FROM_AUTHORIZATION_CODE;

    /*
     * ***********************************************
     * 静态代码块
      *     - 读取 src/main/resources/neubbs.properties
     * ***********************************************
     */
    static {
        Resource resource = new ClassPathResource("/neubbs.properties");
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);

            FROM_USERNAME = props.getProperty("email.service.send.account.username");
            FROM_AUTHORIZATION_CODE = props.getProperty("email.service.send.account.authorization.code");
        } catch (IOException ioe) {
            throw new UtilClassException(ApiMessage.UNKNOWN_ERROR).log(LogWarnEnum.UC3);
        }
    }

    /**
     * 发送（邮件）
     *      - 构建邮件请求
     *      - 构建参数
     *      - 构建连接
     *      - 构建消息
     *          - 设置发件人 + 昵称
     *          - 设置主题 + 内容
     *          - 设置收件人
     *          - 保存更改
     *      - 发送邮件
     *
     * @param sendNickname 发件人昵称
     * @param receiveEmail 接收邮箱
     * @param sendSubject 发送主题
     * @param sendEmailContent 发送内容
     */
   public static void  send(String sendNickname, String receiveEmail,
                            String sendSubject, String sendEmailContent) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setUsername(FROM_USERNAME);
            sender.setPassword(FROM_AUTHORIZATION_CODE);
            sender.setHost(SetConst.TO_HOST);
            sender.setProtocol(SetConst.TO_SMTP);
            sender.setPort(Integer.parseInt(SetConst.TO_SMTP_SSL_PROT));

        Properties properties = new Properties();
            properties.setProperty(SetConst.TO_AUTH, SetConst.TO_AUTH_TRUE);
            properties.setProperty(
                    SetConst.TO_MAIL_SMTP_SOCKETFACTORY_CLASS,
                    SetConst.TO_JAVAX_NET_SSL_SSLSOCKETFACTORY
            );
            properties.setProperty(SetConst.TO_MAIL_SMTP_SOCKETFACTORY_PORT, SetConst.TO_SMTP_SSL_PROT);

        sender.setJavaMailProperties(properties);

        Session mailSession = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_USERNAME, FROM_AUTHORIZATION_CODE);
            }
        });

        MimeMessage message = new MimeMessage(mailSession);
            try {
                message.setFrom(new InternetAddress(FROM_USERNAME, sendNickname));

                message.setSubject(sendSubject, SetConst.FROM_SUBJECT_ENCODING);
                message.setContent(sendEmailContent, SetConst.FROM_CONTENT_TYPE);

                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiveEmail));

                message.saveChanges();

            } catch (UnsupportedEncodingException | MessagingException uee) {
                uee.printStackTrace();
            }

        sender.send(message);
    }
}
