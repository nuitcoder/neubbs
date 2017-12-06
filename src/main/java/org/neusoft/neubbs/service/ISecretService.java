package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;

/**
 * 加密业务接口
 *
 * @author Suvan
 */
public interface ISecretService {

    /**
     * 获取邮箱激活 token
     *
     * @param email 需要激活的邮箱
     * @return 密文 token（Base64加密）
     */
    String getEmailActivateToken(String email);

    /**
     * jwt 创建 token
     *
     * @param user 用户对象
     * @return String 密文
     */
    String jwtCreateTokenByUser(UserDO user);

    /**
     * jwt 解密 token
     *
     * @param token token密文
     * @param key 解密密钥
     * @return UserDO 解密获取用户对象信息
     */
    UserDO jwtVerifyTokenByTokenByKey(String token, String key);
}
