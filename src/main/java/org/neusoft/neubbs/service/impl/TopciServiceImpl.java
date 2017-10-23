package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * ITopicService 接口实现类
 *
 * @author Suvan
 */
@Service("topicServiceImpl")
public class TopciServiceImpl implements ITopicService{

    @Autowired
    private ITopicDAO topicDAO;

    @Autowired
    private ITopicContentDAO topicContentDAO;

    @Autowired
    private ITopicReplyDAO topicReplyDAO;

    @Override
    public Boolean saveTopic(int userId, String category, String title, String content) {
        TopicDO topic = new TopicDO();
            topic.setUserid(userId);
            topic.setCategory(category);
            topic.setTitle(title);

        int topicEffectRow = topicDAO.saveTopic(topic);
        if (topicEffectRow == 0) {
            return false;
        }

        TopicContentDO topicContent = new TopicContentDO();
            topicContent.setTopicid(topic.getId());
            topicContent.setContent(content);

        int topicContentEffectRow = topicContentDAO.saveTopicContent(topicContent);
        if (topicContentEffectRow == 0) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean saveReply(int userId, int topicId, String content) {
        TopicReplyDO topicReply = new TopicReplyDO();
            topicReply.setUserid(userId);
            topicReply.setTopicid(topicId);
            topicReply.setContent(content);

        int topicReplyEffectRow = topicReplyDAO.saveTopicReply(topicReply);
        if (topicReplyEffectRow == 0) {
            return false;
        }

        int updateTopicCommentEffectRow = topicDAO.updateCommentAddOneById(topicId);
        int updateTopicLastreplyuserid = topicDAO.updateLastreplyuseridById(topicId, userId);
        int updateTopicLastreplytime  = topicDAO.updateLastreplytimeById(topicId, new Date());
        if (updateTopicCommentEffectRow == 0 || updateTopicLastreplyuserid == 0 || updateTopicLastreplytime == 0) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean removeTopic(int topicId){
        int removeTopicEffectRow = topicDAO.removeTopicById(topicId);
        int removeTopicContentEffectRow = topicContentDAO.removeTopicContentById(topicId);
        int removeTopicReplyEffectRow = topicReplyDAO.removeTopicReplyByTopicId(topicId);
        if (removeTopicEffectRow == 0 || removeTopicContentEffectRow == 0 || removeTopicReplyEffectRow == 0) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean removeReply(int replyId) {
       TopicReplyDO topicReply = topicReplyDAO.getTopicReplyById(replyId);

       int removeTopicEffectRow = topicDAO.updateCommentCutOneById(topicReply.getTopicid());
       if (removeTopicEffectRow == 0) {
           return false;
       }

       int removeTopicReplyEffectRow = topicReplyDAO.removeTopicReplyById(replyId);
       if (removeTopicReplyEffectRow == 0) {
            return false;
       }

       return true;
    }

    @Override
    public TopicDO getTopic(int topicId) {
        return topicDAO.getTopicById(topicId);
    }

    @Override
    public TopicContentDO getTopicContent(int topicId) {
        return topicContentDAO.getTopicContentById(topicId);
    }

    @Override
    public TopicReplyDO getReply(int replyId){
        return topicReplyDAO.getTopicReplyById(replyId);
    }

    @Override
    public List<TopicDO> listTopicDesc(int count) {
        return topicDAO.listTopicDESCByCount(count);
    }

    @Override
    public List<TopicReplyDO> listTopicAllReply(int topicId) {
        return topicReplyDAO.listTopicReplyByTopicId(topicId);
    }

    @Override
    public Boolean alterTopicContent(int topicId, String content) {
        int updateTopicContentEffectRow = topicContentDAO.updateContentByTopicId(topicId, content);
        if (updateTopicContentEffectRow == 0) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean alterTopicReplyContent(int replyId, String content) {
       int updateTopicReplyEffectRow = topicReplyDAO.updateContentByIdByContent(replyId, content);
        if (updateTopicReplyEffectRow == 0) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean alterTopicStatistics(int topicId, String field, String type) {
        //int updateTopicContent
        if ("add".equals(type)) {
            if ("comment".equals(field)) {
                topicDAO.updateCommentAddOneById(topicId);
            } else if ("read".equals(field)) {
                topicContentDAO.updateReadAddOneByTopicId(topicId);
            } else if ("agree".equals(field)) {
                topicContentDAO.updateAgreeAddOneByTopicId(topicId);
            } else {
                return false;
            }

            return true;

        } else if ("cut".equals(type)) {
            if ("comment".equals(field)) {
                topicDAO.updateCommentCutOneById(topicId);
            } else if ("agree".equals(field)) {
                topicContentDAO.updateAgreeCutOneByTopicId(topicId);
            } else{
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public Boolean alterTopicReplyStatistics(int replyId, String field, String type) {
        if ("add".equals(type)) {
            if ("agree".equals(field)) {
                topicReplyDAO.updateAgreeAddOneById(replyId);
            } else if ("oppose".equals(field)) {
                topicReplyDAO.updateOpposeAddOneById(replyId);
            } else {
                return false;
            }

            return true;

        } else if ("cut".equals(type)) {
            if ("agree".equals(field)) {
                topicReplyDAO.updateAgreeCutOneById(replyId);
            } else if ("oppose".equals(field)) {
                topicReplyDAO.updateOpposeCutOneById(replyId);
            } else {
                return false;
            }

            return true;
        }

        return false;
    }
}
