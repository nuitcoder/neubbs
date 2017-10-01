package org.neusoft.neubbs.constant.login;

/**
 * Redis信息
 */
public interface RedisInfo {
    long EXPIRE_TIME_SERVER_DAY = 1000 * 60 * 60 * 24 * 7;

    String REDIS_USER_LOGINSTATE_ALREADYEXPIRE = "用户登录状态已经过期，请重新登录";
}
