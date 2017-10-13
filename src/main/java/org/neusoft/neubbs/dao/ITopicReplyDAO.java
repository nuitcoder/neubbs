package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicReplyDO;
import org.springframework.stereotype.Repository;

/**
 * forum_topic_reply 数据访问对象（TopicRelayMapper.xml 映射接口）
 */
@Repository
public interface ITopicReplyDAO {
    Integer saveTopicReply(TopicReplyDO topicReply);

    Integer removeTopicReplyById(int id);

    Integer getTopicReplyMaxId();
    TopicReplyDO getTopicReplyById(int id);

    Integer updateContentByIdByContent(int id, String content);
    Integer updateAgreeById(int id);
    Integer updateOpposeById(int id);
}
