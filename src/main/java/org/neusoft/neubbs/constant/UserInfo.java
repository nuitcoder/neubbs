package org.neusoft.neubbs.constant;

/**
 * 用户信息
 */
public interface UserInfo {
    String ID = "id";
    String USERNAME = "username";
    String NAME = "name";
    String PASSWORD = "password";
    String SEX = "sex";
    String BIRTHDAY = "birthday";
    String PHONE = "phone";
    String EMAIL = "email";
    String ADDRESS = "address";
    String CREATETIME = "createtime";

    //格式
    String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";
    String ID_STYLE_ERROR_NO_PPURENUMBER = "id 格式错误，存在非数字字符，请重新输入";

    //数据库反馈
    String DATABASE_NO_EXIST_USER = "数据库中不存在该用户";
    String DATABASE_ALREAD_EXIST_USER = "数据库已经存在用户，请勿重复添加";

    //权限问题
    String RANK_NO_ENOUGH = "权限不足，无法调用 api";

    //登录
    String LOGIN_USERNAME_NULL = "用户未输入用户名,username为空";
    String LOGIN_PASSWORD_NULL = "用户未输入密码,password为空";
    String LOGIN_PASSWORD_ERROR = "该用户密码不正确！";
    String LOGIN_PASS_AUTHENTICATE_LOGIN_SUCCESS = "用户帐号密码通过验证,登录成功，将在7天内自动登录";

    //注销
    String LOGOUT_SUCCESS = "用户注销成功！";

    //获取用户信息
    String GET_USERINFO_SUCCESS = "获取用户信息成功";
    String GET_USERINFO_FAIL = "获取用户信息失败";
    String GET_USERINFO_USERNAME_NONULL = "获取用户信息，username 不能为空";

    //注册用户
    String REGISTER_USER_SUCCESS = "注册用户成功！";
    String REGISTER_USER_FAIL = "注册用户失败";
    String REGISTER_USER_USERNAME_NONULL = "注册用户，username 不能为空";
    String REGISTER_USER_PASSWORD_NONULL = "注册用户，password 不能为空";
    String REGISTER_USER_EMAIL_NONULL = "注册用户， email 不能为空";

    //修改用户密码
    String UPDATE_USER_PASSWORD_SUCCESS = "更新用户密码成功";
    String UPDATE_USER_PASSWORD_FAIL = "更新用户密码失败";
    String UPDATE_USER_PASSWORD_USERNAME_NONULL = "更新用户密码，username 不能为空";
    String UPDATE_USER_PASSWORD_PASSWORD_NONULL = "更新用户密码，password 不能为空";

    //修改用户权限
    String UPDATE_USER_RANK_SUCCESS = "更新用户权限成功";
    String UPDATE_USER_RANK_FAIL = "更新用户权限失败";
    String UPDATE_USER_RANK_USERNAME_NONULL = "更新用户权限，username 不能为空";
    String UPDATE_USER_RANK_RANK_NONULL = "更新用户权限，rank 不能为空";

    //发送邮箱验证码
    String EMAILCODE = "emailcode";
    String EMAILCODE_HOST = "smtp.qq.com";
    String EMAILCODE_FROM_USERNAME = "526097449@qq.com";
    String EMAILCODE_FROM_PASSWORD = "pxjkgvpuvtngbhbe";
    String EMAILCODE_SMTP = "smtp";
    String EMAILCODE_AUTH = "mail.smtp.auth";
    String EMAILCODE_AUTH_TRUE = "true";
    String EMAILCODE_SMTP_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";
    String EMAILCODE_JAVAX_NET_SSL_SSLSOCKETFACTORY = "javax.net.ssl.SSLSocketFactory";
    String EMAILCODE_SMTP_SOCKETFACTORY_PORT = "mail.smtp.socketFactory.port";
    String EMAILCODE_SMTP_SSL_PROT = "465";
    String EMAILCODE_TO_SUBJECT = "neubbs 邮箱验证码";
    String EMAILCODE_TO_TEXT = "您好，您的邮箱验证码为: ";
    String EMAILCODE_SUCCESS = "邮箱验证码,已成功发送到指定用户邮箱";
    String EMAILCODE_FAIL = "获取邮箱验证码失败";
    String EMAILCODE_EMAIL_NUNULL = "获取邮箱验证码，邮箱不能为空";
}
