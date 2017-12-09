package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicReplyDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 话题回复数据访问接口
 *      - 针对 forum_topic_reply
 *      - resources/mapping/TopicReplyMapper.xml 配置 SQL
 *
 * @author Suvan
 */
@Repository
public interface ITopicReplyDAO {

    /**
     * 保存话题回复
     *
     * @param topicReply 话题回复对象
     * @return int 插入行数
     */
    int saveTopicReply(TopicReplyDO topicReply);

    /**
     * 删除话题回复
     *
     * @param replyId 回复id
     * @return int 删除行数
     */
    int removeTopicReplyById(int replyId);

    /**
     * 测试删除指定 topicId 所有回复
     *
     * @param topicId 话题id
     * @return int 删除行数
     */
    int removeTopicAllReplyByTopicId(int topicId);

    /**
     * 获取最大的话题回复 id
     *      - 最新插入的回复 id
     *
     * @return int 话题回复id
     */
    int getMaxTopicReplyId();

    /**
     * 获取话题回复
     *
     * @param replyId 回复id
     * @return TopicReplyDO 话题回复对象
     */
    TopicReplyDO getTopicReplyById(int replyId);

    /**
     * 获取话题回复列表（指定话题 id）
     *
     * @param topicId 话题id
     * @return List 话题回复列表
     */
    List<TopicReplyDO> listTopicReplyByTopicId(int topicId);

    /**
     * 更新回复内容
     *
     * @param replyId 回复id
     * @param content 新回复内容
     * @return int 更新行数
     */
    int updateContentByIdByContent(int replyId, String content);

    /**
     * 更新点赞数（自动 +1）
     *
     * @param replyId 回复id
     * @return int 更新行数
     */
    int updateAgreeAddOneById(int replyId);

    /**
     * 更新点赞数（自动 -1）
     *
     * @param replyId 回复id
     * @return int 更新行数
     */
    int updateAgreeCutOneById(int replyId);

    /**
     * 更新反对数（自动 +1）
     *
     * @param replyId 回复id
     * @return int 更新行数
     */
    int updateOpposeAddOneById(int replyId);

    /**
     * 更新反对数（自动 -1）
     *
     * @param replyId 回复id
     * @return int 更新行数
     */
    int updateOpposeCutOneById(int replyId);
}
