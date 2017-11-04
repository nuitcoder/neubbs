package org.neusoft.neubbs.utils;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 发送邮件 工具类
 *
 * @author Suvan
 */
public final class SendEmailUtil {

    private SendEmailUtil() { }

    /**
     * 发送邮件的账户
     */
    private static final String FROM_USERNAME = "suvan@liushuwei.cn";
    private static final String FROM_AUTHORIZATIONCODE = "23h235i2vhKwUgbg";

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
     * 发送邮件
     *
     * @param name 发件人昵称
     * @param email 要发送的的邮箱
     * @param subject 发送主题
     * @param content 发送内容
     */
   public static void  sendEmail(String name, String email, String subject, String content) {
        //构造邮件请求
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setUsername(FROM_USERNAME);
            sender.setPassword(FROM_AUTHORIZATIONCODE);
            sender.setHost(TO_HOST);
            sender.setProtocol(TO_SMTP);
            sender.setPort(Integer.parseInt(TO_SMTP_SSL_PROT));

        //构建参数
        Properties properties = new Properties();
            properties.setProperty(TO_AUTH, TO_AUTH_TRUE);
            properties.setProperty(TO_MAIL_SMTP_SOCKETFACTORY_CLASS, TO_JAVAX_NET_SSL_SSLSOCKETFACTORY);
            properties.setProperty(TO_MAIL_SMTP_SOCKETFACTORY_PORT, TO_SMTP_SSL_PROT);

        sender.setJavaMailProperties(properties);

       //构建连接
        Session mailSession = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_USERNAME, FROM_AUTHORIZATIONCODE);
            }
        });

        //构建消息
        MimeMessage message = new MimeMessage(mailSession);
            try {
                //设置发件人 + 昵称
                message.setFrom(new InternetAddress(FROM_USERNAME, name));

                //设置主题 + 内容
                message.setSubject(subject, FROM_SUBJECT_ENCODING);
                message.setContent(content, FROM_CONTENT_TYPE);

                //设置收件人
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

                //保存更改
                message.saveChanges();

            } catch (UnsupportedEncodingException | MessagingException uee) {
                uee.printStackTrace();
            }

       //发送邮件
        sender.send(message);
    }
}
