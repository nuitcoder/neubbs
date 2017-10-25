package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.db.DatabaseInfo;
import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
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
public class TopicServiceImpl implements ITopicService{

    private final ITopicDAO topicDAO;
    private final ITopicContentDAO topicContentDAO;
    private final ITopicReplyDAO topicReplyDAO;

    /**
     * Constructor
     */
    @Autowired
    public TopicServiceImpl(ITopicDAO topicDAO, ITopicContentDAO topicContentDAO, ITopicReplyDAO topicReplyDAO){
        this.topicDAO = topicDAO;
        this.topicContentDAO = topicContentDAO;
        this.topicReplyDAO = topicReplyDAO;
    }


    @Override
    public void saveTopic(int userId, String category, String title, String content) throws Exception{
        TopicDO topic = new TopicDO();
            topic.setUserid(userId);
            topic.setCategory(category);
            topic.setTitle(title);

        int topicEffectRow = topicDAO.saveTopic(topic);
        if (topicEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_SAVE_FAIL);
        }

        TopicContentDO topicContent = new TopicContentDO();
            topicContent.setTopicid(topic.getId());
            topicContent.setContent(content);

        int topicContentEffectRow = topicContentDAO.saveTopicContent(topicContent);
        if (topicContentEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_CONTENT_SAVE_FAIL);
        }
    }

    @Override
    public void saveReply(int userId, int topicId, String content) throws Exception{
        TopicReplyDO topicReply = new TopicReplyDO();
            topicReply.setUserid(userId);
            topicReply.setTopicid(topicId);
            topicReply.setContent(content);

        int topicReplyEffectRow = topicReplyDAO.saveTopicReply(topicReply);
        if (topicReplyEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_REPLY_SAVE_FAIL);
        }

        int updateTopicCommentEffectRow = topicDAO.updateCommentAddOneById(topicId);
        int updateTopicLastreplyuserid = topicDAO.updateLastreplyuseridById(topicId, userId);
        int updateTopicLastreplytime  = topicDAO.updateLastreplytimeById(topicId, new Date());
        if (updateTopicCommentEffectRow == 0 || updateTopicLastreplyuserid == 0 || updateTopicLastreplytime == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_ALTER_FAIL);
        }
    }

    @Override
    public void removeTopic(int topicId) throws Exception{
        int removeTopicEffectRow = topicDAO.removeTopicById(topicId);
        int removeTopicContentEffectRow = topicContentDAO.removeTopicContentById(topicId);
        int removeTopicReplyEffectRow = topicReplyDAO.removeTopicReplyByTopicId(topicId);
        if (removeTopicEffectRow == 0 || removeTopicContentEffectRow == 0 || removeTopicReplyEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_REMOVE_FAIL);
        }
    }

    @Override
    public void removeReply(int replyId) throws Exception{
       TopicReplyDO topicReply = topicReplyDAO.getTopicReplyById(replyId);

       int updateTopicEffectRow = topicDAO.updateCommentCutOneById(topicReply.getTopicid());
       if (updateTopicEffectRow == 0) {
           throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_ALTER_FAIL);
       }

       int removeTopicReplyEffectRow = topicReplyDAO.removeTopicReplyById(replyId);
       if (removeTopicReplyEffectRow == 0) {
           throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_REPLY_REMOVE_FAIL);
       }
    }

    @Override
    public void alterTopicContent(int topicId, String content) throws Exception{
        int updateTopicContentEffectRow = topicContentDAO.updateContentByTopicId(topicId, content);
        if (updateTopicContentEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_CONTENT_ALTER_FAIL);
        }
    }

    @Override
    public void alterTopicReplyContent(int replyId, String content) throws Exception{
        int updateTopicReplyEffectRow = topicReplyDAO.updateContentByIdByContent(replyId, content);
        if (updateTopicReplyEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_REPLY_ALTER_FAIL);
        }
    }

    @Override
    public void alterTopicStatistics(int topicId, String field, String type) throws Exception{
        //int updateTopicContent
        if ("add".equals(type)) {
            if ("comment".equals(field)) {
                topicDAO.updateCommentAddOneById(topicId);
            } else if ("read".equals(field)) {
                topicContentDAO.updateReadAddOneByTopicId(topicId);
            } else if ("agree".equals(field)) {
                topicContentDAO.updateAgreeAddOneByTopicId(topicId);
            } else {

            }

        } else if ("cut".equals(type)) {
            if ("comment".equals(field)) {
                topicDAO.updateCommentCutOneById(topicId);
            } else if ("agree".equals(field)) {
                topicContentDAO.updateAgreeCutOneByTopicId(topicId);
            } else{

            }
        }
    }

    @Override
    public void alterTopicReplyStatistics(int replyId, String field, String type) throws Exception{
        if ("add".equals(type)) {
            if ("agree".equals(field)) {
                topicReplyDAO.updateAgreeAddOneById(replyId);
            } else if ("oppose".equals(field)) {
                topicReplyDAO.updateOpposeAddOneById(replyId);
            } else {
            }


        } else if ("cut".equals(type)) {
            if ("agree".equals(field)) {
                topicReplyDAO.updateAgreeCutOneById(replyId);
            } else if ("oppose".equals(field)) {
                topicReplyDAO.updateOpposeCutOneById(replyId);
            } else {
            }
        }

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
}
