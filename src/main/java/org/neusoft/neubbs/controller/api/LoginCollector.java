package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.constant.user.UserInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.LoginJsonDTO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.CookieUtils;
import org.neusoft.neubbs.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录接口
 */
@Controller
@RequestMapping("/api")
public class LoginCollector {

    @Autowired
    IUserService userService;


    @RequestMapping(value="/login",method = RequestMethod.GET)
    @ResponseBody
    @LoginAuthorization
    public LoginJsonDTO loginCheck(@RequestParam(value="username",required = false)String username,
                                   @RequestParam(value="password",required = false)String password,
                                   HttpServletRequest request, HttpServletResponse response)
                                        throws Exception{
        //定义数据传输对象
        LoginJsonDTO loginJson = new LoginJsonDTO();

        //空判断
        if(username == null || username.length() <= 0){
            loginJson.put(AjaxRequestStatus.FAIL, LoginInfo.USERNAME_NULL);
            return loginJson;
        }
        if(password == null || password.length() <= 0){
            loginJson.put(AjaxRequestStatus.FAIL,LoginInfo.PASSWORD_NULL);
            return loginJson;
        }

        //用户是否存在判断
        LoginJsonDTO dataLoginJson = userService.getLoginJsonByName(username);
        if(dataLoginJson.getModel() == null) {
            return dataLoginJson;
        }

        //指定用户密码判断
        Map<String,String> userInfoMap = dataLoginJson.getModel();
        if(!password.equals(userInfoMap.get(UserInfo.PASSWORD))){
            loginJson.put(AjaxRequestStatus.SUCCESS,LoginInfo.PASSWORD_ERROR);
            return loginJson;
        }

        //用户密码通过验证
        if(username.equals(userInfoMap.get(UserInfo.USERNAME)) && password.equals(userInfoMap.get(UserInfo.PASSWORD))){
            //获取Token，储存进本地Cookie
            String token = TokenUtils.createToken(username);
            CookieUtils.saveCookie(response,LoginInfo.AUTHORIZATION,token);
            //CookieUtils.printCookie(request);//本地打印Cookie测试
        }

        return dataLoginJson;
    }
}
