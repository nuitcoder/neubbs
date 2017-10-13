package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicDO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * forum_topic 数据访问对象（TopicMapper.xml 映射接口）
 */
@Repository
public interface ITopicDAO {
    Integer saveTopic(TopicDO topic);

    Integer removeTopicById(int id);

    Integer countTopic();
    Integer getTopicMaxId();
    TopicDO getTopicById(int id);
    List<TopicDO> listTopicByStartRowByCount(int startRow, int count);

    Integer updateCategoryById(int id, String category);
    Integer updateTitleById(int id, String title);
    Integer updateCommentById(int id);
    Integer updateLastreplytimeById(int id, Date lastreplytime);

}
