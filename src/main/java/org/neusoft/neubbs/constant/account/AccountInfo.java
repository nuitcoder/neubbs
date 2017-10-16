package org.neusoft.neubbs.constant.account;

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

    //数据库判断
    String DATABASE_NO_EXIST_USER = "数据库中不存在该用户!";
    String DATABASE_ALREAD_EXIST_USER_NO_AGAIN_ADD = "数据库已经存在用户，请勿再次添加";

    //错误
    String USER_PASSWORD_NO_MATCH = "用户密码不匹配";

    //成功
    String GET_USER_INFORMATION_SUCCESS = "获取用户信息成功！";
    String USER_PASS_AUTHENTICATE_LOGIN_SUCCESS = "用户通过验证，登录成功，将在7天内自动登录!";
    String USER_LOGOU_SUCCESS = "用户注销成功!";
    String REGISTER_USER_SUCCESS = "注册用户成功！";
    String UPDATE_USER_PASSWORD_SUCCESS = "更新用户密码成功!";
    String ACCOUNT_ACTIVATE_SUCCESS = " 邮箱，指定账户已经成功激活！";

    String ENGLISH_GET_USER_INFORMATION_SUCCESS = "Get User Information Success!";
    String ENGLISH_LOGIN_SUCCESS = "User Login Success!";
    String ENGLISH_USER_LOGOU_SUCCESS = "User Logout Success!";
    String ENGLISH_REGISTER_USER_SUCCESS = "Register User Success!";
    String ENGLISH_UPDATE_USER_PASSWORD_SUCCESS = "Update User Password Success!";
    String ENGLISH_ACOUNT_ACTIVATE_SUCCESS = " Email , Activate Success !";

    //权限问题
    String NO_VISIT_AHTORITY_PLEASE_LOGIN = "无访问权限，请登录后执行操作！";
    String USER_RANK_NO_ENOUGH = "用户权限不足，非管理员用户无法调用此 api";

    //帐号激活
    String EMAIL_TOKEN_DECRYPT_FAIL = "邮件 token 解码失败，服务器异常！";
    String ACCOUNT_ACTIVATION_URL_ALREAD_EXPIRE_TIME = "该激活链接已经过期，请重新注册";
    String ACCOUNT_ACTIVATION_FAIL = "账户激活失败!";

    String ACCOUNT_NO_ACTIVATION_NO_LOGIN = "账户未激活，无法登录，请到指定邮箱，点击激活链接，激活账户";

}
