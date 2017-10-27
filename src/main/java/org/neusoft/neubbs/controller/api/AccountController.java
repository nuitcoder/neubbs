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
import org.neusoft.neubbs.utils.CookieUtil;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.JwtTokenUtil;
import org.neusoft.neubbs.utils.PatternUtil;
import org.neusoft.neubbs.utils.RandomUtil;
import org.neusoft.neubbs.utils.RequestParamsCheckUtil;
import org.neusoft.neubbs.utils.SecretUtil;
import org.neusoft.neubbs.utils.SendEmailUtil;
import org.neusoft.neubbs.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;


/**
 *   账户 api
 *      + 获取用户信息
 *      + 获取用户激活状态
 *      + 登录
 *      + 注销
 *      + 注册
 *      + 修改密码
 *      + 修改邮箱
 *      + 账户激活（发送激活 email）
 *      + 激活账户（验证 token）
 *      + 图片验证码（页面生成图片）
 *      + 检查验证码（比较用户输入是否与图片一致）
 *      + 忘记密码（发送临时密码 email）
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api/account")
public class AccountController {

    private final IUserService userService;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final Producer captchaProducer;

    /**
     * Constructor（自动注入）
     */
    @Autowired
    public AccountController(IUserService userService, ThreadPoolTaskExecutor taskExecutor, Producer captchaProducer) {
        this.userService = userService;
        this.taskExecutor = taskExecutor;
        this.captchaProducer = captchaProducer;
    }

    /**
     * 获取用户信息（AccountController 默认访问）
     *
     * 注解提示：
     *
     * @param username 用户名
     * @param email 邮箱
     * @param request http请求
     * @return ResponseJsonDTO 响应 JSON 传输对象
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInformation(@RequestParam(value = "username", required = false) String username,
                                              @RequestParam(value = "email", required = false) String email,
                                                 HttpServletRequest request) throws Exception {
        /*
         * 获取用户信息
         *      1.判断输入参数（username 或者 email）,优先选择 username，其次 email
         *      2.根据不同参数类型，进行参数检测
         *      3.根据不同参数类型，使用不同方式，获取用户信息
         *      4.判断用户是否存在
         */
        UserDO user;
        if (username != null) {
             //用户名获取
             String usernameErrorInfo = RequestParamsCheckUtil.checkUsername(username);
             if (usernameErrorInfo != null) {
                throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(usernameErrorInfo);
             }

             user = userService.getUserInfoByName(username);
             if (user == null) {
                 throw new AccountErrorException(AccountInfo.NO_USER)
                            .log(username + LogWarnInfo.DATABASE_NO_EXIST_USER);
             }

        } else if (email != null) {
            //邮箱获取
            String emailErrorInfo = RequestParamsCheckUtil.checkEmail(email);
            if (emailErrorInfo != null) {
                throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(emailErrorInfo);
            }

            user = userService.getUserInfoByEmail(email);
            if (user == null) {
                //不存在用户
                throw new AccountErrorException(AccountInfo.NO_USER).log(email + LogWarnInfo.DATABASE_NO_EXIST_USER);
            }

        } else {
            //获取不到 username or email 参数
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR)
                        .log(LogWarnInfo.MISSING_USERNAME_OR_EMAIL_PARAM_NO_GET_ACCOUNT_INFO);
        }

        /*
         * 判断是否登录（Cookie 内是否有参数）
         *      1.未登录，返回 true，不返回用户信息
         *      2.已登录，返回 true，将 UserDO 对象转为 Map ，同时返回用户信息
         */
        String authroization =  CookieUtil.getCookieValue(request, AccountInfo.AUTHENTICATION);
        if (authroization == null) {
            return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
        }

        Map<String, Object> userInfoMap = JsonUtil.toMapByObject(user);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, userInfoMap);
    }

    /**
     * 获取用户激活状态
     *
     * @param username 用户名
     * @return ResponseJsonDTO request-body内JSON数据
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/state", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO verificationActivateState(@RequestParam(value = "username", required = false)String username) throws Exception {
        /*
         *  获取流程
         *      1.参数合法合法性验证
         *      2.用户是否存在
         *      3.用户激活状态判断，返回（true-激活，false-未激活）
         */
        String errorInfo = RequestParamsCheckUtil.checkUsername(username);
        if (errorInfo != null) {
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(errorInfo);
        }

        UserDO user = userService.getUserInfoByName(username);
        if (user == null) {
            throw new AccountErrorException(AccountInfo.NO_USER).log(username + LogWarnInfo.DATABASE_NO_EXIST_USER);
        }

        if (user.getState() == AccountInfo.STATE_FAIL) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL);
        }
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 登录
     *
     * @param requestBodyParamsMap  request-body内JSON数据
     * @param request http请求
     * @param response http响应
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO login(@RequestBody Map<String, Object> requestBodyParamsMap,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取 username 和 password
        String username = (String) requestBodyParamsMap.get(AccountInfo.USERNAME);
        String password = (String) requestBodyParamsMap.get(AccountInfo.PASSWORD);

        /*
         * 参数合法性验证
         *      1.判断用户输入 username 参数类型（用户名 or 用户邮箱）,是否未邮件类型（默认为 false）
         *      2.参数合法性验证（只有符合邮箱格式，才能用邮箱登录）
         */
        StringBuilder errorInfo = new StringBuilder();
        boolean emailType = false;
        if (PatternUtil.matchEmail(username)) {
            String emailErrorInfo = RequestParamsCheckUtil.checkEmail(username);
            if (emailErrorInfo != null) {
                errorInfo.append(emailErrorInfo);
            }

            emailType = true;
        } else {
            String nameErrorInfo = RequestParamsCheckUtil.checkUsername(username);
            if (nameErrorInfo != null) {
                errorInfo.append(nameErrorInfo);
            }
        }
        if (errorInfo.length() == 0) {
            String passwordErrorInfo = RequestParamsCheckUtil.checkPassword(password);
            if (passwordErrorInfo != null) {
                errorInfo.append(passwordErrorInfo);
            }
        }
        if (errorInfo.length() > 0) {
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(errorInfo.toString());
        }

        /*
         * 用户验证
         *      1.用户存在性（用户名 or 邮箱）（与用户账户密码错误抛出一样警告，防止通过 api 试出用户是否存在）
         *      2.用户密码验证,先 MD5 加密，加密结果与数据库内结果对比
         */
        UserDO user = emailType ? userService.getUserInfoByEmail(username) : userService.getUserInfoByName(username);
        if (user == null) {
            throw new AccountErrorException(AccountInfo.USERNAME_OR_PASSWORD_INCORRECT)
                        .log(username + LogWarnInfo.DATABASE_NO_EXIST_USER);
        }
        String cipherText = SecretUtil.encryptUserPassword(password);
        if (!cipherText.equals(user.getPassword())) {
            throw new AccountErrorException(AccountInfo.USERNAME_OR_PASSWORD_INCORRECT)
                        .log(password + LogWarnInfo.USER_PASSWORD_INCORRECT);
        }

        /*
         * 通过验证后处理
         *      1.加密 user 数据，获取 authentication（用作 token，令牌，内含 id ，username，rank，state），存入客户端 Cookie
         *      2.在线登录人数 +1（获取应用上下文对象，获取在线人数对象）
         *      3,构建 api 页面显示 JSON 数据（authentication 和 state）
         *      4.返回成功状态
         */
        String authentication = JwtTokenUtil.createToken(user);
        CookieUtil.saveCookie(response, AccountInfo.AUTHENTICATION, authentication);

        ServletContext application = request.getServletContext();
        Integer onlineLoginUser = (Integer) application.getAttribute(CountInfo.ONLINE_LOGIN_USER);
        if (onlineLoginUser == null) {
            onlineLoginUser = 0;
        }
        application.setAttribute(CountInfo.ONLINE_LOGIN_USER, ++onlineLoginUser);

        Map<String, Object> loginJsonMap = new HashMap<>();
            loginJsonMap.put(AccountInfo.AUTHENTICATION, authentication);
            if (user.getState() == 1) {
                loginJsonMap.put(AccountInfo.STATE, true);
            } else if (user.getState() == 0) {
                loginJsonMap.put(AccountInfo.STATE, false);
            }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, loginJsonMap);
    }

    /**
     * 注销
     *
     * @param request http请求
     * @param response http响应
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @LoginAuthorization
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //删除Cookie
        CookieUtil.removeCookie(request, response, AccountInfo.AUTHENTICATION);

        //在线登录人数-1
        ServletContext application = request.getServletContext();
        Integer onlineLoginUser = (Integer) application.getAttribute(CountInfo.ONLINE_LOGIN_USER);
        if (onlineLoginUser != null) {
            application.setAttribute(CountInfo.ONLINE_LOGIN_USER, --onlineLoginUser);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 注册
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO register(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        String username = (String) requestBodyParamsMap.get(AccountInfo.USERNAME);
        String password = (String) requestBodyParamsMap.get(AccountInfo.PASSWORD);
        String email = (String) requestBodyParamsMap.get(AccountInfo.EMAIL);

        String errorInfo = RequestParamsCheckUtil
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
        UserDO user;
        user = userService.getUserInfoByName(username);
        if (user != null) {
            throw new AccountErrorException(AccountInfo.USERNAME_REGISTERED)
                        .log(username + LogWarnInfo.DATABASE_ALREAD_EXIST_USER);
        }
        user = userService.getUserInfoByEmail(email);
        if (user != null) {
            throw new AccountErrorException(AccountInfo.EMAIL_REGISTERED)
                        .log(email + LogWarnInfo.DATABASE_ALREAD_EXIST_USER);
        }

        //注册操作
        user = new UserDO();
            user.setName(username);
            user.setEmail(email);

            //密码MD5加密
            user.setPassword(SecretUtil.encryptUserPassword(password));

        userService.registerUser(user);

        //注册成功（已注册 UserDO 对象，注入用户id（数据库主键自增））
        int newId = user.getId();
        user = userService.getUserInfoById(newId);

        Map<String, Object> userInfoMap = JsonUtil.toMapByObject(user);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, userInfoMap);
    }

    /**
     * 修改密码
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @return ResponseJsonDTO 相应JSON传输对象
     * @throws Exception 所有异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/update-password", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO updatePassword(@RequestBody Map<String, Object> requestBodyParamsMap,
                                            HttpServletRequest request) throws Exception {
        String username = (String) requestBodyParamsMap.get(AccountInfo.USERNAME);
        String password = (String) requestBodyParamsMap.get(AccountInfo.PASSWORD);

        String errorInfo = RequestParamsCheckUtil
                            .putParamKeys(new String[]{AccountInfo.USERNAME, AccountInfo.PASSWORD})
                            .putParamValues(new String[]{username, password})
                            .checkParamsNorm();
        if (errorInfo != null) {
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(errorInfo);
        }

        //验证 Cookie authentication 是否与用于输入一致
        String authentication = CookieUtil.getCookieValue(request, AccountInfo.AUTHENTICATION);
        UserDO cookieUser = JwtTokenUtil.verifyToken(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
        if (cookieUser == null || !username.equals(cookieUser.getName())) {
            throw new AccountErrorException(AccountInfo.NO_PERMISSION).log(LogWarnInfo.NO_PERMISSION_UPDATE_OTHER_USER);
        }

        //更新用户密码（需加密）
        String newPassword = SecretUtil.encryptUserPassword(password);
        userService.alterUserPassword(username, newPassword);

        return new  ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 修改邮箱
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @param response http响应
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @LoginAuthorization
    @RequestMapping(value = "/update-email", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO updateEmail(@RequestBody Map<String, Object> requestBodyParamsMap,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*
         * 修改邮箱验证
         *      1.参数合法性
         *      2.Cookie authentication 判断用户名是否与输入用户一致（放着篡改他人邮箱）
         *      3.Cookie authentication 判断用户是否激活
         *      4.数据库验证是否已激活
         *      5.数据库验证邮箱是否被注册
         *      6.修改邮箱
         */
        String username = (String) requestBodyParamsMap.get(AccountInfo.USERNAME);
        String email = (String) requestBodyParamsMap.get(AccountInfo.EMAIL);

        String errorInof = RequestParamsCheckUtil
                                .putParamKeys(new String[]{AccountInfo.USERNAME, AccountInfo.EMAIL})
                                .putParamValues(new String[]{username, email})
                                .checkParamsNorm();
        if (errorInof != null) {
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(errorInof);
        }

        String authentication = CookieUtil.getCookieValue(request, AccountInfo.AUTHENTICATION);
        UserDO cookieUser = JwtTokenUtil.verifyToken(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
        if (cookieUser == null || !username.equals(cookieUser.getName())) {
            throw new AccountErrorException(AccountInfo.NO_PERMISSION).log(LogWarnInfo.NO_PERMISSION_UPDATE_OTHER_USER);
        }
        if (cookieUser.getState() == AccountInfo.STATE_SUCCESS) {
            throw new AccountErrorException(AccountInfo.ACCOUNT_ACTIVATED)
                        .log(LogWarnInfo.EMAIL_ACTIVATED_NO_AGAIN_SEND_EMAIL);
        }

        UserDO user = userService.getUserInfoByName(username);
        if (user.getState() == AccountInfo.STATE_SUCCESS) {
              //重新存储 Cookie
              CookieUtil.saveCookie(response, AccountInfo.AUTHENTICATION, JwtTokenUtil.createToken(cookieUser));
              throw new AccountErrorException(AccountInfo.ACCOUNT_ACTIVATED)
                            .log(email + LogWarnInfo.EMAIL_ACTIVATED_NO_AGAIN_SEND_EMAIL);
        }
        if (userService.getUserInfoByEmail(email) != null) {
            throw new AccountErrorException(AccountInfo.EMAIL_REGISTERED).log(email + LogWarnInfo.EMAIL_OCCUPIED);
        }

        //修改邮箱操作
        userService.alterUserEmail(username, email);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 账户激活（发送激活 email）
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/activate", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO sendActivateEmail(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        String email = (String) requestBodyParamsMap.get(AccountInfo.EMAIL);

        String errorInfo = RequestParamsCheckUtil.checkEmail(email);
        if (errorInfo != null) {
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(errorInfo);
        }

        //邮箱是否存在
        UserDO user = userService.getUserInfoByEmail(email);
        if (user == null) {
            throw new AccountErrorException(AccountInfo.EMAIL_NO_REIGSTER)
                        .log(email + LogWarnInfo.EMAIL_NO_REGISTER_NO_SEND_EMAIL);
        }

        //账户激活状态（0-未激活，1-已激活），激活的无法再次发送邮件
        if (user.getState() == AccountInfo.STATE_SUCCESS) {
            throw new AccountErrorException(AccountInfo.ACCOUNT_ACTIVATED)
                        .log(email + LogWarnInfo.EMAIL_ACTIVATED_NO_AGAIN_SEND_EMAIL);
        }

        //构建 token（用户邮箱 + 过期时间）
        long expireTime = System.currentTimeMillis() + AccountInfo.EXPIRE_TIME_ONE_DAY;
        String token = SecretUtil.encryptBase64(email + "-" + expireTime);

        //构建邮件内容
        String content = StringUtil.createEmailActivationHtmlString(AccountInfo.MAIL_ACCOUNT_ACTIVATION_URL + token);

        //发送邮件（Spring 线程池，另启线程），new Runnable(){},lambda 写法
        taskExecutor.execute(
                () -> SendEmailUtil.sendEmail(email, " Neubbs 账户激活", content)
        );

        //返回发送成息信息
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.MAIL_SEND_SUCCESS);
    }

    
    /**
     * 激活账户（ 验证 token ）
     *
     * @param token 传入的 token
     * @return ResponseJsonDTO request-body内JSON数据
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO validateEmailToken(@RequestParam(value = "token", required = false)String token) throws Exception {
        String errorInfo = RequestParamsCheckUtil.checkToken(token);
        if (errorInfo != null) {
            throw new ParamsErrorException(errorInfo).log(errorInfo);
        }

        /*
         * 获取 email 和 expireTime 参数
          *    1. 密文解密成明文（用户邮箱-过期时间）
          *    2. 明文分组，获取参数
         */
        String plainText = SecretUtil.decryptBase64(token);
        String[] array = plainText.split("-");
        String email = array[0];
        String expireTime = array[1];

        //过期判断（1 天）
        if (StringUtil.isExpire(expireTime)) {
            throw new TokenExpireException(AccountInfo.LINK_INVALID)
                        .log(token + LogWarnInfo.ACTIVATION_URL_ALREAD_EXPIRE_TIME);
        }

        //激活账户
        userService.activationUser(email);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, AccountInfo.ACTIVATION_SUCCESSFUL);
    }

    /**
     * 图片验证码（页面生成图片）
     *
     * @param request http请求
     * @param response http响应
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void getCaptchaPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    /**
     * 检查验证码（比较用户输入是否与图片一致）
     *
     * @param captcha 用户输入验证码
     * @param request http请求
     * @return ResponseJsonDTO 响应JSON字符串
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/check-captcha", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO checkCaptcha(@RequestParam(value = "captcha", required = false)String captcha, HttpServletRequest request) throws Exception {
        //参数检查(空与长度)
        String errorInfo = RequestParamsCheckUtil.checkCaptcha(captcha);
        if (errorInfo != null) {
            throw new AccountErrorException(AccountInfo.PARAM_ERROR).log(errorInfo);
        }

        /*
         * 验证码比较
         *      1.是否有生成验证码
         *      2.验证码是否匹配
         */
        String sessionCaptcha = (String) request.getSession().getAttribute(AccountInfo.SESSION_CAPTCHA);
        if (StringUtil.isEmpty(sessionCaptcha)) {
            throw new AccountErrorException(AccountInfo.NO_GENERATE_CAPTCHA).log(LogWarnInfo.NO_GENERATE_CAPTCHA_NO_VERIFY);
        }
        if (!captcha.equals(sessionCaptcha)) {
            throw new AccountErrorException(AccountInfo.CAPTCHA_INCORRECT).log(LogWarnInfo.CAPTCHA_INCORRECT);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 忘记密码（发送临时密码 email）
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/forget-password", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO sendTemporaryPasswordEmail(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        String email = (String) requestBodyParamsMap.get(AccountInfo.EMAIL);

        String errorInfo = RequestParamsCheckUtil.checkEmail(email);
        if (errorInfo != null) {
            throw new ParamsErrorException(AccountInfo.PARAM_ERROR).log(errorInfo);
        }

        UserDO user = userService.getUserInfoByEmail(email);
        if (user == null) {
            throw new AccountErrorException(AccountInfo.EMAIL_NO_REIGSTER)
                        .log(LogWarnInfo.EMAIL_NO_REGISTER_NO_SEND_EMAIL);
        }

        //生成新密码（随机字符串，len = 6）
        String randomPassword = RandomUtil.getRandomString(AccountInfo.FORGET_PASSWORD_RANDOM_LENGTH);

        //修改密码（重新加密）(最后3个字符，为临时密码标识)
        userService.alterUserPassword(user.getName(), SecretUtil.encryptUserPassword(randomPassword));

        //构建邮件内容
        String content = user.getEmail() + " 邮箱用户临时密码为：" + randomPassword + " ，请登陆后尽快修改密码！";

        //另启线程发送邮件，new Runnable(){}，lambda写法
        taskExecutor.execute(
                () -> SendEmailUtil.sendEmail(email, "Neubbs 账户临时密码", content)
        );

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }
}
