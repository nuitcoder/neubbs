package org.neusoft.neubbs.service;

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
     */
    TopicDO getTopicDOByTopicId(int topicId);

    /**
     * 获取 TopicContentDO（查询 forum_topic_content 表）
     *
     * @param topicId 话题id
     * @return TopicContentDO 话题内容对象
     */
    TopicContentDO getTopicContentDOByTopicId(int topicId);

    /**
     * 获取 TopicReplyDO（查询 forum_topic_reply 表）
     *
     * @param replyId 回复id
     * @return TopicReplyDO 话题回复对象
     */
    TopicReplyDO getTopicReplyDOByReplyId(int replyId);

    /**
     * 获取话题信息（基本信息 + 内容 + 回复）
     *
     * @param topicId 话题 id
     * @return Map 话题内容信息
     */
    Map<String, Object> getTopic(int topicId);

    /**
     * 获取回复信息（单条回复）
     *
     * @param replyId 回复id
     * @return Map 回复信息
     */
    Map<String, Object> getReply(int replyId);


    /**
     * 判断话题类别
     *
     * @param category 话题类别
     */
    void isTopicCategoryExist(String category);

    /**
     * 获取话题总页数
     *
     * @param limit 每页限制多少条
     * @param category 分类
     * @param username 用户名
     * @return String 总页数
     */
    String getTopicTotalPages(int limit, String category, String username);

    /**
     * 获取话题列表（包含话题数据与基本用户数据）
     *
     * @param limit 每页显示数量
     * @param page 跳转到指定页数
     * @param category 分类目录
     * @param username 用户名
     * @return List 列表
     */
    List<Map<String, Object>> listTopics(int limit, int page, String category, String username);

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
     */
    int saveTopic(int userId, String category, String title, String topicContent);

    /**
     * 保存回复
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @param replyContent 话题内容
     * @return int 新增的回复id
     */
    int saveReply(int userId, int topicId, String replyContent);

    /**
     * 删除话题
     *
     * @param topicId 要删除的话题id
     */
    void removeTopic(int topicId);

    /**
     * 删除回复
     *
     * @param replyId 回复id
     */
    void removeReply(int replyId);

    /**
     * 修改话题内容
     *
     * @param topicId 话题id
     * @param newCategory 分类
     * @param newTitle 新标题
     * @param newTopicContent 新话题内容
     */
    void alterTopicContent(int topicId, String newCategory, String newTitle, String newTopicContent);

    /**
     * 修改回复内容
     *
     * @param replyId 回复id
     * @param newReplyContent 新回复内容
     */
     void alterReplyContent(int replyId, String newReplyContent);
}
