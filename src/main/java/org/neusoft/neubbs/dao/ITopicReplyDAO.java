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
     * @return Integer 插入行数
     */
    Integer saveTopicReply(TopicReplyDO topicReply);

    /**
     * 删除话题回复
     *
     * @param id 回复id
     * @return Integer 删除行数
     */
    Integer removeTopicReplyById(int id);

    /**
     * 删除话题回复（指定话题id，所有回复）
     *
     * @param topicId 话题id
     * @return Integer 删除行数
     */
    Integer removeTopicReplyByTopicId(int topicId);

    /**
     * 获取话题回复总数
     *
     * @return Integer 回复总数
     */
    Integer getTopicReplyMaxId();

    /**
     * 查询话题回复
     *
     * @param id 回复id
     * @return TopicReplyDO 话题回复对象
     */
    TopicReplyDO getTopicReplyById(int id);

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
     * @param id 话题id
     * @param content 新话题内容
     * @return Integer 更新行数
     */
    Integer updateContentByIdByContent(int id, String content);

    /**
     * 更新点赞数（自动 +1）
     *
     * @param id 回复id
     * @return Integer 更新行数
     */
    Integer updateAgreeAddOneById(int id);

    /**
     * 更新点赞数（自动 -1）
     *
     * @param id 回复id
     * @return Integer 更新行数
     */
    Integer updateAgreeCutOneById(int id);

    /**
     * 更新反对数（自动 +1）
     *
     * @param id 回复id
     * @return Integer 更新行数
     */
    Integer updateOpposeAddOneById(int id);

    /**
     * 更新反对数（自动 -1）
     *
     * @param id 回复id
     * @return Integer 更新行数
     */
    Integer updateOpposeCutOneById(int id);
}
