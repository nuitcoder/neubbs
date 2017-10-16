package org.neusoft.neubbs.constant.api;

/**
 * 用户信息
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

    //api 返回数据
    String NO_USER = "the user does not exist";
    String NO_ACTIVATE   = "account is not active";
    String PASSWORD_ERROR = "user password is incorrect";
    String USERNAME_PASSWORD_ERROR = "wrong user name or password";
    String USERNAME_REGISTERED = "user name has been registered";
    String LINK_INVALID = "invalid activation link";
    String ACTIVATION_FAIL_EMAIL_NO_REGISTER = "activation failed, mailbox is not registered";
    String TOKEN_EXPIRED = "token expired";
    String NO_PERMISSION= "no permission";

    //验证 Token
    String AUTHENTICATION = "authentication";
    String RANK_ADMIN = "admin"; //管理员权限


}
