package org.neusoft.neubbs.utils;

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
 *
 * @author Suvan
 */
public final class SendEmailUtil {

    private SendEmailUtil() { }

    /**
     * 邮件格式
     */
    private static final String FROM_SUBJECT_ENCODING = "UTF-8";
    private static final String FROM_CONTENT_TYPE = "text/html;charset=UTF-8";

    /**
     * 腾讯企业邮箱
     */
    private static final String TO_HOST = "smtp.exmail.qq.com";
    private static final String TO_SMTP = "smtp";
    private static final String TO_AUTH = "mail.smtp.auth";
    private static final String TO_AUTH_TRUE = "true";
    private static final String TO_MAIL_SMTP_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";
    private static final String TO_JAVAX_NET_SSL_SSLSOCKETFACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final String TO_MAIL_SMTP_SOCKETFACTORY_PORT = "mail.smtp.socketFactory.port";
    private static final String TO_SMTP_SSL_PROT = "465";

    /**
     * 发送邮件的账户
     */
    private static final String FROM_USERNAME;
    private static final String FROM_AUTHORIZATION_CODE;

    static {
        Resource resource = new ClassPathResource("/neubbs.properties");
        Properties props = null;
        try {
            //read '/resources/neubbs.properties'
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        FROM_USERNAME = props.getProperty("email.service.send.account.username");
        FROM_AUTHORIZATION_CODE = props.getProperty("email.service.send.account.authorization.code");
    }

    /**
     * 发送邮件
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
   public static void  sendEmail(String sendNickname, String receiveEmail,
                                 String sendSubject, String sendEmailContent) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setUsername(FROM_USERNAME);
            sender.setPassword(FROM_AUTHORIZATION_CODE);
            sender.setHost(TO_HOST);
            sender.setProtocol(TO_SMTP);
            sender.setPort(Integer.parseInt(TO_SMTP_SSL_PROT));

        Properties properties = new Properties();
            properties.setProperty(TO_AUTH, TO_AUTH_TRUE);
            properties.setProperty(TO_MAIL_SMTP_SOCKETFACTORY_CLASS, TO_JAVAX_NET_SSL_SSLSOCKETFACTORY);
            properties.setProperty(TO_MAIL_SMTP_SOCKETFACTORY_PORT, TO_SMTP_SSL_PROT);

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

                message.setSubject(sendSubject, FROM_SUBJECT_ENCODING);
                message.setContent(sendEmailContent, FROM_CONTENT_TYPE);

                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiveEmail));

                message.saveChanges();

            } catch (UnsupportedEncodingException | MessagingException uee) {
                uee.printStackTrace();
            }

        sender.send(message);
    }
}
