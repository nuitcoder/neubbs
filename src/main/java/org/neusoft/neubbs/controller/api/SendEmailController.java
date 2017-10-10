package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.AjaxRequestStatus;
import org.neusoft.neubbs.constant.UserInfo;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 发送邮件 控制器
 */
@Controller
@RequestMapping("/api")
public class SendEmailController {


    /**
     * 输入邮箱，发送邮箱验证码 api
     * @param email
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/code", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO emailCode(@RequestParam(value = "email", required = false)String email,
                                        HttpServletRequest request, HttpServletResponse response)
                                            throws Exception{
        if(email == null || email.length() == 0){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.EMAILCODE_EMAIL_NUNULL);
        }

        //生成 6 位数随机数,验证码
        int randomNumber = (int)((Math.random() * 9 + 1) * 100000);

        //构造邮件请求
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(UserInfo.EMAILCODE_HOST);
            sender.setUsername(UserInfo.EMAILCODE_FROM_USERNAME);
            sender.setPassword(UserInfo.EMAILCODE_FROM_PASSWORD);
            sender.setProtocol(UserInfo.EMAILCODE_SMTP);
            sender.setPort(465);

        Properties properties = new Properties();
            properties.setProperty(UserInfo.EMAILCODE_AUTH, UserInfo.EMAILCODE_AUTH_TRUE);
            properties.setProperty(UserInfo.EMAILCODE_SMTP_SOCKETFACTORY_CLASS, UserInfo.EMAILCODE_JAVAX_NET_SSL_SSLSOCKETFACTORY);
            properties.setProperty(UserInfo.EMAILCODE_SMTP_SOCKETFACTORY_PORT, UserInfo.EMAILCODE_SMTP_SSL_PROT);

        sender.setJavaMailProperties(properties);

        SimpleMailMessage ssm = new SimpleMailMessage();
            ssm.setFrom(sender.getUsername());
            ssm.setTo(email);
            ssm.setSubject(UserInfo.EMAILCODE_TO_SUBJECT);
            ssm.setText(UserInfo.EMAILCODE_TO_TEXT + randomNumber);

        //发送邮件
        sender.send(ssm);

        Map<String, Object> codeMap = new HashMap<String, Object>();
            codeMap.put(UserInfo.EMAILCODE, randomNumber);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, UserInfo.EMAILCODE_SUCCESS, codeMap);
    }
}
