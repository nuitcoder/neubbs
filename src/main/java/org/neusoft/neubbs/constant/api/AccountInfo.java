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
     * token 验证
     */
    String AUTHENTICATION = "authentication";
    String RANK_ADMIN = "admin";

    /**
     *  api 警告信息
     */
    String NO_USER = "the user does not exist";
    String NO_ACTIVATE   = "account is not active";
    String PASSWORD_ERROR = "user password is incorrect";
    String USERNAME_PASSWORD_ERROR = "wrong user name or password";
    String USERNAME_REGISTERED = "user name has been registered";
    String LINK_INVALID = "invalid activation link";
    String ACTIVATION_FAIL_EMAIL_NO_REGISTER = "activation failed, mailbox is not registered";
    String TOKEN_EXPIRED = "token expired";
    String NO_PERMISSION= "no permission";
    String ACTIVATION_SUCCESSFUL =  "activation successful！";



}
