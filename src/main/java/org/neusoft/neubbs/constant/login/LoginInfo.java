package org.neusoft.neubbs.constant.login;

/**
 *  登录信息
 */
public interface LoginInfo {
    String USERNAME_NULL = "用户未输入用户名,username为空";
    String PASSWORD_NULL = "用户未输入密码,password为空";
    String PASSWORD_ERROR = "该用户密码不正确！";

    String NO_VISIT_AUTHORIZATION = "无访问权限，请登录后执行操作！";

    String TOKEN_ALREAD_EXPIRE = "客户端 token，已经过期，请重新的登录";

    String PASS_AUTHENTICATE_LOGIN_SUCCESS = "用户帐号密码通过验证,登录成功，将在7天内自动登录";
    String LOGOUT_SUCCESS = "用户注销成功！";
}