package org.neusoft.neubbs.service.impl;


import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("topicServiceImpl")
public class TopicServiceImpl implements ITopicService{

    @Autowired
    ITopicDAO topicDAO;

    @Override
    public Integer saveTopic(TopicDO topic) {
        return topicDAO.saveTopic(topic);
    }

    @Override
    public Integer removeTopicById(int id) {
        return topicDAO.removeTopicById(id);
    }

    @Override
    public TopicDO getTopicById(int id) {
        return topicDAO.getTopicById(id);
    }

    @Override
    public List<TopicDO> listTopicByStartRowByCount(int startRow, int count) {
        return topicDAO.listTopicByStartRowByCount(startRow, count);
    }

    @Override
    public Integer updateCategoryById(int id, String category) {
        return topicDAO.updateCategoryById(id, category);
    }

    @Override
    public Integer updateTitleById(int id, String title) {
        return topicDAO.updateTitleById(id, title);
    }

    @Override
    public Integer updateCommentById(int id) {
        return topicDAO.updateCommentById(id);
    }

    @Override
    public Integer updateLastreplytimeById(int id, Date lastreplytime) {
        return topicDAO.updateLastreplytimeById(id, lastreplytime);
    }
}
