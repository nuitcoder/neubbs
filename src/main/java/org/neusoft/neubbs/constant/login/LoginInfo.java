package org.neusoft.neubbs.constant.login;

/**
 *  登录信息
 */
public interface LoginInfo {
    String USERNAME_NULL = "用户未输入用户名,用户名为null";
    String PASSWORD_NULL = "用户未输入密码,密码为null";

    String USER_NOEXIT = "数据库中不存在该用户";
    String USER_AUTHENTICATE = "用户帐号密码通过验证,登录成功";
    String PASSWORD_ERROR = "该用户密码不正确！";

    String LOGINSTATE_SUCCESS = "登录成功";
    String LOGINSTATE_FAIL = "登录失败";

    String AUTOMATICLOGIN = "automaticLogin";
    String AUTOMATICLOGIN_ON = "ON";
}
