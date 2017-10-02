package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.constant.db.RedisInfo;
import org.neusoft.neubbs.constant.login.TokenInfo;
import org.neusoft.neubbs.constant.secret.JWTTokenSecret;
import org.neusoft.neubbs.constant.user.UserInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.CookieUtils;
import org.neusoft.neubbs.util.JsonUtils;
import org.neusoft.neubbs.util.TokenUtils;
import org.neusoft.neubbs.entity.token.TokenDO;
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
     * 登录接口
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
            //构建数据传输对象
            responseJson.put(AjaxRequestStatus.SUCCESS, LoginInfo.USER_AUTHENTICATE, userInfoMap);

            //获取Token，储存进本地Cookie
            TokenDO tokenDO = TokenUtils.createToken(username);
            CookieUtils.saveCookie(response, TokenInfo.AUTHENTICATION, tokenDO.getToken());
            //System.out.println("JWT username :" + tokenDO.getTokenname());//打印JWT加密的username
            //CookieUtils.printCookie(request);//本地打印Cookie测试

            //Redis储存键值对
            String userInfoJSON = JsonUtils.getObjectJSONString(userInfoMap);
            redisService.saveByKeyValueTime(tokenDO.getTokenname(), userInfoJSON, RedisInfo.EXPIRETIME_SERVER_DAY);

            //持久化到MySQL(用于备份，防止服务器宕机)【未实现，可在下面写业务逻辑】

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
        //Reids清空缓存
        String token = CookieUtils.getCookieValue(request, TokenInfo.AUTHENTICATION);
        String username = TokenUtils.verifyToken(token, JWTTokenSecret.SECRET_KEY);//解密获取用户
        redisService.removeByKey(username);

        //删除Cookie
        CookieUtils.removeCookie(request, response, TokenInfo.AUTHENTICATION);
        //CookieUtils.printCookie(request);//打印测试

        //删除MySQL记录【未实现，可以在此添加业务逻辑】

        //构建JSON提示信息
        ResponseJsonDTO responseJson = new ResponseJsonDTO();
        responseJson.put(AjaxRequestStatus.SUCCESS, LoginInfo.LOGOUT_SUCCESS);

        return responseJson;
    }
}