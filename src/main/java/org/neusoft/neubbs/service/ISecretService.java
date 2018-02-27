package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;

/**
 * 加密业务接口
 *      - 生成验证邮箱 Token
 *      - 生成用户信息 authentication
 *      - 通过 authentication，获取用户信息
 *
 * @author Suvan
 */
public interface ISecretService {

    /**
     * 生成验证邮箱 Token
     *      - 用于账号邮箱激活
     *      - 使用 Base64 加密，用户邮箱
     *
     * @param email 待激活邮箱
     * @return String 密文token
     */
    String generateValidateEmailToken(String email);

    /**
     * 生成用户信息 authentication
     *      - JWT 加密用户信息（UserDO（id, name, rank, state））
     *      - 存入 Cookie，用于登陆验证，管理员权限认证，激活认证
     *
     * @param user 用户对象（不为 null，已包含用户信息）
     * @return String 密文 authentication
     */
    String generateUserInfoAuthentication(UserDO user);

    /**
     * 通过 authentication, 获取用户信息
     *
     * @param authentication 密文 authentication
     * @return UserDO 用户信息实例对象
     */
    UserDO getUserInfoByAuthentication(String authentication);
}
