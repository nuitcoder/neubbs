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
     * Account api 警告信息
     */
    String PARAM_ERROR  = "incorrect input parameter";
    String NO_USER = "the user does not exist";
    String NO_ACTIVATE   = "account has not been activated";
    String PASSWORD_ERROR = "user password is incorrect";
    String USERNAME_PASSWORD_ERROR = "username or password is incorrect";
    String USERNAME_REGISTERED = "username has been registered";
    String EMAIL_REGISTERED = "email has bean registered";
    String ACCOUNT_ACTIVATED_NO_UPDATE_EMAIL = "account has bean activated no update email";
    String LINK_INVALID = "invalid activation link";
    String ACTIVATION_FAIL_EMAIL_NO_REGISTER = "activation failed, email is not registered";
    String TOKEN_EXPIRED = "token expired";
    String NO_PERMISSION= "no permission";
    String ACTIVATION_SUCCESSFUL =  "activation successful！";
    String NO_GENERATE_CAPTCHA = "did not generate captcha";
    String CAPTCHA_ERROR = "captcha error";
    String EMAIL_NO_REIGSTER_NO_SEND = "the email is not registered";
    String EMAIL_ACTIVATED = "the email has been activated";
    String MAIL_SENT_SUCCESS = "mail sent successfully";



}
