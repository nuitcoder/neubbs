package org.neusoft.neubbs.constant.secret;

/**
 * MD5 密钥信息
 *
 * @author Suvan
 */
public interface SecretInfo {

    /**
     * MD5 加密
     *      + 用户密码
     */
    String MD5_ENCRYP_FAIL = "MD5 加密失败，服务器故障";

    /**
     * Base64 加密
     *      + 邮箱激活 token
     */

    /**
     * JWT token 加密
     *      + 登录验证
     */
    String TOKEN_SECRET_KEY = "this neubbs is best";

    /**
     * 加密过期时间
     *      + 7天（1000 * 60 * 60 * * 24 * 7 ms）
     *      + 1天
     */
    Long EXPIRETIME_SERVEN_DAY = 604800000L;
    Long EXPIRETIME_ONE_DAY = 86400000L;
}
