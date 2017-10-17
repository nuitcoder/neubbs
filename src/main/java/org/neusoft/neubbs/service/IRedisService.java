package org.neusoft.neubbs.service;

/**
 * Redis数据库 业务接口
 *
 * @author Suvan
 */
public interface IRedisService {
    /**
     * 保存 key-value
     *
     * @param key 键
     * @param value 值
     */
    void save(String key, String value);

    /**
     * 保存 key-value
     *
     * @param key
     * @param value
     * @param time  过期时间（时间戳，time 毫秒后过期）
     */
    void save(String key, String value, long time);

    /**
     * 删除 key-value
     *
     * @param key
     */
    void remove(String key);

    /**
     *  获取 value
     *
     * @param key
     * @return String  得到 key 对应的 value值
     */
    String getValue(String key);

    /**
     * 获取 key-value 过期时间
     *
     * @param key
     * @return Long 剩余过期时间（时间戳，ms）
     */
    Long getExpireTime(String key);

    /**
     * 更新 key-value，覆盖旧值
     *
     * @param key
     * @param value
     */
    void update(String key, String value);
}
