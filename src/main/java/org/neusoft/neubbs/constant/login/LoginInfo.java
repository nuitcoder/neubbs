package org.neusoft.neubbs.constant.login;

/**
 *  登录信息
 */
public interface LoginInfo {
    String USERNAME_NULL = "用户未输入用户名,username为空";
    String PASSWORD_NULL = "用户未输入密码,password为空";
    String PASSWORD_ERROR = "该用户密码不正确！";

    String USER_NOEXIT = "数据库中不存在该用户";
    String USER_AUTHENTICATE = "用户帐号密码通过验证,登录成功，将在7天内自动登录";
    String USER_GETINFO_SUCCESS = "成功获取用户信息";

    String NO_VISITAUTHORIZATION = "无访问权限，请登录后再操作api";

    String LOGOUT_SUCCESS = "注销成功";
}