package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ApiJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.ICaptchaService;
import org.neusoft.neubbs.service.IEmailService;
import org.neusoft.neubbs.service.IFtpService;
import org.neusoft.neubbs.service.IHttpService;
import org.neusoft.neubbs.service.IRandomService;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.service.ISecretService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.service.IValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 *   账户 api
 *      + 获取用户信息
 *      + 获取用户激活状态
 *      + 获取获取用户所有主动关注人信息列表
 *      + 获取所有被关注人信息列表
 *      + 登录
 *      + 注销
 *      + 注册
 *      + 修改用户基本信息
 *      + 修改密码
 *      + 修改邮箱
 *      + 激活账户
 *      + 激活 token 验证
 *      + 图片验证码
 *      + 验证用户输入的验证码
 *      + 忘记密码
 *      + 关注用户
 *
 * @author Suvan
 */
@RestController
@RequestMapping("/api/account")
public final class AccountController {

    private final IValidationService validationService;
    private final IUserService userService;
    private final ISecretService secretService;
    private final IRedisService redisService;
    private final IFtpService ftpService;
    private final IHttpService httpService;
    private final IEmailService emailService;
    private final ICaptchaService captchaService;
    private final IRandomService randomService;


    /**
     * Constructor（自动注入）
     */
    @Autowired
    private AccountController(IValidationService validationService, IUserService userService,
                              ISecretService secretService, IRedisService redisService,
                              IFtpService ftpService, IHttpService httpService,
                              IEmailService emailService, ICaptchaService captchaService,
                              IRandomService randomService) {
        this.validationService = validationService;
        this.userService = userService;
        this.secretService = secretService;
        this.redisService = redisService;
        this.ftpService = ftpService;
        this.httpService = httpService;
        this.emailService = emailService;
        this.captchaService = captchaService;
        this.randomService = randomService;
    }

    /**
     * 获取用户信息
     *      - AccountController 默认访问
     *
     * @param username 用户名
     * @param email 用户邮箱
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ApiJsonDTO getUserInfo(@RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "email", required = false) String email) {
        validationService.paramsNotNull(username, email);
        if (username != null) {
            validationService.check(validationService.getUsernameParamType(username), username);
        } else {
            validationService.check(ParamConst.EMAIL, email);
        }

        return userService.isUserExist(username, email)
                ? new ApiJsonDTO().success().map(userService.getUserInfoToPageModelMap(username, email))
                : new ApiJsonDTO().error();
    }

    /**
     * 获取用户激活状态
     *
     * @param username 用户名
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @RequestMapping(value = "/state", method = RequestMethod.GET)
    public ApiJsonDTO getUserState(@RequestParam(value = "username", required = false) String username) {
        validationService.check(ParamConst.USERNAME, username);
        return new ApiJsonDTO().success().map(userService.isUserActivatedByName(username));
    }

    /**
     * 获取用户所有主动关注人信息列表
     *
     * @param userId 用户id
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @RequestMapping(value = "/following", method = RequestMethod.GET)
    public ApiJsonDTO listUserFollowingUsersInfo(@RequestParam(value = "userid", required = false) String userId) {
        validationService.check(ParamConst.USER_ID, userId);
        return new ApiJsonDTO().success()
                .list(userService.listAllFollowingUserInfoToPageModelList(Integer.valueOf(userId)));
    }

    /**
     * 获取用户所有被关注信息列表
     *
     * @param userId 用户id
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @RequestMapping(value = "/followed", method = RequestMethod.GET)
    public ApiJsonDTO listUserFollowedUsersInfo(@RequestParam(value = "userid", required = false) String userId) {
        validationService.check(ParamConst.USER_ID, userId);
        return new ApiJsonDTO().success()
                .list(userService.listAllFollowedUserInfoToPageModelList(Integer.valueOf(userId)));
    }

    /**
     * 登录
     *
     * @param requestBodyParamsMap  request-body内JSON数据
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiJsonDTO loginAccount(@RequestBody Map<String, Object> requestBodyParamsMap) {
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String password = (String) requestBodyParamsMap.get(ParamConst.PASSWORD);

        validationService.check(ParamConst.USERNAME, username).check(ParamConst.PASSWORD, password);

        //database login authenticate
        UserDO user = userService.loginAuthenticate(username, password);

        //jwt secret user information, save authentication to cookie
        String authentication = secretService.jwtCreateTokenByUser(user);
        httpService.saveAuthenticationCookie(authentication);

        //login user +1
        httpService.incOnlineLoginUserNumber();

        //response model -> include authentication and state
        Map<String, Object> modelJsonMap = new LinkedHashMap<>(SetConst.LENGTH_TWO);
            modelJsonMap.put(ParamConst.AUTHENTICATION, authentication);
            modelJsonMap.put(ParamConst.STATE, userService.isUserActivatedByState(user.getState()));
        return new ApiJsonDTO().success().map(modelJsonMap);
    }

    /**
     * 注销
     *
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @LoginAuthorization
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ApiJsonDTO logoutAccount() {
        httpService.removeCookie(ParamConst.AUTHENTICATION);

        //login user +1
        httpService.decOnlineLoginUserNumber();

        return new ApiJsonDTO().success();
    }

    /**
     * 注册
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO registerAccount(@RequestBody Map<String, Object> requestBodyParamsMap) {
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String password = (String) requestBodyParamsMap.get(ParamConst.PASSWORD);
        String email = (String) requestBodyParamsMap.get(ParamConst.EMAIL);

        validationService.check(ParamConst.USERNAME, username)
                         .check(ParamConst.PASSWORD, password)
                         .check(ParamConst.EMAIL, email);

        //database register user
        UserDO newRegisterUser = userService.registerUser(username, password, email);

        //create user person directory on cloud ftp server
        ftpService.registerUserCreatePersonDirectory(newRegisterUser);

        return new ApiJsonDTO().success().map(userService.getUserInfoMapByUser(newRegisterUser));
    }

    /**
     * 修改用户基本信息
     *      - sex 不能为空，且只能为 0 or 1
     *      - birthday, position, description 允许 "",但是不能为 null
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/update-profile", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO updateUserInfo(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer newSex = (Integer) requestBodyParamsMap.get(ParamConst.SEX);
        String newBirthday = (String) requestBodyParamsMap.get(ParamConst.BIRTHDAY);
        String newPosition = (String) requestBodyParamsMap.get(ParamConst.POSITION);
        String newDescription = (String) requestBodyParamsMap.get(ParamConst.DESCRIPTION);

        validationService.checkInstructionOfSpecifyArray(String.valueOf(newSex), "0", "1");
        validationService.paramsNotNull(newBirthday, newPosition, newDescription);
        validationService.check(ParamConst.BIRTHDAY, newBirthday)
                         .check(ParamConst.POSITION, newPosition)
                         .check(ParamConst.DESCRIPTION, newDescription);

        //get user information in client cookie
        UserDO user = secretService.jwtVerifyTokenByTokenByKey(
               httpService.getAuthenticationCookieValue(), SetConst.JWT_TOKEN_SECRET_KEY
        );

        return new ApiJsonDTO().success()
                .map(userService.alterUserProfile(user.getName(), newSex, newBirthday, newPosition, newDescription));
    }

    /**
     * 修改密码
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ApiJsonDTO 页面JSON传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/update-password", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO updateUserPassword(@RequestBody Map<String, Object> requestBodyParamsMap) {
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String newPassword = (String) requestBodyParamsMap.get(ParamConst.PASSWORD);

        validationService.check(ParamConst.USERNAME, username).check(ParamConst.PASSWORD, newPassword);

        //confirm input username match logged in user
        UserDO cookieUser = secretService.jwtVerifyTokenByTokenByKey(
                httpService.getAuthenticationCookieValue(), SetConst.JWT_TOKEN_SECRET_KEY
        );
        userService.confirmUserMatchCookieUser(username, cookieUser);

        userService.alterUserPasswordByName(username, newPassword);

        return new ApiJsonDTO().success();
    }

    /**
     * 修改邮箱
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @LoginAuthorization
    @RequestMapping(value = "/update-email", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO updateUserEmail(@RequestBody Map<String, Object> requestBodyParamsMap) {
        String username = (String) requestBodyParamsMap.get(ParamConst.USERNAME);
        String newEmail = (String) requestBodyParamsMap.get(ParamConst.EMAIL);

        validationService.check(ParamConst.USERNAME, username).check(ParamConst.EMAIL, newEmail);

        UserDO cookieUser = secretService.jwtVerifyTokenByTokenByKey(
                httpService.getAuthenticationCookieValue(), SetConst.JWT_TOKEN_SECRET_KEY
        );
        userService.confirmUserMatchCookieUser(username, cookieUser);

        userService.alterUserEmail(username, newEmail);

        return new ApiJsonDTO().success();
    }

    /**
     * 激活账户
     *      - 发送激活 mail
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @RequestMapping(value = "/activate", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO activateAccount(@RequestBody Map<String, Object> requestBodyParamsMap) {
        String email = (String) requestBodyParamsMap.get(ParamConst.EMAIL);

        validationService.check(ParamConst.EMAIL, email);

        userService.confirmUserActivatedByEmail(email);

        //60s send email interval
        long remainAllowSendEmailInterval = redisService.getExpireTime(email);
        if (remainAllowSendEmailInterval != SetConst.EXPIRE_TIME_REDIS_NO_EXIST_KEY) {
            Map<String, Object> timerMap = new HashMap<>(SetConst.SIZE_ONE);
                timerMap.put(ParamConst.TIMER, remainAllowSendEmailInterval / SetConst.THOUSAND);
            return new ApiJsonDTO().error().message(ApiMessage.WAIT_TIMER).map(timerMap);
        }

        //start another thread to send mail
        String token = secretService.getEmailActivateToken(email);

        //get email content(if param activateUrl = null, default use neubbs.properties settings)
        String emailContent = emailService.getEmailContentxtToActivateUserMailHtml(null, token);
        emailService.sendEmail(SetConst.EMAIL_SENDER_NAME, email, SetConst.EMAIL_SUBJECT_ACTIVATE, emailContent);

        //set user send email 60s interval
        redisService.save(email, SetConst.VALUE_MAIL_SEMD_INTERVAL, SetConst.EXPIRE_TIME_SIXTY_SECOND_MS);

        return new ApiJsonDTO().success();
    }


    /**
     * 激活 token 验证
     *      - 验证 token
     *      - 数据库激活用户
     *      - 修改客户端 cookie (重新保存用户信息)
     *
     * @param token 传入的 token
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public ApiJsonDTO validateActivateToken(@RequestParam(value = "token", required = false) String token) {
        validationService.check(ParamConst.TOKEN, token);

        UserDO activatedUser = userService.alterUserActivateStateByToken(token);

        String authentication = secretService.jwtCreateTokenByUser(activatedUser);
        httpService.saveAuthenticationCookie(authentication);

        return new ApiJsonDTO().success();
    }

    /**
     * 图片验证码
     *      - 页面生成验证码图片, 刷新重新生成
     *      - 目前 Session 储存验证码
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void generateCaptchaPicture() {
        //set response headers
        httpService.setPageResponseHeaderToImageType();

        //generate captcha text
        String captchaText = captchaService.getCaptchaText();

        //set session attribute
       httpService.setSessionToSaveCaptchaText(captchaText);

        //generate captcha image, input captcha text
        BufferedImage outputImage = captchaService.getCaptchaImage(captchaText);

        //page output jpg format image
        httpService.outputPageImageJPGFormat(outputImage);
    }

    /**
     * 验证用户输入的验证码
     *      - 比较用户输入是否与图片一致
     *
     * @param captcha 用户输入验证码
     * @return ApiJsonDTO 接口JSON字符串
     */
    @RequestMapping(value = "/check-captcha", method = RequestMethod.GET)
    public ApiJsonDTO validateCaptcha(@RequestParam(value = "captcha", required = false)String captcha) {
        validationService.check(ParamConst.CAPTCHA, captcha);

        //get session captcha
        String sessionCaptcha = httpService.getSessionCaptchaText();

        //compare user input captcha match session captcha
        captchaService.judgeInputCaptchaWhetherSessionCaptcha(captcha, sessionCaptcha);

        return new ApiJsonDTO().success();
    }

    /**
     * 忘记密码
     *      - 验证邮箱存在性
     *      - 生成随机临时密码，修改邮箱用户密码 - > 临时密码
     *      - 发送临时密码到指定 email
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ApiJsonDTO 接口JSON传输对象
     */
    @RequestMapping(value = "/forget-password", method = RequestMethod.POST)
    public ApiJsonDTO forgetPassword(@RequestBody Map<String, Object> requestBodyParamsMap) {
        String email = (String) requestBodyParamsMap.get(ParamConst.EMAIL);

        validationService.check(ParamConst.EMAIL, email);

        String randomPassword = randomService.generateSixDigitsRandomPassword();
        userService.alterUserPasswordByEmail(email, randomPassword);

        String emailContent = emailService.getEmailContentToWarnUserGeneratedRandomPassword(email, randomPassword);
        emailService.sendEmail(
                SetConst.EMAIL_SENDER_NAME, email,
                SetConst.EMAIL_SUBJECT_TEMPORARY_PASSWORD, emailContent
        );

        return new ApiJsonDTO().success();
    }

    /**
     * 关注用户
     *      - 主动关注其余用户
     *      - 自动切换：有 -> 无， 无 -> 有
     *
     * @param requestBodyParams request-body内JSON数据
     * @return ApiJsonDTO 页面JSON传输对象 */
    @RequestMapping(value = "/following", method = RequestMethod.POST, consumes = "application/json")
    @LoginAuthorization @AccountActivation
    public ApiJsonDTO followingUser(@RequestBody Map<String, Object> requestBodyParams) {
        Integer followingUserId = (Integer) requestBodyParams.get(ParamConst.USER_ID);

        validationService.check(ParamConst.USER_ID, String.valueOf(followingUserId));

        UserDO cookieUser = secretService.jwtVerifyTokenByTokenByKey(
                httpService.getAuthenticationCookieValue(), SetConst.JWT_TOKEN_SECRET_KEY
        );

        return new ApiJsonDTO().success().
                buildMap(ParamConst.FOLLOWING_USER_ID,
                        userService.operateFollowUser(cookieUser.getId(), followingUserId));
    }
}
