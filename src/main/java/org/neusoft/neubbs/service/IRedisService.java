package org.neusoft.neubbs.service;

/**
 * Redis 业务接口
 *
 * @author Suvan
 */
public interface IRedisService {

    /**
     * 保存 key-value（无限期）
     *
     * @param key 键
     * @param value 值
     */
    void save(String key, String value);

    /**
     * 保存 key-value，指定时间后过期
     *
     * @param key 健
     * @param value 值
     * @param time  过期时间（时间戳，time 毫秒后过期）,TimeUnit.MILLISECONDS 类型
     */
    void save(String key, String value, long time);

    /**
     * 删除 key-value
     *
     * @param key 健
     */
    void remove(String key);

    /**
     * 获取 value
     *
     * @param key 健
     * @return String  得到 key 对应的 value值
     */
    String getValue(String key);

    /**
     * 获取 key 过期时间
     *
     * @param key 健
     * @return long 剩余过期时间（时间戳，ms）（-1 无限期， -2 已经过期）
     */
    long getExpireTime(String key);

    /**
     * 更新 key-value，覆盖旧值
     *
     * @param key 健
     * @param value 值
     */
    void update(String key, String value);
}
