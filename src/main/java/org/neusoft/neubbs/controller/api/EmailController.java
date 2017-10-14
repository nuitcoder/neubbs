package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.account.EmailInfo;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.ajax.RequestParamInfo;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.SecretUtils;
import org.neusoft.neubbs.util.SendEmailUtils;
import org.neusoft.neubbs.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  邮件 api
 *      1. + 发送邮件（账户激活URL）
 */
@Controller
@RequestMapping("/api/email")
public class EmailController {

    private final String ACTIVATION_URL = "http://localhost:8080/neubbs/api/account/activation?token="; //邮件激活 URL;

    @Autowired
    IUserService userService;

    /**
     * 1.发送邮件（账户激活URL）
     * @param email
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @RequestMapping(value = "/account-activation", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO activation(@RequestParam(value = "email", required = false)String email) throws Exception{
        if(email == null || email.length() == 0){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, RequestParamInfo.PARAM_EMAIL_NO_NULL);
        }

        //检测数据库是否存在此邮箱
        UserDO user = userService.getUserInfoByEmail(email);
        if(user == null){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, EmailInfo.EMAIL_NO_REGISTER_NO_SEND_EMAIL);
        }

        //构建 token（用户邮箱 + 过期时间）
        long expireTime = System.currentTimeMillis() + SecretInfo.EXPIRE_TIME_ONE_DAY;
        String token = SecretUtils.base64Encrypt(email + "-" + expireTime);

        //构建邮件内容
        String content = StringUtils.createEmailActivationHtmlString(ACTIVATION_URL + token);

        //发送邮件（另启线程）
        new Thread(new Runnable() {
            @Override
            public void run() {
                SendEmailUtils.sendEmail(email, EmailInfo.EMAIL_ACTIVATION_FROM_SUBJECT , content);
            }
        }).start();


        //发送成功
       return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, EmailInfo.EMAIL_AUTIVATION_SEND_EMAIL_SUCCESS);
    }
}
