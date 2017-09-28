package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.AjaxRequestStatus;
import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.constant.user.UserInfo;
import org.neusoft.neubbs.entity.json.LoginJsonDO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.CookieUtils;
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
 * Login Api
 *
 * @Author Suvan
 * @Date 2017-09-27-22:40
 */
@Controller
@RequestMapping("/api")
public class LoginCollector {

    @Autowired
    IUserService userService;


    @RequestMapping(value="/login",method = RequestMethod.GET)
    @ResponseBody
    public LoginJsonDO loginCheck(@RequestParam(value="username",required = false)String username,
                                  @RequestParam(value="password",required = false)String password,
                                    HttpServletRequest request, HttpServletResponse response)
                                        throws Exception{

        //判断是否自动登录
        String loginStatus = CookieUtils.getCookieValue(request,UserInfo.LOGINSTATE);
        if(loginStatus != null){
            String cookieUsername = CookieUtils.getCookieValue(request,UserInfo.USERNAME);

            if(cookieUsername != null){
                LoginJsonDO dataLoginJson = userService.getLoginJsonByName(username);
                if(dataLoginJson.getModel() != null) {
                    return dataLoginJson;
                }
            }
        }

        LoginJsonDO loginJson = new LoginJsonDO();//数据传输对象暂存

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
        LoginJsonDO dataLoginJson = userService.getLoginJsonByName(username);
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
            //Cookie自动登录
            CookieUtils.saveCookie(response,UserInfo.USERNAME,username);
            CookieUtils.saveCookie(response,UserInfo.LOGINSTATE,UserInfo.LOGINSTATE_YES);//登录状态
            CookieUtils.printCookie(request);//打印本地Cookie,包含JSESSIONID,Idea-7ca706b7,loginState
        }

        return dataLoginJson;
    }
}
