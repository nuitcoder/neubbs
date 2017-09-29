package org.neusoft.neubbs.constant.login;

/**
 *  登录信息
 */
public interface LoginInfo {
    String USERNAME_NULL = "用户未输入用户名,username为空";
    String PASSWORD_NULL = "用户未输入密码,password为空";
    String PASSWORD_ERROR = "该用户密码不正确！";

    String USER_NOEXIT = "数据库中不存在该用户";
    String USER_AUTHENTICATE = "用户帐号密码通过验证,登录成功";

    String LOGINSTATE = "loginState";
    String LOGINSTATE_YES = "true";
    String LOGINSTATE_NO = "false";
    String LOGINSTATE_SUCCESS = "登录成功";
    String LOGINSTATE_FAIL = "登录失败";

    String AUTOMATICLOGIN = "automaticLogin"; //自动登录
    String AUTOMATICLOGIN_ON = "ON";

    String AUTHORIZATION = "Authorization";

    String TOKEN = "toekn";
}
