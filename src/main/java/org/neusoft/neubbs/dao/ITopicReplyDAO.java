package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicReplyDO;
import org.springframework.stereotype.Repository;

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
     * @return 删除行数
     */
    Integer removeTopicReplyById(int id);

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
     * 更新回复内容
     *
     * @param id 话题id
     * @param content 新话题内容
     * @return Integer 更新行数
     */
    Integer updateContentByIdByContent(int id, String content);

    /**
     * 更新点赞数
     *
     * @param id 回复id
     * @return Integer 更新行数
     */
    Integer updateAgreeById(int id);

    /**
     * 更新反对数
     *
     * @param id 回复id
     * @return Integer 更新行数
     */
    Integer updateOpposeById(int id);
}
