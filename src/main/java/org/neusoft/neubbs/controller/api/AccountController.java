package org.neusoft.neubbs.controller.api;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.AccountInfo;
import org.neusoft.neubbs.constant.api.CountInfo;
import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    IUserService userService;

    @Autowired
    IRedisService redisService;

    private static Logger logger = Logger.getLogger(AccountController.class);

    /**
     * 1.获取用户信息（AccountController 默认访问）
     *
     * 注解提示：
     *      @LoginAuthroization 需要登录验证
     *      @AdminRank 需要管理员权限验证
     *      @RequestMapping 指定 api 路径
     *      @RequestParam 的 required 属性声明参数是否必须，默认为true,此处声明 false 表示参数非必须，由 api 内部处理空情况
     *      @RequestBody 将 ResponseJsonDTO 对象，转为 JSON 格式字符串，显示在页面
     *
     * @param username 用户名
     * @return ResponseJsonDTO 数据传输对象
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInfoByName(@RequestParam(value = "username", required = false)String username) throws Exception{
        //@RequestParam 的 required 属性声明参数是否必须，默认为true,此处声明 false 表示参数非必须，由 api 内部处理空情况

        //参数合法性检测
        String errorInfo = RequestParamsCheckUtils.checkUsername(username);
        if (errorInfo != null) {
            //日志打印警告信息
            logger.warn(errorInfo);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorInfo);
        }

        /*
         *获取用户信息
         *      1.数据库查询用户信息，获取 UserDO 对象
         *      2.将 UserDO 对象转为 Map 对象
         */
        UserDO user = userService.getUserInfoByName(username);
        Map<String, Object> userInfoMap = JsonUtils.toMapByObject(user);
        if(userInfoMap == null){
            logger.warn(username + LogWarnInfo.DATABASE_NO_EXIST_USER);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.NO_USER);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, userInfoMap);
    }

    /**
     * 2.登录
     *
     * 注解提示：
     *       @RequestBody 自动将 JSON 格式转为 java 对象
     * @param requestBodyParamsMap  request Body 里的JSON 数据
     * @param request
     * @param response
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json") //指定处理Content-Type 类型
    @ResponseBody
    public ResponseJsonDTO login(@RequestBody Map<String,Object> requestBodyParamsMap,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        //获取 request body 的 JSON 格式参数
        String username = (String)requestBodyParamsMap.get(AccountInfo.USERNAME);
        String password = (String)requestBodyParamsMap.get(AccountInfo.PASSWORD);

        //用户，密码参数合法性检测（链式调用）
        String errorInfo = RequestParamsCheckUtils
                            .putParamKeys(new String[]{AccountInfo.USERNAME, AccountInfo.PASSWORD})
                            .putParamValues(new String[]{username, password})
                            .checkParamsNorm();
        if (errorInfo != null) {
            logger.warn(errorInfo);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorInfo);
        }

        //用户是否存在（与用户账户密码错误抛出一样警告，防止被试出用户是否存在）
        UserDO user = userService.getUserInfoByName(username);
        Map<String, Object> userInfoMap = JsonUtils.toMapByObject(user);
        if (userInfoMap == null) {
            logger.warn(username + LogWarnInfo.DATABASE_NO_EXIST_USER);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.USERNAME_PASSWORD_ERROR);
        }

        //用户是否激活
        if((Integer)userInfoMap.get(AccountInfo.STATE) == 0){
            logger.warn(username + LogWarnInfo.ACCOUNT_NO_ACTIVATION_NO_LOGIN);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.NO_ACTIVATE);
        }

        //用户密码是否正确（密码为 MD5 加密后的密文）
        String cipherText = SecretUtils.encryptUserPassword(password);
        if (!cipherText.equals(userInfoMap.get(AccountInfo.PASSWORD))) {
            logger.warn(username + LogWarnInfo.USER_PASSWORD_NO_MATCH);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.USERNAME_PASSWORD_ERROR);
        }

        /*
         * 通过所有验证
         *      1.获取登录 token（JWT 密文，内含用户 id，name，rank）,储存进Cookie
         *      2.获取上下文对象，获取在线登录对象，在线登录人数 +1
         *      3.api 返回成功状态 + token
         */
        String token = JwtTokenUtils.createToken(user);
        CookieUtils.saveCookie(response, AccountInfo.AUTHENTICATION, token);

        ServletContext application = request.getServletContext();
        Integer onlineLoginUser = (Integer) application.getAttribute(CountInfo.ONLINE_LOGIN_USER);
        if (onlineLoginUser == null) {
            onlineLoginUser = 0;
        }
        application.setAttribute(CountInfo.ONLINE_LOGIN_USER, ++onlineLoginUser);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.AUTHENTICATION, token);
    }

    /**
     * 3.注销
     *
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
        CookieUtils.removeCookie(request, response, AccountInfo.AUTHENTICATION);

        //在线登录人数-1
        ServletContext application = request.getServletContext();
        Integer onlineLoginUser = (Integer)application.getAttribute(CountInfo.ONLINE_LOGIN_USER);
        if(onlineLoginUser != null){
            application.setAttribute(CountInfo.ONLINE_LOGIN_USER, --onlineLoginUser);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }


    /**
     * 4.注册
     * @param requestBodyParamsMap
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO registerUser(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        //获取参数
        String username = (String)requestBodyParamsMap.get(AccountInfo.USERNAME);
        String password = (String)requestBodyParamsMap.get(AccountInfo.PASSWORD);
        String email = (String)requestBodyParamsMap.get(AccountInfo.EMAIL);

        //用户名，密码，邮箱参数合法性检测
        String errorInfo = RequestParamsCheckUtils
                            .putParamKeys(new String[]{AccountInfo.USERNAME, AccountInfo.PASSWORD, AccountInfo.EMAIL})
                            .putParamValues(new String[]{username, password, email})
                            .checkParamsNorm();
        if (errorInfo != null) {
            logger.warn(errorInfo);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorInfo);
        }

        //判断用户名唯一
        UserDO user = userService.getUserInfoByName(username);
        if (user != null) {
            logger.warn(username + LogWarnInfo.DATABASE_ALREAD_EXIST_USER_NO_AGAIN_ADD);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.USERNAME_REGISTERED);
        }

        //注册操作
        user = new UserDO();
            user.setName(username);
            user.setEmail(email);

            //密码MD5加密
            String cipherText = SecretUtils.encryptUserPassword(password);
            user.setPassword(cipherText);

        userService.registerUser(user);

        //注册成功（ //默认往 user 对象，注入用户 id（数据库主键自增））
        int newId = user.getId();
        user = userService.getUserInfoById(newId);
        Map<String, Object> userInfoMap = JsonUtils.toMapByObject(user);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, userInfoMap);
    }

    /**
     * 5.修改密码
     *
     * @param requestBodyParamsMap
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/update-password", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO updateUserPasswordById(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception{
        String username = (String)requestBodyParamsMap.get(AccountInfo.USERNAME);
        String password = (String)requestBodyParamsMap.get(AccountInfo.PASSWORD);

        String errorInfo = RequestParamsCheckUtils
                            .putParamKeys(new String[]{AccountInfo.USERNAME, AccountInfo.PASSWORD})
                            .putParamValues(new String[]{username, password})
                            .checkParamsNorm();
        if (errorInfo != null) {
            logger.warn(errorInfo);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorInfo);
        }

        //更新用户密码（需加密）,返回更新状态（true-成功，false-失败）
        String newPassword = SecretUtils.encryptUserPassword(password);
        if (!userService.alterUserPassword(username, newPassword)) {
            logger.warn(username + LogWarnInfo.DATABASE_NO_EXIST_USER);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.NO_USER);
        }

        return new  ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
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
            logger.warn(errorInfo);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorInfo);
        }

        /*
         * 获取 email 和 expireTime 参数
          *    1. 密文解密成明文
          *    2. 明文分组，获取参数
         */
        String plainText = SecretUtils.decryptBase64(token);
        String [] array = plainText.split("-");
        String email = array[0];
        String expireTime = array[1];

        //过期判断（1 天）
        if(StringUtils.isExpire(expireTime)){
            logger.warn(token + LogWarnInfo.ACCOUNT_ACTIVATION_URL_ALREAD_EXPIRE_TIME);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.LINK_INVALID);
        }

        //激活账户（激活失败，表示不存在用户）
        if (!userService.activationUser(email)) {
            logger.warn(LogWarnInfo.ACCOUNT_ACTIVATION_FAIL_EMAIL_NO_REGISTER + LogWarnInfo.DATABASE_NO_EXIST_USER);
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, AccountInfo.ACTIVATION_FAIL_EMAIL_NO_REGISTER);
        }

        logger.warn(email + LogWarnInfo.ACCOUNT_ACTIVATION_SUCCESSFUL);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.ACTIVATION_SUCCESSFUL);
    }
}
