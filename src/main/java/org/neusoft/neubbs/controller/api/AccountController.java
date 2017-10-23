package org.neusoft.neubbs.controller.api;

import com.google.code.kaptcha.Producer;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.AccountInfo;
import org.neusoft.neubbs.constant.api.CountInfo;
import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.exception.TokenExpireException;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 *   账户 api
 *      1.获取用户信息
 *      2.登录
 *      3.注销
 *      4.注册
 *      5.修改密码
 *      6.发送账户激活邮件
 *      7.激活账户（ token 验证）
 *      8.图片验证码
 *      9.检查验证码（比较用户输入是否与图片一致）
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private Producer captchaProducer;

    private final  String ACCOUNT_ACTIVATION_URL = "http://localhost:8080/api/account/validate?token=";

    /**
     * 1.获取用户信息（AccountController 默认访问）
     *
     * 注解提示：
     *      @LoginAuthroization 需要登录验证
     *      @AccountActivation 需要激活才能访问
     *      @AdminRank 需要管理员权限验证
     *
     *      @RequestMapping 指定 api 路径
     *      @RequestParam 的 required 属性声明参数是否必须，默认为true,此处声明 false 表示参数非必须，由 api 内部处理空情况
     *      @RequestBody 将 ResponseJsonDTO 对象，转为 JSON 格式字符串，显示在页面
     *
     * @param username 用户名
     * @param request http请求
     * @return ResponseJsonDTO 传输对象,api返回结果
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInfoByName(@RequestParam(value = "username", required = false)String username,
                                             @RequestParam(value = "email", required = false)String email,
                                             HttpServletRequest request) throws Exception{
        //@RequestParam 的 required 属性声明参数是否必须，默认为true,此处声明 false 表示参数非必须，由 api 内部处理空情况

        /*
         * 获取用户信息
         *      1.判断输入参数（username 或者 email）,优先选择 username，其次 email
         *      2.根据不同参数类型，进行参数检测
         *      3.根据不同参数类型，使用不同方式，获取用户信息
         *      4.判断用户是存在
         */
        UserDO user = null;
        if (username != null) {
             //用户名获取
             String usernameErrorInfo = RequestParamsCheckUtils.checkUsername(username);
             if (usernameErrorInfo != null) {
                throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(usernameErrorInfo);
             }

             user = userService.getUserInfoByName(username);
             if(user == null){
                 throw new AccountErrorException(AccountInfo.NO_USER).log(LogWarnInfo.DATABASE_NO_EXIST_USER + username);
             }

        } else if (email != null) {
            //邮箱获取
            String emailErrorInfo = RequestParamsCheckUtils.checkEmail(email);
            if (emailErrorInfo != null) {
                throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(emailErrorInfo);
            }

            user = userService.getUserInfoByEmail(email);
            if (user == null) {
                throw new AccountErrorException(AccountInfo.NO_USER).log(email + LogWarnInfo.DATABASE_NO_EXIST_USER);
            }

        } else {
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(LogWarnInfo.NO_USERNAME_OR_EMAIL_PARAM_NO_GET_ACCOUNT_INFO);
        }

        /*
         * 判断是否登录（Cookie 内是否有参数）
         *      1.未登录，返回 true，不返回用户信息
         *      2.已登录，返回 true，将 UserDO 对象转为 Map ，同时返回用户信息
         */
        String authroization =  CookieUtils.getCookieValue(request, AccountInfo.AUTHENTICATION);;
        if (authroization == null) {
            return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
        }

        Map<String, Object> userInfoMap = JsonUtils.toMapByObject(user);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, userInfoMap);
    }

    /**
     * 2.登录
     *
     * 注解提示：
     *       @RequestBody 自动将 JSON 格式转为 log 对象
     *
     * @param requestBodyParamsMap  http请求，request body 内 JSON 数据
     * @param request http请求
     * @param response http响应
     * @return ResponseJsonDTO 传输对象
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
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(errorInfo);
        }

        //用户是否存在（与用户账户密码错误抛出一样警告，防止被试出用户是否存在）(登录注册的，api 返回信息，统一是用户密码错误(包含用户不存在场景))
        UserDO user = userService.getUserInfoByName(username);
        Map<String, Object> userInfoMap = JsonUtils.toMapByObject(user);
        if (userInfoMap == null) {
            throw new AccountErrorException(AccountInfo.USERNAME_PASSWORD_ERROR).log(LogWarnInfo.DATABASE_NO_EXIST_USER +  username);
        }

        //用户密码是否正确（密码为 MD5 加密后的密文）
        String cipherText = SecretUtils.encryptUserPassword(password);
        if (!cipherText.equals(userInfoMap.get(AccountInfo.PASSWORD))) {
            throw new AccountErrorException(AccountInfo.USERNAME_PASSWORD_ERROR).log(password + LogWarnInfo.USER_PASSWORD_NO_MATCH);
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
     * @param request http请求
     * @param response http响应
     * @return ResponseJsonDTO 传输对象，api 显示结果
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
     *
     * @param requestBodyParamsMap http请求，request body 内 JSON 数据
     * @return ResponseJsonDTO 传输对象，api 显示结果
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
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(errorInfo);
        }

        /*
         * 判断用户是否注册
         *      1.检测用户名
         *      2.检测邮箱
         */
        UserDO user = null;
        user = userService.getUserInfoByName(username);
        if (user != null) {
            throw new AccountErrorException(AccountInfo.USERNAME_REGISTERED).log(username + LogWarnInfo.DATABASE_ALREAD_EXIST_USER_NO_AGAIN_ADD);
        }
        user = userService.getUserInfoByEmail(email);
        if (user != null) {
            throw new AccountErrorException(AccountInfo.EMAIL_REGISTERED).log(email + LogWarnInfo.DATABASE_ALREAD_EXIST_USER_NO_AGAIN_ADD);
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
     * @param requestBodyParamsMap http请求，request body 内 JSON 数据
     * @return ResponseJsonDTO 传输对象，api显示结果
     * @throws Exception
     */
    @LoginAuthorization @AccountActivation
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
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(errorInfo);
        }

        //更新用户密码（需加密）,返回更新状态（true-成功，false-失败）
        String newPassword = SecretUtils.encryptUserPassword(password);
        if (!userService.alterUserPassword(username, newPassword)) {
            throw new AccountErrorException(AccountInfo.NO_USER).log(LogWarnInfo.DATABASE_NO_EXIST_USER + username);
        }

        return new  ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 6.发送账户激活邮件
     *
     * @param requestBodyParamsMap http请求，request body 内 JSON 数据
     * @return ResponseJsonDTO 传输对象，api 显示结果
     * @throws Exception
     */
    @RequestMapping(value = "/activate", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO accountActivation(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception{
        String email = (String)requestBodyParamsMap.get(AccountInfo.EMAIL);

        String errorInfo = RequestParamsCheckUtils.checkEmail(email);
        if (errorInfo != null) {
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(errorInfo);
        }

        //检测数据库是否存在此邮箱
        UserDO user = userService.getUserInfoByEmail(email);
        if(user == null){
            throw new AccountErrorException(AccountInfo.EMAIL_NO_REIGSTER_NO_SEND).log(email + LogWarnInfo.EMAIL_NO_REGISTER_NO_SEND_EMAIL);
        }

        //检测邮箱激活状态（0-未激活，1-已激活）
        if(user.getState() == 1){
            throw new AccountErrorException(AccountInfo.EMAIL_ACTIVATED).log(email + LogWarnInfo.EMAIL_ACTIVATED_NO_AGAIN_SEND_EMAIL);
        }

        //构建 token（用户邮箱 + 过期时间）
        long expireTime = System.currentTimeMillis() + SecretInfo.EXPIRETIME_ONE_DAY;
        String token = SecretUtils.encryptBase64(email + "-" + expireTime);

        //构建邮件内容
        String content = StringUtils.createEmailActivationHtmlString(ACCOUNT_ACTIVATION_URL + token);

        //发送邮件（Spring 线程池，另启线程）
        taskExecutor.execute(new Runnable(){
            @Override
            public void run() {
                SendEmailUtils.sendEmail(email, AccountInfo.EMAIL_SUBJECT_ACCOUNT_ACTIVATION , content);
            }
        });

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.MAIL_SENT_SUCCESS);
    }


    /**
     * 7.激活账户（ token 验证）
     *
     * @param token 密文
     * @return ResponseJsonDTO 传输对象，api 显示结果
     * @throws Exception
     */
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO emailToken(@RequestParam(value = "token", required = false)String token) throws Exception{
        String errorInfo = RequestParamsCheckUtils.checkToken(token);
        if (errorInfo != null) {
            throw new ParamsErrorException(errorInfo).log(errorInfo);
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
            throw new TokenExpireException(AccountInfo.LINK_INVALID).log(token + LogWarnInfo.ACTIVATION_URL_ALREAD_EXPIRE_TIME);
        }

        //激活账户（激活失败，表示不存在用户）
        if (!userService.activationUser(email)) {
            throw new AccountErrorException(AccountInfo.ACTIVATION_FAIL_EMAIL_NO_REGISTER).log(email + LogWarnInfo.ACTIVATION_FAIL_EMAIL_NO_REGISTER);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.ACTIVATION_SUCCESSFUL);
    }

    /**
     * 8.图片验证码
     *
     * @param request http请求
     * @param response http响应
     * @return ResponseJsonDTO 传输对象，api 显示结果
     * @throws Exception
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception{
        /*
         * 生成验证码步骤
         *      1.设置 response
         *      2.生成验证码（文本 or 数字）
         *      3.存放至 Session
         *      4.,生成图像（jpg格式），页面输出
         */
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();
        request.getSession().setAttribute(AccountInfo.SESSION_CAPTCHA, capText);

        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try{
            out.flush();
        } finally {
            out.close();
        }

        return null;
    }

    /**
     * 9.检查验证码（比较用户输入是否与图片一致）
     *
     * @param captcha 验证码字符串
     * @param request http请求
     * @return ResponseJsonDTO 传输对象，api 显示结果
     * @throws Exception
     */
    @RequestMapping(value = "/check-captcha", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO checkCaptcha(@RequestParam(value = "captcha", required = false)String captcha, HttpServletRequest request) throws Exception{
        //参数检查(空与长度)
        String errorInfo = RequestParamsCheckUtils.checkCaptcha(captcha);
        if (errorInfo != null) {
            throw new AccountErrorException(AccountInfo.PARAM_ERROR).log(errorInfo);
        }

        /*
         * 验证码比较
         *      1.是否有生成验证码
         *      2.验证码是否匹配
         */
        String sessionCaptcha = (String)request.getSession().getAttribute(AccountInfo.SESSION_CAPTCHA);
        if(StringUtils.isEmpty(sessionCaptcha)){
            throw new AccountErrorException(AccountInfo.NO_GENERATE_CAPTCHA).log(LogWarnInfo.NO_GENERATE_CAPTCHA_NO_CHECK);
        }
        if(!captcha.equals(sessionCaptcha)){
            throw new AccountErrorException(AccountInfo.CAPTCHA_ERROR).log(LogWarnInfo.CAPTCHA_ERROR_NO_MATCH);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }
}
