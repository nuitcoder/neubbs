package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicReplyDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * forum_topic_reply 数据访问对象（TopicRelayMapper.xml 映射接口）
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
     * 删除话题回复（指定话题id，所有回复）
     *
     * @param topicId 话题id
     * @return int 删除行数
     */
    int removeTopicReplyByTopicId(int topicId);

    /**
     * 获取话题回复总数
     *
     * @return int 回复总数
     */
    int getTopicReplyMaxId();

    /**
     * 查询话题回复
     *
     * @param replyId 回复id
     * @return TopicReplyDO 话题回复对象
     */
    TopicReplyDO getTopicReplyById(int replyId);

    /**
     * 获取话题回复列表（指定话题id）
     *
     * @param topicId 话题id
     * @return List<TopicReplyDO>
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
     * @param id 回复id
     * @return int 更新行数
     */
    int updateAgreeAddOneById(int id);

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
