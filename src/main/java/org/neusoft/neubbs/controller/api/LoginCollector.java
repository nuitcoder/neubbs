package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.constant.login.TokenInfo;
import org.neusoft.neubbs.constant.user.UserInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.CookieUtils;
import org.neusoft.neubbs.util.JsonUtils;
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
 *  登录，注销 api
 */
@Controller
@RequestMapping("/api")
public class LoginCollector {

    @Autowired
    IUserService userService;
    @Autowired
    IRedisService redisService;

    /**
     * 输入 username password，登录
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
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
            responseJson.putAjaxFail(LoginInfo.USERNAME_NULL);
            return responseJson;
        }
        if (password == null || password.length() <= 0) {
            responseJson.putAjaxFail(LoginInfo.PASSWORD_NULL);
            return responseJson;
        }

        //用户是否存在判断
        UserDO user = userService.getUserByName(username);
        Map<String, Object> userInfoMap = JsonUtils.getMapByObject(user);
        if (userInfoMap == null){
            responseJson.putAjaxFail(UserInfo.DATABASE_NO_EXIST_USER);
            return responseJson;
        }

        //用户密码判断
        if (!password.equals(userInfoMap.get(UserInfo.PASSWORD))) {
            responseJson.putAjaxFail(LoginInfo.PASSWORD_ERROR);
            return responseJson;
        }


        //用户密码通过验证
        if (username.equals(userInfoMap.get(UserInfo.NAME)) && password.equals(userInfoMap.get(UserInfo.PASSWORD))) {
            //获取Token，储存进本地Cookie
            String token = TokenUtils.createToken(user);
            CookieUtils.saveCookie(response, TokenInfo.AUTHENTICATION, token);
            //System.out.println(token);//测试打印 token
            //CookieUtils.printCookie(request);//测试打印 Cookie

            responseJson.putAjaxSuccess(LoginInfo.PASS_AUTHENTICATE_LOGIN_SUCCESS);
            responseJson.getModel().add(userInfoMap);
        }

        return responseJson;
    }

    /**
     * 注销用户
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO logout(HttpServletRequest request,HttpServletResponse response) throws Exception{
        //删除Cookie
        CookieUtils.removeCookie(request, response, TokenInfo.AUTHENTICATION);

        //构建JSON提示信息
        ResponseJsonDTO responseJson = new ResponseJsonDTO();
            responseJson.putAjaxSuccess(LoginInfo.LOGOUT_SUCCESS);

        return responseJson;
    }
}