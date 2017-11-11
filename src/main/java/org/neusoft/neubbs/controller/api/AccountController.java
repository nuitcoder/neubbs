package org.neusoft.neubbs.controller.api;

import com.google.code.kaptcha.Producer;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.exception.TokenErrorException;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.entity.properties.NeubbsConfigDO;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.utils.CookieUtil;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.JwtTokenUtil;
import org.neusoft.neubbs.utils.MapFilterUtil;
import org.neusoft.neubbs.utils.PatternUtil;
import org.neusoft.neubbs.utils.RandomUtil;
import org.neusoft.neubbs.utils.RequestParamCheckUtil;
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
import java.io.IOException;
import java.util.LinkedHashMap;
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
public final class AccountController {

    private final IUserService userService;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final Producer captchaProducer;
    private final IRedisService redisService;
    private final NeubbsConfigDO neubbsConfig;

    /**
     * Constructor（自动注入）
     */
    @Autowired
    private AccountController(IUserService userService, ThreadPoolTaskExecutor taskExecutor,
                              Producer captchaProducer, IRedisService redisService,
                              NeubbsConfigDO neubbsConfig) {
        this.userService = userService;
        this.taskExecutor = taskExecutor;
        this.captchaProducer = captchaProducer;
        this.redisService = redisService;
        this.neubbsConfig = neubbsConfig;
    }

    /**
     * 获取用户信息（AccountController 默认访问）
     *
     * 业务流程
     *      A.参数处理
     *          - 两个参数存在性（至少存一）
     *          - 优先考虑 username，根据类型，执行参数检查
     *
     *      B.数据库信息获取
     *          - 不同参数类型，不同方式获取
     *
     *      C.判断登录状态
     *          - 获取认证信息（Cookie 内 authentication，得到 JWT加密数据）
     *              <1>未登录，返回 true，不返回用户信息
     *              <2>已登录，返回 true，同时返回用户信息（需将 UserDO 通过 JsonUtil 转为 Map，通过 MapFilter 过滤无用信息）
     *
     *
     * @param username 用户名
     * @param email 邮箱
     * @param request http请求
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws AccountErrorException 账户错误异常
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInformation(@RequestParam(value = "username", required = false) String username,
                                              @RequestParam(value = "email", required = false) String email,
                                              HttpServletRequest request)
            throws ParamsErrorException, AccountErrorException {

        //参数处理
        if (username == null && email == null) {
            throw new ParamsErrorException(ApiMessage.PARAM_ERROR).log(LogWarn.ACCOUNT_13);
        }

        boolean emailType;
        if (username != null) {
            emailType = PatternUtil.matchEmail(username);
            String checkType = emailType ? ParamConst.EMAIL : ParamConst.USERNAME;
            RequestParamCheckUtil.check(checkType, username);
        } else {
            emailType = true;
            RequestParamCheckUtil.check(ParamConst.EMAIL, email);
        }

        //数据库信息获取
        UserDO user;
        if (username != null && emailType) {
            user = userService.getUserInfoByEmail(username);
        } else {
            user = emailType ? userService.getUserInfoByEmail(email) : userService.getUserInfoByName(username);
        }

        //判断登录状态
        if (CookieUtil.getCookieValue(request, ParamConst.AUTHENTICATION) == null) {
            return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
        }

        Map<String, Object> userInfoMap = JsonUtil.toMapByObject(user);
        MapFilterUtil.filterUserInfo(userInfoMap);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, userInfoMap);
    }

    /**
     * 获取用户激活状态
     *
     * 业务流程
     *      - 参数检查（username）
     *      - 数据库获取用户信息
     *      - 返回用户激活状态（true-激活，false-未激活）
     *
     * @param username 用户名
     * @return ResponseJsonDTO request-body内JSON数据
     * @throws ParamsErrorException 参数错误异常
     * @throws AccountErrorException 账户错误异常
     */
    @RequestMapping(value = "/state", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserActivateState(@RequestParam(value = "username", required = false) String username)
            throws ParamsErrorException, AccountErrorException {

        RequestParamCheckUtil.check(ParamConst.USERNAME, username);

        UserDO user = userService.getUserInfoByName(username);

        if (user.getState() == SetConst.ACCOUNT_STATE_FALSE) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL);
        }
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 登录
     *
     * 业务流程
     *      A.参数处理
     *          - 判断 username 类型（用户 or 邮箱，符合邮箱类型，emailType = true）
     *          - 参数合法检查（构建 paramsMap，传入检查器）
     *
     *      B.数据库验证
     *          - 获取用户信息（username or email 获取）
     *          - 用户输入密码加密，加密结果与数据库用户名对比
     *
     *      C.通过验证后处理
     *          - JWT 加密用户信息（获取 token，存入客户端 Cookie[key = authentication]，内含 id，username，rank，state）
     *          - 在线登录人数 +1（获取应用上下文对象，获取在线人数对象，自增）
     *          - 返回成功状态 + model 信息（authentication 和 state[用户激活状态，1 = true，0 = false]）     *
     *
     * @param requestBodyParamsMap  request-body内JSON数据
     * @param request http请求
     * @param response http响应
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws AccountErrorException 账户错误异常
     * @throws TokenErrorException 口令错误异常
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO login(@RequestBody Map<String, Object> requestBodyParamsMap,
                                 HttpServletRequest request, HttpServletResponse response)
            throws ParamsErrorException, AccountErrorException, TokenErrorException {
        //参数处理
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String password = (String) requestBodyParamsMap.get(ParamConst.PASSWORD);

        boolean emailType = PatternUtil.matchEmail(username);
        String usernameType = emailType ? ParamConst.EMAIL : ParamConst.USERNAME;

        Map<String, String> paramsMap = new LinkedHashMap<>(SetConst.SIZE_TWO);
        paramsMap.put(usernameType, username);
        paramsMap.put(ParamConst.PASSWORD, password);
        RequestParamCheckUtil.check(paramsMap);

        //数据库验证
        UserDO user;
        try {
            user = emailType ? userService.getUserInfoByEmail(username) : userService.getUserInfoByName(username);
        } catch (AccountErrorException ae) {
            throw new AccountErrorException(ApiMessage.USERNAME_OR_PASSWORD_INCORRECT).log(ae.getLogMessage());
        }

        String cipherText = SecretUtil.encryptUserPassword(password);
        if (!cipherText.equals(user.getPassword())) {
            throw new AccountErrorException(ApiMessage.USERNAME_OR_PASSWORD_INCORRECT)
                    .log(username + LogWarn.ACCOUNT_09);
        }

        //通过验证后处理
        String authentication = JwtTokenUtil.createToken(user);
        CookieUtil.saveCookie(response, ParamConst.AUTHENTICATION, authentication,
                neubbsConfig.getCookieAutoLoginMaxAgeDay());

        ServletContext context = request.getServletContext();
        int onlineLoginUser = (int) context.getAttribute(ParamConst.COUNT_LOGIN_USER);
        context.setAttribute(ParamConst.COUNT_LOGIN_USER, ++onlineLoginUser);

        Map<String, Object> loginJsonMap = new LinkedHashMap<>(SetConst.LENGTH_TWO);
        loginJsonMap.put(ParamConst.AUTHENTICATION, authentication);
        loginJsonMap.put(ParamConst.STATE, user.getState() == SetConst.ACCOUNT_STATE_TRUE);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, loginJsonMap);
    }

    /**
     * 注销
     *
     * 业务流程
     *      - 删除客户端 Cookie[key = authentication]
     *      - 在线登录人数 -1
     *      - 返回成功状态
     *
     * @param request http请求
     * @param response http响应
     * @return ResponseJsonDTO 响应JSON传输对象
     */
    @LoginAuthorization
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.removeCookie(request, response, ParamConst.AUTHENTICATION);

        ServletContext context = request.getServletContext();
        int onlineLoginUser = (int) context.getAttribute(ParamConst.COUNT_LOGIN_USER);
        context.setAttribute(ParamConst.COUNT_LOGIN_USER, --onlineLoginUser);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 注册
     *
     * 业务流程
     *      A.参数处理
     *          - 获取参数（username, password, email）
     *          - 参数检测
     *
     *      B.数据库处理
     *          - 检测用户名是否被占用
     *          - 检测邮箱
     *          - 构建用户，注册
     *          - 根据 id 重新获取用户数据
     *
     *      C.返回成功状态
     *          - 构建 userInfoMap, 使用 MapFilterUtil 过滤信息
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws AccountErrorException 账户错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO register(@RequestBody Map<String, Object> requestBodyParamsMap)
            throws ParamsErrorException, AccountErrorException, DatabaseOperationFailException {

        //参数处理
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String password = (String) requestBodyParamsMap.get(ParamConst.PASSWORD);
        String email = (String) requestBodyParamsMap.get(ParamConst.EMAIL);

        Map<String, String> paramsMap = new LinkedHashMap<>(SetConst.SIZE_THREE);
        paramsMap.put(ParamConst.USERNAME, username);
        paramsMap.put(ParamConst.PASSWORD, password);
        paramsMap.put(ParamConst.EMAIL, email);
        RequestParamCheckUtil.check(paramsMap);

        //数据库处理
        userService.isOccupyByUsername(username);
        userService.isOccupyByEmail(email);

        UserDO user = new UserDO();
        user.setName(username);
        user.setEmail(email);
        user.setPassword(SecretUtil.encryptUserPassword(password));

        userService.registerUser(user);

        UserDO dbUser = userService.getUserInfoById(user.getId());

        //返回成功状态
        Map<String, Object> userInfoMap = JsonUtil.toMapByObject(dbUser);
        MapFilterUtil.filterUserInfo(userInfoMap);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, userInfoMap);
    }

    /**
     * 修改密码
     *
     * 业务流程：
     *      A.参数处理
     *          - 获取参数（username，password）
     *          - 参数检查
     *
     *      B.权限安全
     *          - 获取登录认证信息（客户端 Cookie[authentication]）
     *          - 比较"登录"用户名和"输入"用户名是否一致（防止篡改他人密码）
     *
     *      C.更改密码
     *          - 更新数据库密码（需对新密码进行 MD5 加密，保存加密后结果）
     *          - 返回成功状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws AccountErrorException 账户错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/update-password", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO updatePassword(@RequestBody Map<String, Object> requestBodyParamsMap,
                                          HttpServletRequest request)
            throws ParamsErrorException, AccountErrorException, DatabaseOperationFailException {

        //参数处理
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String newPassword = (String) requestBodyParamsMap.get(ParamConst.PASSWORD);

        Map<String, String> paramsMap = new LinkedHashMap<>(SetConst.SIZE_TWO);
            paramsMap.put(ParamConst.USERNAME, username);
            paramsMap.put(ParamConst.PASSWORD, newPassword);
        RequestParamCheckUtil.check(paramsMap);

        //权限安全
        String authentication = CookieUtil.getCookieValue(request, ParamConst.AUTHENTICATION);
        UserDO cookieUser = JwtTokenUtil.verifyToken(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
        if (cookieUser == null || !username.equals(cookieUser.getName())) {
            throw new AccountErrorException(ApiMessage.NO_PERMISSION).log(LogWarn.ACCOUNT_12);
        }

        //C.更新密码
        userService.alterUserPassword(username, SecretUtil.encryptUserPassword(newPassword));
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 修改邮箱
     *
     * 业务流程：
     *      A.参数处理
     *          - 获取参数（username，email）
     *          - 参数检查
     *
     *      B.权限安全
     *          - 获取登录登录认证信息（Cookie[authentication]）
     *
     *      C.数据库验证
     *          - 数据库验证用户是否激活（已激活，重储存 Cookie）
     *          - 邮箱是否被占用
     *
     *      D.更改邮箱
     *          - 数据库更改邮箱
     *          - 返回成功状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @param response http响应
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws AccountErrorException 账户错误异常
     * @throws TokenErrorException 口令错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
    @LoginAuthorization
    @RequestMapping(value = "/update-email", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO updateEmail(@RequestBody Map<String, Object> requestBodyParamsMap,
                                       HttpServletRequest request, HttpServletResponse response)
            throws ParamsErrorException, AccountErrorException, TokenErrorException, DatabaseOperationFailException {

        //参数处理
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String email = (String) requestBodyParamsMap.get(ParamConst.EMAIL);

        Map<String, String> paramsMap = new LinkedHashMap<>(SetConst.SIZE_TWO);
        paramsMap.put(ParamConst.USERNAME, username);
        paramsMap.put(ParamConst.EMAIL, email);
        RequestParamCheckUtil.check(paramsMap);

        //权限安全
        String authentication = CookieUtil.getCookieValue(request, ParamConst.AUTHENTICATION);
        UserDO cookieUser = JwtTokenUtil.verifyToken(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
        if (cookieUser == null || !username.equals(cookieUser.getName())) {
            throw new AccountErrorException(ApiMessage.NO_PERMISSION).log(LogWarn.ACCOUNT_12);
        }

        //数据库验证
        if (userService.getUserInfoByName(username).getState() == SetConst.ACCOUNT_STATE_TRUE) {
            CookieUtil.saveCookie(response, ParamConst.AUTHENTICATION, JwtTokenUtil.createToken(cookieUser),
                    neubbsConfig.getCookieAutoLoginMaxAgeDay());
            throw new AccountErrorException(ApiMessage.ACCOUNT_ACTIVATED).log(LogWarn.ACCOUNT_07);
        }
        userService.isOccupyByEmail(email);


        //更改邮箱
        userService.alterUserEmail(username, email);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 账户激活（发送激活 email）
     *
     * 业务流程：
     *      A.参数处理
     *          - 获取 email，执行参数检查
     *
     *      B.数据库判断
     *          - 邮箱是否被占用
     *          - 用户是否激活（已激活的无法发送邮件）
     *
     *      C.邮件定时器检查
     *          - 检查是否有能发送过期
     *          - 不能发送，返回倒计时
     *
     *      C.发送邮件
     *          - 构建 token（用户邮箱 + 过期时间戳(当天24点)，使用 Base64 加密）
     *          - 构建邮件内容（生成 Html 格式）
     *          - 发送邮件（Spring 线程池，taskExecutor 另调用线程执行发送邮件任务，采用 lambda 写法）
     *
     *       D.邮箱时限
     *          - 邮箱保存至 redis（key-value， 60 * 1000 ms 过期）
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws AccountErrorException 账户错误异常
     */
    @RequestMapping(value = "/activate", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO sendActivateEmail(@RequestBody Map<String, Object> requestBodyParamsMap)
            throws ParamsErrorException, AccountErrorException {

        //参数处理
        String email = (String) requestBodyParamsMap.get(ParamConst.EMAIL);
        RequestParamCheckUtil.check(ParamConst.EMAIL, email);

        //数据库判断
        if (userService.getUserInfoByEmail(email).getState() == SetConst.ACCOUNT_STATE_TRUE) {
            throw new AccountErrorException(ApiMessage.ACCOUNT_ACTIVATED).log(email + LogWarn.ACCOUNT_07);
        }

        //邮箱定时器检查
        long emailkeyExpireTime = redisService.getExpireTime(email);
        if (emailkeyExpireTime != SetConst.REDIS_EXPIRED) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, ApiMessage.WATI_TIMER,
                    "timer", emailkeyExpireTime / SetConst.THOUSAND);
        }

        //发送邮件
        String token = SecretUtil.encryptBase64(email + "-" + StringUtil.getTwentyFourClockTime());
        String emailContent = StringUtil
                .createEmailActivationHtmlString(neubbsConfig.getAccountApiVaslidateUrl() + token);

        taskExecutor.execute(
                () -> SendEmailUtil.sendEmail("Neubbs", email, " Neubbs 账户激活", emailContent)
        );

        //邮箱时限
        redisService.save(email, "activate", SetConst.EXPIRE_TIME_MS_SIXTY_SECOND);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, ApiMessage.MAIL_SEND_SUCCESS);
    }


    /**
     * 激活账户（ 验证 token ）
     *
     * 业务流程：
     *      A.参数处理
     *          - 检查 token
     *
     *      B.token 解析
     *          - 解密 token（Base64 解密，获取用户邮箱 + 过期时间）
     *          - 参数检查
     *          - 判断 token 是否过期
     *
     *      C.激活用户
     *          - 数据库激活账户
     *          - 返回成功状态
     *
     * @param token 传入的 token
     * @return ResponseJsonDTO request-body内JSON数据
     * @throws ParamsErrorException 参数错误异常
     * @throws TokenErrorException 口令错误异常
     * @throws AccountErrorException 账户错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO validateEmailToken(@RequestParam(value = "token", required = false) String token)
            throws ParamsErrorException, TokenErrorException, AccountErrorException, DatabaseOperationFailException {

        //参数处理
        RequestParamCheckUtil.check(ParamConst.TOKEN, token);

        //解析 token
        String plainText = SecretUtil.decryptBase64(token);
        String[] array = plainText.split("-");
        if (array.length != SetConst.LENGTH_TWO) {
            throw new TokenErrorException(ApiMessage.IVALID_TOKEN).log(token + LogWarn.ACCOUNT_15);
        }

        String email = array[0];
        String expireTime = array[1];
        if (!PatternUtil.matchEmail(email)) {
            throw new TokenErrorException(ApiMessage.IVALID_TOKEN).log(token + LogWarn.ACCOUNT_15);
        }

        if (StringUtil.isExpire(expireTime)) {
            throw new TokenErrorException(ApiMessage.LINK_INVALID).log(token + LogWarn.ACCOUNT_05);
        }

        //激活用户
        userService.activationUser(email);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, ApiMessage.ACTIVATION_SUCCESSFUL);
    }

    /**
     * 图片验证码（页面生成图片）
     *
     * 业务流程：
     *      - 设置 response
     *      - 生成验证码（随机文本 + 数字）
     *      - 验证码储存到 Sessin
     *      - 生成图像（jpg 格式）
     *      - 页面输出
     *
     * @param request http请求
     * @param response http响应
     * @throws AccountErrorException 账户错误异常
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void getCaptchaPicture(HttpServletRequest request, HttpServletResponse response)
            throws AccountErrorException {

        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();

        request.getSession().setAttribute(SetConst.SESSION_CAPTCHA, capText);

        BufferedImage bi = captchaProducer.createImage(capText);

        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new AccountErrorException(ApiMessage.GENERATE_CAPTCHA_FAIL).log(LogWarn.ACCOUNT_17);
        }
    }

    /**
     * 检查验证码（比较用户输入是否与图片一致）
     *
     * 业务流程：
     *      A.参数处理
     *          - 检查 captcha（用户输入验证码）
     *
     *      B.检查验证码是否正确
     *          - 获取 Session 内验证码
     *          - 用户输入与 Session 储存验证进行对比
     *          - 返回成功状态
     *
     * @param captcha 用户输入验证码
     * @param request http请求
     * @return ResponseJsonDTO 响应JSON字符串
     * @throws ParamsErrorException 参数错误异常
     * @throws AccountErrorException 账户错误异常
     */
    @RequestMapping(value = "/check-captcha", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO checkCaptcha(@RequestParam(value = "captcha", required = false)String captcha,
                                        HttpServletRequest request)
            throws ParamsErrorException, AccountErrorException {

        //参数处理
        RequestParamCheckUtil.check(ParamConst.CAPTCHA, captcha);

        //检查验证码是否正确
        String sessionCaptcha = (String) request.getSession().getAttribute(SetConst.SESSION_CAPTCHA);
        if (sessionCaptcha == null) {
            throw new AccountErrorException(ApiMessage.NO_GENERATE_CAPTCHA).log(LogWarn.ACCOUNT_10);
        }
        if (!captcha.equals(sessionCaptcha)) {
            throw new AccountErrorException(ApiMessage.CAPTCHA_INCORRECT).log(LogWarn.ACCOUNT_11);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 忘记密码（发送临时密码到指定 email）
     *
     * 业务流程
     *      A.参数处理
     *          - 检查 email
     *
     *      C.更改密码
     *          - 判断邮箱用户是否存在
     *          - 生成新密码（随机字符串，length = 6）
     *          - 数据库更改密码（MD5 加密新密码，数据库储存加密结果）
     *
     *      D.发送邮件
     *          - 构建邮件内容
     *          - 异步发送邮件（Spring 线程池，另调用 taskExecutor 线程执行发送邮件任务，lambda 写法）
     *          - 返回成功状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws AccountErrorException 账户错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
    @RequestMapping(value = "/forget-password", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO sendTemporaryPasswordEmail(@RequestBody Map<String, Object> requestBodyParamsMap)
            throws ParamsErrorException, AccountErrorException, DatabaseOperationFailException {

        //参数处理
        String email = (String) requestBodyParamsMap.get(ParamConst.EMAIL);
        RequestParamCheckUtil.check(ParamConst.EMAIL, email);

        //更改密码
        UserDO user = userService.getUserInfoByEmail(email);
        String randomPassword = RandomUtil.getRandomString(SetConst.FORGET_PASSWORD_RANDOM_LENGTH);
        userService.alterUserPassword(user.getName(), SecretUtil.encryptUserPassword(randomPassword));

        //D.发送邮件
        String emailContent = user.getEmail() + " 邮箱用户临时密码为：" + randomPassword + " ，请登陆后尽快修改密码！";
        taskExecutor.execute(
                () -> SendEmailUtil.sendEmail("Neubbs", email, "Neubbs 账户临时密码", emailContent)
        );

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }
}
