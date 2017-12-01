package org.neusoft.neubbs.service;

import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;

import java.util.List;
import java.util.Map;

/**
 * 话题业务接口
 *
 * @author Suvan
 */
public interface ITopicService {

    /**
     * 获取 TopicDO（查询 forum_topic 表）
     * @param topicId 话题id
     * @return TopicDO 话题对象
     * @throws TopicErrorException 话题错误异常
     */
    TopicDO getTopicDOByTopicId(int topicId) throws TopicErrorException;

    /**
     * 获取 TopicContentDO（查询 forum_topic_content 表）
     *
     * @param topicId 话题id
     * @return TopicContentDO 话题内容对象
     * @throws TopicErrorException 话题错误异常
     */
    TopicContentDO getTopicContentDOByTopicId(int topicId) throws TopicErrorException;

    /**
     * 获取 TopicReplyDO（查询 forum_topic_reply 表）
     *
     * @param replyId 回复id
     * @return TopicReplyDO 话题回复对象
     * @throws TopicErrorException 话题错误异常
     */
    TopicReplyDO getTopicReplyDOByReplyId(int replyId) throws TopicErrorException;

    /**
     * 获取话题信息（基本信息 + 内容 + 回复）
     *
     * @param topicId 话题 id
     * @return Map 话题内容信息
     * @throws TopicErrorException 话题错误异常
     * @throws AccountErrorException 账户错误异常
     */
    Map<String, Object> getTopic(int topicId) throws TopicErrorException, AccountErrorException;

    /**
     * 获取回复信息（单条回复）
     *
     * @param replyId 回复id
     * @return Map 回复信息
     * @throws AccountErrorException 账户错误异常
     */
    Map<String, Object> getReply(int replyId) throws AccountErrorException;


    /**
     * 获取话题总页数
     *
     * @param limit 每页限制多少条
     * @return String 总页数
     */
    String getTopicTotalPages(int limit);

    /**
     * 获取话题列表（包含话题数据与基本用户数据）
     *
     * @param limit 每页显示数量
     * @param page 跳转到指定页数
     * @param category 分类目录
     * @param username 用户名
     * @return List 列表
     * @throws TopicErrorException 话题错误异常
     * @throws AccountErrorException 账户错误异常
     */
    List<Map<String, Object>> listTopics(int limit, int page, String category, String username)
            throws TopicErrorException, AccountErrorException;

    /**
     * 获取所有话题分类（去重）
     *
     * @return List<String> 话题分类列表
     */
    List<String> listTopicCategory();

    /**
     * 保存话题
     *
     * @param userId 用户id
     * @param category 话题分类
     * @param title 话题标题
     * @param topicContent 话题内容
     * @return int 新增的话题id
     * @throws AccountErrorException 账户错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
    int saveTopic(int userId, String category, String title, String topicContent)
            throws AccountErrorException, DatabaseOperationFailException;

    /**
     * 保存回复
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @param replyContent 话题内容
     * @return int 新增的回复id
     * @throws AccountErrorException 账户错误异常
     * @throws TopicErrorException 主题错误异常
     * @throws DatabaseOperationFailException 数据操作失败异常
     */
    int saveReply(int userId, int topicId, String replyContent)
            throws AccountErrorException, TopicErrorException, DatabaseOperationFailException;

    /**
     * 删除话题
     *
     * @param topicId 要删除的话题id
     * @throws TopicErrorException 话题错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
    void removeTopic(int topicId) throws TopicErrorException, DatabaseOperationFailException;

    /**
     * 删除回复
     *
     * @param replyId 回复id
     * @throws TopicErrorException 话题错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
    void removeReply(int replyId) throws TopicErrorException, DatabaseOperationFailException;

    /**
     * 修改话题内容
     *
     * @param topicId 话题id
     * @param newCategory 分类
     * @param newTitle 新标题
     * @param newTopicContent 新话题内容
     * @throws TopicErrorException 话题错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
    void alterTopicContent(int topicId, String newCategory, String newTitle, String newTopicContent)
            throws TopicErrorException, DatabaseOperationFailException;

    /**
     * 修改回复内容
     *
     * @param replyId 回复id
     * @param newReplyContent 新回复内容
     * @throws TopicErrorException 话题错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
     void alterReplyContent(int replyId, String newReplyContent)
             throws TopicErrorException, DatabaseOperationFailException;
}
