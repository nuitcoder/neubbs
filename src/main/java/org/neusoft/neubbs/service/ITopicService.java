package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;

import java.util.List;

/**
 * 话题业务接口
 *
 * @author Suvan
 */
public interface ITopicService {

    /**
     * 保存话题
     *
     * @param userId 用户id
     * @param category 话题分类
     * @param title 话题标题
     * @param content 话题内容
     * @throws Exception 所有异常
     */
    void saveTopic(int userId, String category, String title, String content) throws Exception;

    /**
     * 保存回复
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @param content 话题内容
     * @throws Exception 所有异常
     */
    void saveReply(int userId, int topicId, String content) throws Exception;

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
     * @param content 新话题内容
     * @throws Exception 所有异常
     */
    void alterTopicContent(int topicId, String content) throws Exception;

    /**
     * 修改话题回复内容
     *
     * @param replyId 回复id
     * @param content 新回复内容
     * @throws Exception 所有异常
     */
     void alterTopicReplyContent(int replyId, String content) throws Exception;

    /**
     * 修改主题统计
     *
     * @param topicId 话题id
     * @param field 字段（read 或 agree）
     * @param type 增减类型（add 或 cut）
     * @throws Exception 所有异常
     */
     void alterTopicStatistics(int topicId, String field, String type) throws Exception;

    /**
     * 修改主题回复统计
     *
     * @param replyId 回复id
     * @param field 字段（agree 或 agree）
     * @param type 增减类型（add 或者 cut）
     * @throws Exception 所有异常
     */
     void alterTopicReplyStatistics(int replyId, String field, String type) throws Exception;

    /**
     * 话获取话题（基本信息）
     *
     * @param topicId 话题id
     * @return TopicDO 话题对象
     */
    TopicDO getTopic(int topicId);

    /**
     * 获取话题内容
     *
     * @param topicId 话题id
     * @return TopicContentDO 话题内容对象
     */
    TopicContentDO getTopicContent(int topicId);

    /**
     * 获取话题回复
     *
     * @param replyId
     * @return TopicReplyDO 话题回复对象
     */
    TopicReplyDO getReply(int replyId);

    /**
     * 获取话题信息（最新发布）
     *
     * @param count 显示话题数
     * @return List<TopicDO> 话题列表
     */
    List<TopicDO> listTopicDesc(int count);

    /**
     * 获取话题所有回复
     *
     * @param topicId 话题 id
     * @return List<TopicReplyDO> 话题回复列表
     */
    List<TopicReplyDO> listTopicAllReply(int topicId);

}
