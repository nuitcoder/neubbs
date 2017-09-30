package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.constant.user.UserInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.CookieUtils;
import org.neusoft.neubbs.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO login(@RequestParam(value = "username", required = false) String username,
                                 @RequestParam(value = "password", required = false) String password,
                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //定义响应数据传输对象
        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        //空判断
        if (username == null || username.length() <= 0) {
            responseJson.put(AjaxRequestStatus.FAIL, LoginInfo.USERNAME_NULL);
            return responseJson;
        }
        if (password == null || password.length() <= 0) {
            responseJson.put(AjaxRequestStatus.FAIL, LoginInfo.PASSWORD_NULL);
            return responseJson;
        }

        //用户是否存在判断
        Map<String,String> userInfoMap = userService.listUserInfoByName(username);
        if (userInfoMap == null) {
            responseJson.put(AjaxRequestStatus.FAIL,LoginInfo.USER_NOEXIT);
            return responseJson;
        }

        //指定用户密码判断
        if (!password.equals(userInfoMap.get(UserInfo.PASSWORD))) {
            responseJson.put(AjaxRequestStatus.SUCCESS, LoginInfo.PASSWORD_ERROR);
            return responseJson;
        }

        //用户密码通过验证
        if (username.equals(userInfoMap.get(UserInfo.USERNAME)) && password.equals(userInfoMap.get(UserInfo.PASSWORD))) {
            //获取Token，储存进本地Cookie
            String token = TokenUtils.createToken(username);
            CookieUtils.saveCookie(response, LoginInfo.AUTHORIZATION, token);
            //CookieUtils.printCookie(request);//本地打印Cookie测试
        }

        return responseJson;
    }

    @LoginAuthorization
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO logout(HttpServletRequest request,HttpServletResponse response){
        ResponseJsonDTO responseJson = new ResponseJsonDTO();
            //CookieUtils.removeCookie(request, response, LoginInfo.AUTHORIZATION);
        return responseJson;
    }


    @LoginAuthorization
    @RequestMapping(value = "/get/userinfo",method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInfo(@RequestParam(value = "username",required = false)String username){
        ResponseJsonDTO responseJson = new ResponseJsonDTO();
            Map<String,String> userInfoMap = userService.listUserInfoByName(username);
            if(userInfoMap == null){
                responseJson.put(AjaxRequestStatus.FAIL, LoginInfo.USER_NOEXIT);
                return responseJson;
            }

            responseJson.put(AjaxRequestStatus.SUCCESS, LoginInfo.USER_GETINFO, userInfoMap);
        return responseJson;
    }
}