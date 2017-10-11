package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.TopicDO;

import java.util.Date;
import java.util.List;

/**
 * 主题业务接口
 */
public interface ITopicService {
    Integer saveTopic(TopicDO topic);

    Integer removeTopicById(int id);

    TopicDO getTopicById(int id);
    List<TopicDO> listTopicByStartRowByCount(int startRow, int count);

    Integer updateCategoryById(int id, String category);
    Integer updateTitleById(int id, String title);
    Integer updateCommentById(int id);
    Integer updateLastreplytimeById(int id, Date lastreplytime);
}
