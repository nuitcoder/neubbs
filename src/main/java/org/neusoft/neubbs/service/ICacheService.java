package org.neusoft.neubbs.service;

/**
 * 缓存业务业务接口
 *      - 保存用户邮箱 Key
 *      - 获取邮箱 Key（剩余）过期时间
 *
 * @author Suvan
 */
public interface ICacheService {

    /**
     * 保存用户邮箱 Key
     *      - 指定 60s 过期
     *
     * @param emailKey 用户邮箱（作为 key）
     */
    void saveUserEmailKey(String emailKey);

    /**
     * 获取邮箱 Key （剩余）过期时间
     *
     * @param emailKey 用户邮箱（key）
     * @return long 剩余过期时间（时间戳，ms）（-1 无限期， -2 已经过期）
     */
    long getEmailKeyExpireTime(String emailKey);
}
