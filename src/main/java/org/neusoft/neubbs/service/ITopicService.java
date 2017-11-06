package org.neusoft.neubbs.service;

import java.util.List;
import java.util.Map;

/**
 * 话题业务接口
 *
 * @author Suvan
 */
public interface ITopicService {

    /**
     * 获取话题信息（基本信息 + 内容 + 回复）
     *
     * @param topicId 话题 id
     * @return Map<String, Object> 话题内容信息
     * @throws Exception 所有异常
     */
    Map<String, Object> getTopic(int topicId) throws Exception;

    /**
     * 获取回复信息（单条回复）
     *
     * @param replyId 回复id
     * @return Map<String, Object> 回复信息
     * @throws Exception 所有异常
     */
    Map<String, Object> getReply(int replyId) throws Exception;

    /**
     * 获取话题列表（包含话题数据与基本用户数据）
     *
     * @param page 页数
     * @param limit 每页显示数量
     * @return List<Map<String, Object>> 话题列表
     * @throws Exception 所有异常
     */
    List<Map<String, Object>> listTopics(int page, int limit) throws Exception;

    /**
     * 保存话题
     *
     * @param userId 用户id
     * @param category 话题分类
     * @param title 话题标题
     * @param topicContent 话题内容
     * @return int 新增的话题id
     * @throws Exception 所有异常
     */
    int saveTopic(int userId, String category, String title, String topicContent) throws Exception;

    /**
     * 保存回复
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @param replyContent 话题内容
     * @return int 新增的回复id
     * @throws Exception 所有异常
     */
    int saveReply(int userId, int topicId, String replyContent) throws Exception;

    /**
     * 删除话题
     *
     * @param topicId 要删除的话题id
     * @throws Exception 所有异常
     */
    void removeTopic(int topicId) throws Exception;

    /**
     * 删除回复
     *
     * @param replyId 回复id
     * @throws Exception 所有异常
     */
    void removeReply(int replyId) throws Exception;

    /**
     * 修改话题内容
     *
     * @param topicId 话题id
     * @param newCategory 分类
     * @param newTitle 新标题
     * @param newTopicContent 新话题内容
     * @throws Exception 所有异常
     */
    void alterTopicContent(int topicId, String newCategory, String newTitle, String newTopicContent) throws Exception;

    /**
     * 修改回复内容
     *
     * @param replyId 回复id
     * @param newReplyContent 新回复内容
     * @throws Exception 所有异常
     */
     void alterReplyContent(int replyId, String newReplyContent) throws Exception;
}
