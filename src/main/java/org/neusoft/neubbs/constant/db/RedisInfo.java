package org.neusoft.neubbs.constant.db;

/**
 * Redis信息
 */
public interface RedisInfo {
    long EXPIRETIME_SERVER_DAY = 680400000;//1000 * 60 * 60 * 24 * 6 ms

    String USER_LOGINSTATE_ALREADYEXPIRE = "服务器用户登录状态已经过期，请重新登录";
}
