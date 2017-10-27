package org.neusoft.neubbs.constant.api;

/**
 * 用户信息
 *
 //* @author Suvan
 */
public final class AccountInfo {
    private AccountInfo() { }

    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String SEX = "sex";
    public static final String BIRTHDAY = "birthday";
    public static final  String ADDRESS = "address";
    public static final  String DESCRIPTION = "description";
    public static final String PERSONALPROFILE = "personalprofile";
    public static final String IMAGE = "image";
    public static final String RANK = "rank";
    public static final  String STATE = "state";
    public static final String CREATETIME = "createtime";

    /**
     * Account api 警告信息
     */
    public static final String PARAM_ERROR  = "incorrect input parameter";

    public static final String NO_USER = "the user does not exist";
    public static final String USERNAME_REGISTERED = "username has been registered";
    public static final String EMAIL_NO_REIGSTER = "the email is not registered";
    public static final String EMAIL_REGISTERED = "the email has bean registered";
    public static final String NO_ACTIVATE   = "the account has not been activated";
    public static final String ACCOUNT_ACTIVATED = "the account has bean activated";
    public static final String ACTIVATION_SUCCESSFUL =  "activate success";

    public static final String PASSWORD_ERROR = "user password is incorrect";
    public static final String USERNAME_OR_PASSWORD_INCORRECT = "username or password is incorrect";
    public static final String CAPTCHA_INCORRECT = "input captcha incorrect";

    public static final String NO_PERMISSION = "no permission";
    public static final String LINK_INVALID = "invalid activation link";
    public static final String TOKEN_EXPIRED = "token expired";
    public static final String NO_GENERATE_CAPTCHA = "no generate captcha";

    public static final String MAIL_SEND_SUCCESS = "mail send success";

    /**
     * 发送邮件
     */
    public static final String MAIL_ACCOUNT_ACTIVATION_URL = "http://localhost:8080/api/account/validate?token=";

    /**
     * 验证码（储存在 Session）
     */
    public static final String SESSION_CAPTCHA = "captcha";

    /**
     * token 验证
     */
    public static final String AUTHENTICATION = "authentication";
    public static final String RANK_ADMIN = "admin";

    /**
     * 激活状态
     */
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAIL = 0;

    /**
     * 过期时间（时间戳，ms）
     */
    public static final long EXPIRE_TIME_ONE_DAY = 86400000L;
    public static final long EXPIRE_TIME_SERVEN_DAY = 604800000L;

    /**
     * 临时密码长度
     */
    public static final int FORGET_PASSWORD_RANDOM_LENGTH = 6;

    /**
     * 长度设置
     */
    public static final int LENGTH_TWO = 2;

}
