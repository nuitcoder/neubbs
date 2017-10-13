package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.account.AccountInfo;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *  邮件 api
 *      1. 发送邮件验证码
 */
@Controller
@RequestMapping("/api/email")
public class EmailController {


    /**
     * 邮件验证码
     * @param email
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @RequestMapping(value = "/code", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO emailCode(@RequestParam(value = "email", required = false)String email) throws Exception{
        if(email == null || email.length() == 0){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.EMAILCODE_EMAIL_NUNULL);
        }

        //生成 6 位数随机数,验证码
        int randomNumber = (int)((Math.random() * 9 + 1) * 100000);

        //构造邮件请求
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(AccountInfo.EMAILCODE_HOST);
            sender.setUsername(AccountInfo.EMAILCODE_FROM_USERNAME);
            sender.setPassword(AccountInfo.EMAILCODE_FROM_PASSWORD);
            sender.setProtocol(AccountInfo.EMAILCODE_SMTP);
            sender.setPort(465);

        Properties properties = new Properties();
            properties.setProperty(AccountInfo.EMAILCODE_AUTH, AccountInfo.EMAILCODE_AUTH_TRUE);
            properties.setProperty(AccountInfo.EMAILCODE_SMTP_SOCKETFACTORY_CLASS, AccountInfo.EMAILCODE_JAVAX_NET_SSL_SSLSOCKETFACTORY);
            properties.setProperty(AccountInfo.EMAILCODE_SMTP_SOCKETFACTORY_PORT, AccountInfo.EMAILCODE_SMTP_SSL_PROT);

        sender.setJavaMailProperties(properties);

        SimpleMailMessage ssm = new SimpleMailMessage();
            ssm.setFrom(sender.getUsername());
            ssm.setTo(email);
            ssm.setSubject(AccountInfo.EMAILCODE_TO_SUBJECT);
            ssm.setText(AccountInfo.EMAILCODE_TO_TEXT + randomNumber);

        //发送邮件
        sender.send(ssm);

        Map<String, Object> codeMap = new HashMap<String, Object>();
            codeMap.put(AccountInfo.EMAILCODE, randomNumber);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.EMAILCODE_SUCCESS, codeMap);
    }
}
