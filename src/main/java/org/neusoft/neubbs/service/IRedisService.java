package org.neusoft.neubbs.service;

/**
 * Redis数据库 Service接口
 */
public interface IRedisService {
    void saveByKeyValue(String key, String value);
    void saveByKeyValueTime(String key, String value, long time);

    void removeByKey(String key);

    String getValueByKey(String key);
    Long getExpireTimeByKey(String key);

    void updateByKeyValue(String key, String value);
}
