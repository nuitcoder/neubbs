package org.neusoft.neubbs.constant.secret;

/**
 * MD5 密钥信息
 */
public interface SecretInfo {

    //MD5 加密（用户密码）
    String MD5_ENCRYP_FAIL = "MD5 加密失败，服务器故障";

    //Base64 加密（邮箱激活 token）
    Long EXPIRE_TIME_ONE_DAY = 86400000L; //1天过期
}
