package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicContentDO;
import org.springframework.stereotype.Repository;

/**
 * forum_topic_content 数据访问对象（TopicContentMapper.xml 映射接口）
 */
@Repository
public interface ITopicContentDAO {
    Integer saveTopicContent(TopicContentDO topicContent);

    Integer removeTopicContentById(int id);

    Integer getTopicContentMaxId();
    TopicContentDO getTopicContentById(int id);

    Integer updateContentById(int id, String content);
    Integer updateReadById(int id);
}
