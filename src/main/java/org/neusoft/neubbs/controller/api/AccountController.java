package org.neusoft.neubbs.controller.api;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.constant.account.AccountInfo;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.count.CountInfo;
import org.neusoft.neubbs.constant.log.LoggerInfo;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.constant.secret.TokenInfo;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *   账户 api
 *      1.获取用户信息
 *      2.登录
 *      3.注销
 *      4.注册
 *      5.修改密码
 *      6.激活账户
 */
@Controller
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    IUserService userService;

    @Autowired
    IRedisService redisService;

    private static Logger logger = Logger.getLogger(AccountController.class);//日志类

    /**
     * 1.获取用户信息（AccountController 默认访问）
     * @param username
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInfoByName(@RequestParam(value = "username", required = false)String username) throws Exception{
        //@RequestParam 的 required 属性声明参数是否必须，默认为true,此处声明 false 表示参数非必须，由 api 内部处理空情况

        //参数合法性检测
        String errorInfo = RequestParamsCheckUtils.username(username);//用户名检测
        if (errorInfo != null) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorInfo);
        }

        //数据库获取用户信息
        UserDO user = userService.getUserInfoByName(username);
        Map<String, Object> userInfoMap = JsonUtils.getMapByObject(user); //将 Object 对象转为 Map 类型
        if(userInfoMap == null){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.DATABASE_NO_EXIST_USER);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.GET_USER_INFORMATION_SUCCESS, userInfoMap);
    }

    /**
     * 2.登录
     * @param username
     * @param password
     * @param request
     * @param response
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO login(@RequestParam(value = "username", required = false) String username,
                                 @RequestParam(value = "password", required = false) String password,
                                 HttpServletRequest request,HttpServletResponse response) throws Exception {
        //用户，密码参数合法性检测
        String errorInfo = RequestParamsCheckUtils.checkUsernamePassword(username,password);
        if (errorInfo != null) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorInfo);
        }

        //用户是否存在
        UserDO user = userService.getUserInfoByName(username);
        Map<String, Object> userInfoMap = JsonUtils.getMapByObject(user);
        if (userInfoMap == null) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.DATABASE_NO_EXIST_USER);
        }

        //用户是否激活
        if((Integer)userInfoMap.get(AccountInfo.STATE) == 0){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.ACCOUNT_NO_ACTIVATION_NO_LOGIN);
        }

        //用户密码判断
        String cipherText = SecretUtils.passwordMD5Encrypt(password);//密码 MD5 加密后的密文
        if(cipherText == null){//服务器加密失败
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, SecretInfo.MD5_ENCRYP_FAIL);
        }
        if (!cipherText.equals(userInfoMap.get(AccountInfo.PASSWORD))) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.USER_PASSWORD_NO_MATCH);
        }

        //用户密码通过验证
        if (username.equals(userInfoMap.get(AccountInfo.NAME)) && cipherText.equals(userInfoMap.get(AccountInfo.PASSWORD))) {
            //获取Token，储存进本地Cookie
            String token = TokenUtils.createToken(user);
            CookieUtils.saveCookie(response, TokenInfo.AUTHENTICATION, token);
            //System.out.println(token);//测试打印 token
            //CookieUtils.printCookie(request);//测试打印 Cookie

            //在线登录人数+1
            ServletContext application = request.getServletContext();// 获取上下文对象（全局唯一）
            Integer onlineLoginUser = (Integer) application.getAttribute(CountInfo.ONLINE_LOGIN_USER);//获取在线登录对象
            if(onlineLoginUser == null) {
                onlineLoginUser = 0;
            }
            application.setAttribute(CountInfo.ONLINE_LOGIN_USER, ++onlineLoginUser);//用户 +1

            //储存日志（记录用户登录成功信息）
            logger.info(username + LoggerInfo.USER_LOGINNER_SUCCESS);

            return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.USER_PASS_AUTHENTICATE_LOGIN_SUCCESS,
                                       TokenInfo.AUTHENTICATION, token);
        }

        return null;
    }

    /**
     * 3.注销
     * @param request
     * @param response
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO logout(HttpServletRequest request,HttpServletResponse response) throws Exception{
        //删除Cookie
        CookieUtils.removeCookie(request, response, TokenInfo.AUTHENTICATION);

        //在线登录人数-1
        ServletContext application = request.getServletContext();
        Integer onlineLoginUser = (Integer)application.getAttribute(CountInfo.ONLINE_LOGIN_USER);
        if(onlineLoginUser != null){
            application.setAttribute(CountInfo.ONLINE_LOGIN_USER, --onlineLoginUser);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.USER_LOGOU_SUCCESS);
    }


    /**
     * 4.注册
     * @param username
     * @param password
     * @param email
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO registerUser(@RequestParam(value = "username", required = false)String username,
                                        @RequestParam(value = "password", required = false)String password,
                                        @RequestParam(value = "email", required = false)String email) throws Exception {
        //用户名，密码，邮箱参数合法性检测
        String errorInfo = RequestParamsCheckUtils.checkUsernamePasswordEmail(username, password, email);
        if (errorInfo != null) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorInfo);
        }

        //判断用户名唯一
        UserDO user = userService.getUserInfoByName(username);
        if (user != null) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.DATABASE_ALREAD_EXIST_USER_NO_AGAIN_ADD);
        }

        //注册操作
        user = new UserDO();
            user.setName(username);
            user.setEmail(email);

            //密码加密
            String cipherText = SecretUtils.passwordMD5Encrypt(password);
            user.setPassword(cipherText);

        userService.registerUser(user);

        //注册成功,会自动注入用户 id（用户自增）
        int newId = user.getId();
        user = userService.getUserInfoById(newId);//根据 id 重新查询用户
        Map<String, Object> userInfoMap = JsonUtils.getMapByObject(user);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.REGISTER_USER_SUCCESS, userInfoMap);
    }


    /**
     * 5.修改密码
     * @param username
     * @param password
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/update-password", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO updateUserPasswordById(@RequestParam(value = "username", required = false)String username,
                                                  @RequestParam(value = "password", required = false)String password) throws Exception{
        String errorInfo = RequestParamsCheckUtils.checkUsernamePassword(username, password);
        if (errorInfo != null) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorInfo);
        }

        //更新用户密码,返回更新状态（true-成功，false-失败）
        String newPassword = SecretUtils.passwordMD5Encrypt(password); //新加密
        boolean updateStatus = userService.alterUserPasswordByName(username, newPassword);
        if (!updateStatus) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.DATABASE_NO_EXIST_USER);
        }

        return new  ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.UPDATE_USER_PASSWORD_SUCCESS);
    }

    /**
     * 6.激活账户
     * @param token
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @RequestMapping(value = "/activation", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO emailToken(@RequestParam(value = "token", required = false)String token) throws Exception{
        String errorInfo = RequestParamsCheckUtils.token(token);
        if (errorInfo != null) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorInfo);
        }

        String plainText = SecretUtils.base64Decrypt(token);
        if(plainText == null){ //解密失败
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.EMAIL_TOKEN_DECRYPT_FAIL);
        }

        //获取 email + 过期时间
        String [] array = plainText.split("-");
        String email = array[0];
        String expireTime = array[1];

        //过期判断（1 天）
        if(StringUtils.isExpire(expireTime)){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.ACCOUNT_ACTIVATION_URL_ALREAD_EXPIRE_TIME);
        }

        //激活账户
        boolean autivationResult =  userService.activationUserByEmail(email);
        if(!autivationResult){ //不存在用户账户激活失败
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.DATABASE_NO_EXIST_USER + AccountInfo.ACCOUNT_ACTIVATION_FAIL);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, email + AccountInfo.ACCOUNT_ACTIVATION_SUCCESS);
    }
}
