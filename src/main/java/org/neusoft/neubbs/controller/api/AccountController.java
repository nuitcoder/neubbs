package org.neusoft.neubbs.controller.api;

import com.google.code.kaptcha.Producer;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
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
public final class AccountController {

    private final IUserService userService;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final Producer captchaProducer;

    /**
     * Constructor（自动注入）
     */
    @Autowired
    private AccountController(IUserService userService, ThreadPoolTaskExecutor taskExecutor, Producer captchaProducer) {
        this.userService = userService;
        this.taskExecutor = taskExecutor;
        this.captchaProducer = captchaProducer;
    }

    /**
     * 获取用户信息（AccountController 默认访问）
     *
     * 业务流程
     *      A.参数处理
     *          1.判断输入参数（是否存在 username or email）
     *          2.判断参数类型（若 username 不为空，默认为用户名类型）（username 可支持用户名和邮箱）
     *          3.参数检查（不同参数类型，不同方式检查）
     *
     *      B.数据库获取账户信息
     *          1.不同参数类型，不同方式获取
     *
     *      B.判断登录状态
     *          1.获取认证信息（Cookie 内 authentication，得到 JWT加密数据）
     *              <1-a>未登录，返回 true，不返回用户信息
     *              <1-b>已登录，返回 true，同时返回用户信息（需将 UserDO 通过 JsonUtil 转为 Map，通过 MapFilter 过滤无用信息）
     *
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
        //参数处理
        if (StringUtil.isEmpty(username) && StringUtil.isEmpty(email)) {
            throw new ParamsErrorException(ApiMessage.PARAM_ERROR).log(LogWarn.ACCOUNT_13);
        }

        boolean emailType = false;
        if (!StringUtil.isEmpty(username)) {
            emailType = PatternUtil.matchEmail(username);
            if (emailType) {
                RequestParamCheckUtil.check(ParamConst.EMAIL, username);
            } else {
                RequestParamCheckUtil.check(ParamConst.USERNAME, username);
            }
        } else if (!StringUtil.isEmpty(email)) {
            emailType = true;
            RequestParamCheckUtil.check(ParamConst.EMAIL, email);
        }

        //数据库获取用户信息
        UserDO user;
        if (username != null && emailType) {
            user = userService.getUserInfoByEmail(username);
        } else {
            user = emailType ? userService.getUserInfoByEmail(email)
                             : userService.getUserInfoByName(username);
        }

        //判断登录状态
        String authentication =  CookieUtil.getCookieValue(request, ParamConst.AUTHENTICATION);
        if (authentication == null) {
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
     *      1.参数检查（username）
     *      2.用户是否存在
     *      3.返回用户激活状态（true-激活，false-未激活）
     *
     * @param username 用户名
     * @return ResponseJsonDTO request-body内JSON数据
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/state", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserActivateState(@RequestParam(value = "username", required = false) String username)
                                                    throws Exception {
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
     *      A.参数合法性验证
     *          1.判断参数类型（用户名 or email，必须符合邮箱规范，才属于邮箱类型，否则为用户名类型）
     *          2.参数合法性检测（不同类型，不同检测）
     *
     *      B.用户验证
     *          1.获取用户信息（不同类型，不同方式获取，进行存在性判断）（与用户账户密码错误抛出一样警告，防止通过 api 试出用户是否存在）
     *          2.用户密码验证,先 MD5 加密，加密结果与数据库内结果对比     *
     *
     *      C.通过所有验证后处理
     *          1.JWT 加密用户信息（获取 token，存入客户端 Cookie[key = authentication]，内含 id，username，rank，state）
     *          2.在线登录人数+1（获取应用上下文对象，获取在线人数对象）
     *          3.返回成功状态 + model 信息（authentication 和 state[用户激活状态，如果1-true，0=false]）     *
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
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String password = (String) requestBodyParamsMap.get(ParamConst.PASSWORD);

        //参数合法性验证
        boolean emailType = PatternUtil.matchEmail(username);
        String usernameParamsType = emailType ? ParamConst.EMAIL : ParamConst.USERNAME;

        Map<String, String> typeParamMap = new HashMap<>(SetConst.SIZE_TWO);
            typeParamMap.put(usernameParamsType, username);
            typeParamMap.put(ParamConst.PASSWORD, password);
        RequestParamCheckUtil.check(typeParamMap);

        //用户验证
        UserDO user;
        try {
            user = emailType ? userService.getUserInfoByEmail(username) : userService.getUserInfoByName(username);
        } catch (AccountErrorException ae) {
            throw new AccountErrorException(ApiMessage.USERNAME_OR_PASSWORD_INCORRECT)
                    .log(username + LogWarn.ACCOUNT_01);
        }

        String cipherText = SecretUtil.encryptUserPassword(password);
        if (!cipherText.equals(user.getPassword())) {
            throw new AccountErrorException(ApiMessage.USERNAME_OR_PASSWORD_INCORRECT)
                        .log(password + LogWarn.ACCOUNT_09);
        }

        //通过所有验证后处理
        String authentication = JwtTokenUtil.createToken(user);
        CookieUtil.saveCookie(response, ParamConst.AUTHENTICATION, authentication);

        ServletContext application = request.getServletContext();
        int onlineLoginUser = (Integer) application.getAttribute(ParamConst.COUNT_LOGIN_USER);
        application.setAttribute(ParamConst.COUNT_LOGIN_USER, ++onlineLoginUser);

        Map<String, Object> loginJsonMap = new HashMap<>(SetConst.LENGTH_TWO);
            loginJsonMap.put(ParamConst.AUTHENTICATION, authentication);
            loginJsonMap.put(ParamConst.STATE, user.getState() == SetConst.ACCOUNT_STATE_TRUE);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, loginJsonMap);
    }

    /**
     * 注销
     *
     * 业务流程
     *      1.删除客户端 Cookie[key = authentication]
     *      2.在线登录人数-1
     *      3.返回成功状态
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
        CookieUtil.removeCookie(request, response, ParamConst.AUTHENTICATION);

        ServletContext application = request.getServletContext();
        int onlineLoginUser = (Integer) application.getAttribute(ParamConst.COUNT_LOGIN_USER);
        application.setAttribute(ParamConst.COUNT_LOGIN_USER, --onlineLoginUser);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 注册
     *
     * 业务流程
     *      A.参数处理
     *          1.获取参数（request-body 内获取参数）
     *          2.参数检测
     *
     *      B.用户邮箱存在性判断
     *          1.检测用户名
     *          2.检测邮箱
     *
     *       C.注册操作
     *          1.构建 UserDO ，设置 username，email，password（密码需用 MD5 加密，数据库保存加密结果）
     *          2.数据库保存用户
     *          3.注册成功（自动往 UserDO 对象注入数据库自增id）
     *          4.返回成功状态 + 根据自增 id 重新查询用户信息（UserDO 需使用 JsonUtil 转换为 Map）
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO register(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        //参数处理
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String password = (String) requestBodyParamsMap.get(ParamConst.PASSWORD);
        String email = (String) requestBodyParamsMap.get(ParamConst.EMAIL);

        Map<String, String> typeParamMap = new HashMap<>(SetConst.SIZE_THREE);
            typeParamMap.put(ParamConst.USERNAME, username);
            typeParamMap.put(ParamConst.PASSWORD, password);
            typeParamMap.put(ParamConst.EMAIL, email);
        RequestParamCheckUtil.check(typeParamMap);

        //用户邮箱存在性判断
        userService.isOccupyByUsername(username);
        userService.isOccupyByEmail(email);

        //注册操作
        UserDO user = new UserDO();
            user.setName(username);
            user.setEmail(email);
            user.setPassword(SecretUtil.encryptUserPassword(password));

        userService.registerUser(user);

        UserDO dbUser = userService.getUserInfoById(user.getId());

        Map<String, Object> userInfoMap = JsonUtil.toMapByObject(dbUser);
        MapFilterUtil.filterUserInfo(userInfoMap);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, userInfoMap);
    }

    /**
     * 修改密码
     *
     * 业务流程：
     *      A.参数处理
     *          1.参数合法性（username 和 password[新密码]）
     *
     *      B.权限安全
     *          1.获取登录信息（客户端 Cookie 内 获取）
     *          2.比较"登录"用户名和"输入"用户名是否一致（防止篡改他人密码）
     *
     *      C.更改密码
     *          1.更新数据库密码（需对新密码进行 MD5 加密，保存加密后结果）
     *          2.返回成功状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/update-password", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO updatePassword(@RequestBody Map<String, Object> requestBodyParamsMap,
                                            HttpServletRequest request) throws Exception {
        //参数处理
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String password = (String) requestBodyParamsMap.get(ParamConst.PASSWORD);

        Map<String, String> typeParamMap = new HashMap<>(SetConst.LENGTH_TWO);
            typeParamMap.put(ParamConst.USERNAME, username);
            typeParamMap.put(ParamConst.PASSWORD, password);
        RequestParamCheckUtil.check(typeParamMap);

        //权限安全
        String authentication = CookieUtil.getCookieValue(request, ParamConst.AUTHENTICATION);
        UserDO cookieUser = JwtTokenUtil.verifyToken(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
        if (cookieUser == null || !username.equals(cookieUser.getName())) {
            throw new AccountErrorException(ApiMessage.NO_PERMISSION).log(LogWarn.ACCOUNT_12);
        }

        //C.更新密码
        userService.alterUserPassword(username, SecretUtil.encryptUserPassword(password));
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 修改邮箱
     *
     * 业务流程：
     *      A.参数处理
     *          1.参数合法性（检查 username 和 email（新邮箱））
     *
     *       B.权限安全
     *          1.获取登录信息（Cookie authentication）
     *          2.判断用户是否激活（已激活的用户，无法修改邮箱）
     *
     *       C.数据库验证
     *          1.数据库验证用户是否激活
     *          2.邮箱存在性（邮箱是否已被注册）
     *
     *       D.更改邮箱
     *          1.数据库更改
     *          2.返回成功状态
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
        //参数处理
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String email = (String) requestBodyParamsMap.get(ParamConst.EMAIL);

        Map<String, String> typeParamMap = new HashMap<>(SetConst.SIZE_TWO);
            typeParamMap.put(ParamConst.USERNAME, username);
            typeParamMap.put(ParamConst.EMAIL, email);
        RequestParamCheckUtil.check(typeParamMap);

        //权限安全
        String authentication = CookieUtil.getCookieValue(request, ParamConst.AUTHENTICATION);
        UserDO cookieUser = JwtTokenUtil.verifyToken(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
        if (cookieUser == null || !username.equals(cookieUser.getName())) {
            throw new AccountErrorException(ApiMessage.NO_PERMISSION).log(LogWarn.ACCOUNT_12);
        }
        if (cookieUser.getState() == SetConst.ACCOUNT_STATE_FALSE) {
            throw new AccountErrorException(ApiMessage.ACCOUNT_ACTIVATED).log(LogWarn.ACCOUNT_07);
        }

        //数据库验证
        if (userService.getUserInfoByName(username).getState() == SetConst.ACCOUNT_STATE_TRUE) {
              //重新存储 Cookie
              CookieUtil.saveCookie(response, ParamConst.AUTHENTICATION, JwtTokenUtil.createToken(cookieUser));
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
     *          1.检测 username
     *
     *      B.邮箱判断用户
     *          1.邮箱是否被占用
     *          2.用户是否激活（已激活的无法发送邮件）
     *
     *      C.发送邮件
     *          1.构建 token（用户邮箱 + 过期时间戳，使用 Base64 加密）
     *          2.构建邮件内容（生成 Html 格式）
     *          3.发送邮件（Spring 线程池，taskExecutor 另调用线程执行发送邮件任务，采用 lambda 写法）
     *          4.返回成功状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/activate", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO sendActivateEmail(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        //参数处理
        String email = (String) requestBodyParamsMap.get(ParamConst.EMAIL);

        RequestParamCheckUtil.check(ParamConst.EMAIL, email);

        //判断邮箱用户
        UserDO user = userService.getUserInfoByEmail(email);
        if (user.getState() == SetConst.ACCOUNT_STATE_FALSE) {
            throw new AccountErrorException(ApiMessage.ACCOUNT_ACTIVATED).log(email + LogWarn.ACCOUNT_07);
        }

        //发送哟件
        long expireTime = System.currentTimeMillis() + SetConst.EXPIRE_TIME_MS_ONE_DAY;
        String token = SecretUtil.encryptBase64(email + "-" + expireTime);

        String emailContent = StringUtil
                                .createEmailActivationHtmlString(SetConst.MAIL_ACCOUNT_ACTIVATION_URL + token);

        taskExecutor.execute(
                () -> SendEmailUtil.sendEmail(email, " Neubbs 账户激活", emailContent)
        );

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, ApiMessage.MAIL_SEND_SUCCESS);
    }

    
    /**
     * 激活账户（ 验证 token ）
     *
     * 业务流程：
     *      A.参数处理
     *          1.检查 token
     *
     *      B.token 解析
     *          1.解密 token（Base64 解密，获取用户邮箱 + 过期时间）
     *          2.判断token是否过期
     *
     *      C.激活用户
     *          1.数据库激活账户（注册完成后，发送邮件激活URL，包含 token 参数，所以此时 email 必定已激活，无需验证邮箱存在性）
     *          2.返回成功状态
     *
     * @param token 传入的 token
     * @return ResponseJsonDTO request-body内JSON数据
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO validateEmailToken(@RequestParam(value = "token", required = false) String token)
                                                throws Exception {
        //参数处理
        RequestParamCheckUtil.check(ParamConst.TOKEN, token);

        //解析 token
        String plainText = SecretUtil.decryptBase64(token);
        String[] array = plainText.split("-");
        String email = array[0];
        String expireTime = array[1];

        if (StringUtil.isExpire(expireTime)) {
            throw new TokenExpireException(ApiMessage.LINK_INVALID).log(token + LogWarn.ACCOUNT_05);
        }

        //激活用户
        userService.activationUser(email);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, ApiMessage.ACTIVATION_SUCCESSFUL);
    }

    /**
     * 图片验证码（页面生成图片）
     *
     * 业务流程：
     *      1.设置 response
     *      2.生成验证码（随机文本 + 数字）
     *      3.验证码储存到 Sessin
     *      4.生成图像（jpg 格式）
     *      5.页面输出
     *
     * @param request http请求
     * @param response http响应
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void getCaptchaPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();

        request.getSession().setAttribute(SetConst.SESSION_CAPTCHA, capText);

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
     * 业务流程：
     *      A.参数处理
     *          1.检查 captcha（用户输入验证码）
     *
     *      B.检查验证码是否正确
     *          1.获取 Session 内验证码
     *          2.用户输入与 Session 储存验证进行对比
     *          3.返回成功状态
     *
     * @param captcha 用户输入验证码
     * @param request http请求
     * @return ResponseJsonDTO 响应JSON字符串
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/check-captcha", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO checkCaptcha(@RequestParam(value = "captcha", required = false)String captcha,
                                            HttpServletRequest request) throws Exception {
        //参数处理
        RequestParamCheckUtil.check(ParamConst.CAPTCHA, captcha);

        //检查验证码是否正确
        String sessionCaptcha = (String) request.getSession().getAttribute(SetConst.SESSION_CAPTCHA);
        if (StringUtil.isEmpty(sessionCaptcha)) {
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
     *          1.检查 email
     *
     *      B.邮箱用户存在性
     *
     *      C.更改密码
     *          1.生成新密码（随机字符串，length = 6）
     *          2.数据库更改密码（MD5 加密新密码，数据库储存加密结果）
     *
     *      D.发送邮件
     *          1.构建邮件内容
     *          2.异步发送邮件（Spring 线程池，另调用线程执行发送邮件任务，lambda 写法）
     *          3.返回成功状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @RequestMapping(value = "/forget-password", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO sendTemporaryPasswordEmail(@RequestBody Map<String, Object> requestBodyParamsMap)
                                                            throws Exception {
        //参数处理
        String email = (String) requestBodyParamsMap.get(ParamConst.EMAIL);

        RequestParamCheckUtil.check(ParamConst.EMAIL, email);

        //邮箱用户存在性
        UserDO user = userService.getUserInfoByEmail(email);

        //更改密码
        String randomPassword = RandomUtil.getRandomString(SetConst.FORGET_PASSWORD_RANDOM_LENGTH);
        userService.alterUserPassword(user.getName(), SecretUtil.encryptUserPassword(randomPassword));

        //D.发送邮件
        String emailContent = user.getEmail() + " 邮箱用户临时密码为：" + randomPassword + " ，请登陆后尽快修改密码！";
        taskExecutor.execute(
                () -> SendEmailUtil.sendEmail(email, "Neubbs 账户临时密码", emailContent)
        );

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }
}
