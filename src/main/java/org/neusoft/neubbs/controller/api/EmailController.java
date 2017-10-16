package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.account.AccountInfo;
import org.neusoft.neubbs.constant.account.EmailInfo;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.RequestParamsCheckUtils;
import org.neusoft.neubbs.util.SecretUtils;
import org.neusoft.neubbs.util.SendEmailUtils;
import org.neusoft.neubbs.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 *  邮件 api
 *      1. + 发送邮件（账户激活URL）
 */
@Controller
@RequestMapping("/api/email")
public class EmailController {

    private final String ACTIVATION_URL = "http://localhost:8080/api/account/activation?token="; //邮件激活 URL;

    @Autowired
    IUserService userService;

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
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorInfo);
        }

        //检测数据库是否存在此邮箱
        UserDO user = userService.getUserInfoByEmail(email);
        if(user == null){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, EmailInfo.EMAIL_NO_REGISTER_NO_SEND_EMAIL);
        }

        //构建 token（用户邮箱 + 过期时间）
        long expireTime = System.currentTimeMillis() + SecretInfo.EXPIRE_TIME_ONE_DAY;
        String token = SecretUtils.encryptBase64(email + "-" + expireTime);

        //构建邮件内容
        String content = StringUtils.createEmailActivationHtmlString(ACTIVATION_URL + token);

        //发送邮件（另启线程）
        new Thread(new Runnable() {
            @Override
            public void run() {
                SendEmailUtils.sendEmail(email, EmailInfo.EMAIL_ACTIVATE_FROM_SUBJECT , content);
            }
        }).start();


        //发送成功
       return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, EmailInfo.ENGLISH_ACCOUTN_ACTIVATE_EMAIL_SEND_SUCCESS);
    }
}
