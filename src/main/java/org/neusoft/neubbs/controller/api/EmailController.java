package org.neusoft.neubbs.controller.api;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.AccountInfo;
import org.neusoft.neubbs.constant.api.EmailInfo;
import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.RequestParamsCheckUtils;
import org.neusoft.neubbs.util.SecretUtils;
import org.neusoft.neubbs.util.SendEmailUtils;
import org.neusoft.neubbs.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 *  邮件 api
 *      1. + 发送邮件（账户激活URL）
 *
 *  @author Suvan
 */
@Controller
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    IUserService userService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    private static final Logger logger = Logger.getLogger(EmailController.class);

    /**
     * 账户激活 URL
     */
    private final  String ACCOUNT_ACTIVATION_URL = "http://localhost:8080/api/account/activation?token=";

    /**
     * 1.发送邮件（账户激活URL）
     * @param requestBodyParamsMap
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @RequestMapping(value = "/account-activation", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO activation(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception{
        String email = (String)requestBodyParamsMap.get(AccountInfo.EMAIL);

        String errorInfo = RequestParamsCheckUtils.checkEmail(email);
        if (errorInfo != null) {
            throw new ParamsErrorException(errorInfo).log(errorInfo);
        }

        //检测数据库是否存在此邮箱
        UserDO user = userService.getUserInfoByEmail(email);
        if(user == null){
            throw new AccountErrorException(EmailInfo.EMAIL_NO_REIGSTER).log(LogWarnInfo.EMAIL_NO_REGISTER_NO_SEND_EMAIL);
        }

        //构建 token（用户邮箱 + 过期时间）
        long expireTime = System.currentTimeMillis() + SecretInfo.EXPIRETIME_ONE_DAY;
        String token = SecretUtils.encryptBase64(email + "-" + expireTime);

        //构建邮件内容
        String content = StringUtils.createEmailActivationHtmlString(ACCOUNT_ACTIVATION_URL + token);

        //发送邮件（Spring 线程池，另启线程）
        taskExecutor.execute(new Runnable(){
            @Override
            public void run() {
                SendEmailUtils.sendEmail(email, EmailInfo.EMAIL_SUBJECT , content);
            }
        });

       return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, EmailInfo.MAIL_SENT_SUCCESS);
    }
}
