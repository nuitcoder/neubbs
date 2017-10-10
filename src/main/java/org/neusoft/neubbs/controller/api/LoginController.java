package org.neusoft.neubbs.controller.api;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.constant.AjaxRequestStatus;
import org.neusoft.neubbs.constant.LoggerInfo;
import org.neusoft.neubbs.constant.TokenInfo;
import org.neusoft.neubbs.constant.UserInfo;
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
public class LoginController {

    @Autowired
    IUserService userService;
    @Autowired
    IRedisService redisService;

    private static Logger logger = Logger.getLogger(LoginController.class);

    /**
     * 输入 username password，登录
     * @param username
     * @param password
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO login(@RequestParam(value = "username", required = false) String username,
                                 @RequestParam(value = "password", required = false) String password,
                                    HttpServletResponse response)
                                        throws Exception {

        //空判断
        if (username == null || username.length() <= 0) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.LOGIN_USERNAME_NULL);
        }
        if (password == null || password.length() <= 0) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.LOGIN_PASSWORD_NULL);
        }

        //用户是否存在判断
        UserDO user = userService.getUserByName(username);
        Map<String, Object> userInfoMap = JsonUtils.getMapByObject(user);
        if (userInfoMap == null){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.DATABASE_NO_EXIST_USER);
        }

        //用户密码判断
        if (!password.equals(userInfoMap.get(UserInfo.PASSWORD))) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.LOGIN_PASSWORD_ERROR);
        }

        //用户密码通过验证
        if (username.equals(userInfoMap.get(UserInfo.NAME)) && password.equals(userInfoMap.get(UserInfo.PASSWORD))) {
            //获取Token，储存进本地Cookie
            String token = TokenUtils.createToken(user);
            CookieUtils.saveCookie(response, TokenInfo.AUTHENTICATION, token);
            //System.out.println(token);//测试打印 token
            //CookieUtils.printCookie(request);//测试打印 Cookie

            logger.info(username + LoggerInfo.USER_LOGINNER_SUCCESS);
        }
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, UserInfo.LOGIN_PASS_AUTHENTICATE_LOGIN_SUCCESS, userInfoMap);
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

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, UserInfo.LOGOUT_SUCCESS);
    }
}