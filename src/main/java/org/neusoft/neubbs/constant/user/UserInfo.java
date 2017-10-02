package org.neusoft.neubbs.constant.user;

/**
 * 用户信息
 */
public interface UserInfo {
    String ID = "id";
    String USERNAME = "username";
    String PASSWORD = "password";
    String SEX = "sex";
    String BIRTHDAY = "birthday";
    String PHONE = "phone";
    String EMAIL = "email";
    String ADDRESS = "address";
    String CREATETIME = "createtime";

    String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";

    String GET_USERINFO_USERNAME_NONUL = "获取用户信息，用户名不能为空";
    String GETINFO_SUCCESS = "获取用户信息成功";

}
