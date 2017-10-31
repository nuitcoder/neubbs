package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.AccountInfo;
import org.neusoft.neubbs.constant.api.TopicInfo;
import org.neusoft.neubbs.constant.db.DatabaseInfo;
import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * ITopicService 接口实现类
 *
 * @author Suvan
 */
@Service("topicServiceImpl")
public class TopicServiceImpl implements ITopicService {

    private final IUserDAO userDAO;
    private final ITopicDAO topicDAO;
    private final ITopicContentDAO topicContentDAO;
    private final ITopicReplyDAO topicReplyDAO;

    /**
     * Constructor
     */
    @Autowired
    public TopicServiceImpl(IUserDAO userDAO, ITopicDAO topicDAO,
                                ITopicContentDAO topicContentDAO, ITopicReplyDAO topicReplyDAO) {
        this.userDAO = userDAO;
        this.topicDAO = topicDAO;
        this.topicContentDAO = topicContentDAO;
        this.topicReplyDAO = topicReplyDAO;
    }


    @Override
    public int saveTopic(int userId, String category, String title, String content) throws Exception {
        //判断用户是否存在
        UserDO user = userDAO.getUserById(userId);
        if (user == null) {
            throw new AccountErrorException(AccountInfo.NO_USER).log(LogWarnInfo.ACCOUNT_01);
        }

        //保存 forum_topic 表
        TopicDO topic = new TopicDO();
            topic.setUserid(userId);
            topic.setCategory(category);
            topic.setTitle(title);

        int topicEffectRow = topicDAO.saveTopic(topic);
        if (topicEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_01);
        }

        //保存 forum_topic_content 表
        TopicContentDO topicContent = new TopicContentDO();
            topicContent.setTopicid(topic.getId());
            topicContent.setContent(content);

        int topicContentEffectRow = topicContentDAO.saveTopicContent(topicContent);
        if (topicContentEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION)
                        .log(LogWarnInfo.TOPIC_02);
        }

        return topic.getId();
    }

    @Override
    public int saveReply(int userId, int topicId, String content) throws Exception {
        //验证用户 id 和 话题 id
        if (userDAO.getUserById(userId) == null) {
            throw new AccountErrorException(AccountInfo.NO_USER).log(LogWarnInfo.ACCOUNT_01);
        }
        if (topicDAO.getTopicById(topicId) == null) {
            throw new TopicErrorException(TopicInfo.NO_TOPIC).log(LogWarnInfo.TOPIC_10);
        }

        //保存回复
        TopicReplyDO topicReply = new TopicReplyDO();
            topicReply.setUserid(userId);
            topicReply.setTopicid(topicId);
            topicReply.setContent(content);

        int topicReplyEffectRow = topicReplyDAO.saveTopicReply(topicReply);
        if (topicReplyEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_03);
        }

        //更新 forum_topic 评论数，评论最后回复人 id，最后回复时间
        int updateTopicCommentEffectRow = topicDAO.updateCommentAddOneById(topicId);
        int updateTopicLastreplyuserid = topicDAO.updateLastreplyuseridById(topicId, userId);
        int updateTopicLastreplytime  = topicDAO.updateLastreplytimeById(topicId, new Date());
        if (updateTopicCommentEffectRow == 0 || updateTopicLastreplyuserid == 0 || updateTopicLastreplytime == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_03);
        }

        return topicReply.getId();
    }

    @Override
    public void removeTopic(int topicId) throws Exception {
        //验证话题 id
        if (topicDAO.getTopicById(topicId) == null) {
            throw new TopicErrorException(TopicInfo.NO_TOPIC).log(LogWarnInfo.TOPIC_10);
        }

        //删除 forum_topic，forum_topic_content 和 forum_topic_reply 相应数据（需先删除外键关联）
        int removeTopicContentEffectRow = topicContentDAO.removeTopicContentById(topicId);
        if (removeTopicContentEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_05);
        }

        int removeTopicEffectRow = topicDAO.removeTopicById(topicId);
        if (removeTopicEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_04);
        }

        //删除指定主题所有回复
        int removeTopicReplyEffectRow = topicReplyDAO.removeTopicReplyByTopicId(topicId);
    }

    @Override
    public void removeReply(int replyId) throws Exception {
       TopicReplyDO topicReply = topicReplyDAO.getTopicReplyById(replyId);
        if (topicReply == null) {
            throw new TopicErrorException(TopicInfo.NO_TOPIC).log(LogWarnInfo.TOPIC_11);
        }

       int updateTopicEffectRow = topicDAO.updateCommentCutOneById(topicReply.getTopicid());
       if (updateTopicEffectRow == 0) {
           throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_07);
       }

       int removeTopicReplyEffectRow = topicReplyDAO.removeTopicReplyById(replyId);
       if (removeTopicReplyEffectRow == 0) {
           throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_05);
       }
    }

    @Override
    public void alterTopicContent(int topicId, String category, String title, String content) throws Exception {
        if (topicDAO.getTopicById(topicId) == null) {
            throw new TopicErrorException(TopicInfo.NO_TOPIC).log(LogWarnInfo.TOPIC_10);
        }

        int updateTopicCategoryEffectRow = topicDAO.updateCategoryById(topicId, category);
        int updateTopicTitleEffectRow = topicDAO.updateTitleById(topicId, title);
        if (updateTopicCategoryEffectRow == 0 || updateTopicTitleEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_07);
        }

        int updateTopicContentEffectRow = topicContentDAO.updateContentByTopicId(topicId, content);
        if (updateTopicContentEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_08);
        }
    }

    @Override
    public void alterReplyContent(int replyId, String content) throws Exception {
        if (topicReplyDAO.getTopicReplyById(replyId) == null) {
            throw new TopicErrorException(TopicInfo.NO_REPLY).log(LogWarnInfo.TOPIC_11);
        }

        int updateTopicReplyEffectRow = topicReplyDAO.updateContentByIdByContent(replyId, content);
        if (updateTopicReplyEffectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.TOPIC_09);
        }
    }
}
