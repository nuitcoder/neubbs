package org.neusoft.neubbs.constant.api;

/**
 * 用户信息
 *
 * @author Suvan
 */
public interface AccountInfo {
    String ID = "id";
    String USERNAME = "username";
    String NAME = "name";
    String PASSWORD = "password";
    String EMAIL = "email";
    String SEX = "sex";
    String BIRTHDAY = "birthday";
    String ADDRESS = "address";
    String DESCRIPTION = "description";
    String PERSONALPROFILE = "personalprofile";
    String IMAGE = "image";
    String RANK = "rank";
    String STATE = "state";
    String CREATETIME = "createtime";

    /**
     * Account api 警告信息
     */
    String PARAM_ERROR  = "incorrect input parameter";

    String NO_USER = "the user does not exist";
    String USERNAME_REGISTERED = "username has been registered";
    String EMAIL_NO_REIGSTER = "the email is not registered";
    String EMAIL_REGISTERED = "the email has bean registered";
    String NO_ACTIVATE   = "the account has not been activated";
    String ACCOUNT_ACTIVATED = "the account has bean activated";
    String ACTIVATION_SUCCESSFUL =  "activate success";

    String PASSWORD_ERROR = "user password is incorrect";
    String USERNAME_OR_PASSWORD_INCORRECT = "username or password is incorrect";
    String CAPTCHA_INCORRECT = "input captcha incorrect";

    String NO_PERMISSION = "no permission";
    String LINK_INVALID = "invalid activation link";
    String TOKEN_EXPIRED = "token expired";
    String NO_GENERATE_CAPTCHA = "no generate captcha";

    String MAIL_SEND_SUCCESS = "mail send success";

    /**
     * 发送邮件
     */
    String MAIL_ACCOUNT_ACTIVATION_URL = "http://localhost:8080/api/account/validate?token=";

    /**
     * 验证码（储存在 Session）
     */
    String SESSION_CAPTCHA = "captcha";

    /**
     * token 验证
     */
    String AUTHENTICATION = "authentication";
    String RANK_ADMIN = "admin";

    /**
     * 激活状态
     */
    int STATE_SUCCESS = 1;
    int STATE_FAIL = 0;

    /**
     * 过期时间（时间戳，ms）
     */
    long EXPIRE_TIME_ONE_DAY = 86400000L;
    long EXPIRE_TIME_SERVEN_DAY = 604800000L;

    /**
     * 临时密码长度
     */
    int FORGET_PASSWORD_RANDOM_LENGTH = 6;

}
