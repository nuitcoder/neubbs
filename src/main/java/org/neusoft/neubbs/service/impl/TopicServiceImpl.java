package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.MapFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> getTopic(int topicId) throws Exception {
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);

        TopicDO topic = topicDAO.getTopicById(topicId);
        if (topic == null) {
            throw new TopicErrorException(ApiMessage.NO_TOPIC).log(LogWarn.TOPIC_10);
        }

        Map<String, Object> topicMap = JsonUtil.toMapByObject(topic);
            MapFilterUtil.filterTopicInfo(topicMap);

        TopicContentDO topicContent = topicContentDAO.getTopicContentById(topicId);
        Map<String, Object> topicContentMap = JsonUtil.toMapByObject(topicContent);
            MapFilterUtil.filterTopicContentInfo(topicContentMap);

        UserDO authorUser = userDAO.getUserById(topic.getUserid());
        Map<String, Object> authorUserMap = JsonUtil.toMapByObject(authorUser);
            MapFilterUtil.filterTopicUserInfo(authorUserMap);

        UserDO lastReplyUser = userDAO.getUserById(topic.getLastreplyuserid());
        Map<String, Object> lastReplyUserMap = JsonUtil.toMapByObject(lastReplyUser);
            MapFilterUtil.filterTopicUserInfo(lastReplyUserMap);

        //回复列表
        List<Map<String, Object>> listReplyMap = new ArrayList<>();
        List<TopicReplyDO> listReplyDO = topicReplyDAO.listTopicReplyByTopicId(topicId);
        for (TopicReplyDO reply : listReplyDO) {
            Map<String, Object> replyMap = JsonUtil.toMapByObject(reply);
                MapFilterUtil.filterTopicReply(replyMap);

            UserDO replyUser = userDAO.getUserById(reply.getUserid());
            Map<String, Object> replyUserMap = JsonUtil.toMapByObject(replyUser);
                MapFilterUtil.filterTopicUserInfo(replyUserMap);

            replyMap.put("user", replyUserMap);
            listReplyMap.add(replyUserMap);
        }

        //最后结果储存至一个 map
        topicMap.putAll(topicContentMap);
        topicMap.put("user", authorUserMap);
        topicMap.put("lastreplyuser", lastReplyUserMap);
        topicMap.put("replys", listReplyMap);

        return topicMap;
    }

    @Override
    public Map<String, Object> getReply(int replyId) throws Exception {
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
        TopicReplyDO reply = topicReplyDAO.getTopicReplyById(replyId);
        Map<String, Object> replyMap = JsonUtil.toMapByObject(reply);
            MapFilterUtil.filterTopicReply(replyMap);

        UserDO replyUser = userDAO.getUserById(reply.getUserid());
        Map<String, Object> replyUserMap = JsonUtil.toMapByObject(replyUser);
            MapFilterUtil.filterTopicUserInfo(replyUserMap);

        replyMap.put("user", replyUserMap);

        return replyMap;
    }

    @Override
    public List<Map<String, Object>> listTopics(int page, int limit) throws Exception {
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
        //计算开始行数
        int topicCount = topicDAO.countTopic();
        if (limit > topicCount || (page * limit) > topicCount) {
            throw new TopicErrorException(ApiMessage.FAIL_GET_TOPIC_LSIT).log(LogWarn.TOPIC_12);
        }

        List<Map<String, Object>> resultTopics = new ArrayList<>();
        List<TopicDO> listTopic = topicDAO.listTopicByStartRowByCount((page - 1) * limit, limit);
        for (TopicDO topic : listTopic) {
            Map<String, Object> topicMap = JsonUtil.toMapByObject(topic);
                MapFilterUtil.filterTopicInfo(topicMap);

            TopicContentDO topicContent = topicContentDAO.getTopicContentById(topic.getId());
            Map<String, Object> topicContentMap = JsonUtil.toMapByObject(topicContent);
                MapFilterUtil.filterTopicContentInfo(topicContentMap);

            UserDO authorUser = userDAO.getUserById(topic.getUserid());
            Map<String, Object> authorUserMap = JsonUtil.toMapByObject(authorUser);
                MapFilterUtil.filterTopicUserInfo(authorUserMap);

            UserDO lastReplyUser = userDAO.getUserById(topic.getLastreplyuserid());
            Map<String, Object> lastReplyUserMap = JsonUtil.toMapByObject(lastReplyUser);
                MapFilterUtil.filterTopicUserInfo(lastReplyUserMap);

            topicMap.putAll(topicContentMap);
            topicMap.put("user", authorUserMap);
            topicMap.put("lastreplyuser", lastReplyUserMap);

            resultTopics.add(topicMap);
        }

        return resultTopics;
    }

    @Override
    public int saveTopic(int userId, String category, String title, String topicContent) throws Exception {
        //判断用户是否存在
        UserDO user = userDAO.getUserById(userId);
        if (user == null) {
            throw new AccountErrorException(ApiMessage.NO_USER).log(LogWarn.ACCOUNT_01);
        }

        //保存 forum_topic 表
        TopicDO topic = new TopicDO();
            topic.setUserid(userId);
            topic.setCategory(category);
            topic.setTitle(title);

        int topicEffectRow = topicDAO.saveTopic(topic);
        if (topicEffectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_01);
        }

        //保存 forum_topic_content 表
        TopicContentDO topicContentDO = new TopicContentDO();
            topicContentDO.setTopicid(topic.getId());
            topicContentDO.setContent(topicContent);

        int topicContentEffectRow = topicContentDAO.saveTopicContent(topicContentDO);
        if (topicContentEffectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_02);
        }

        return topic.getId();
    }

    @Override
    public int saveReply(int userId, int topicId, String replyContent) throws Exception {
        //验证用户 id 和 话题 id
        if (userDAO.getUserById(userId) == null) {
            throw new AccountErrorException(ApiMessage.NO_USER).log(LogWarn.ACCOUNT_01);
        }
        if (topicDAO.getTopicById(topicId) == null) {
            throw new TopicErrorException(ApiMessage.NO_TOPIC).log(LogWarn.TOPIC_10);
        }

        //保存回复
        TopicReplyDO topicReply = new TopicReplyDO();
            topicReply.setUserid(userId);
            topicReply.setTopicid(topicId);
            topicReply.setContent(replyContent);

        int topicReplyEffectRow = topicReplyDAO.saveTopicReply(topicReply);
        if (topicReplyEffectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_03);
        }

        //更新 forum_topic 评论数，评论最后回复人 id，最后回复时间
        int updateTopicCommentEffectRow = topicDAO.updateCommentAddOneById(topicId);
        int updateTopicLastreplyuserid = topicDAO.updateLastreplyuseridById(topicId, userId);
        int updateTopicLastreplytime  = topicDAO.updateLastreplytimeById(topicId, new Date());
        if (updateTopicCommentEffectRow == 0 || updateTopicLastreplyuserid == 0 || updateTopicLastreplytime == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_03);
        }

        return topicReply.getId();
    }

    @Override
    public void removeTopic(int topicId) throws Exception {
        //验证话题 id
        if (topicDAO.getTopicById(topicId) == null) {
            throw new TopicErrorException(ApiMessage.NO_TOPIC).log(LogWarn.TOPIC_10);
        }

        //删除 forum_topic_content 和 forum_topic_reply，forum_topic 相应数据（需先删除外键关联）
        int removeTopicContentEffectRow = topicContentDAO.removeTopicContentById(topicId);
        if (removeTopicContentEffectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_05);
        }

        //删除指定话题的回复
        topicReplyDAO.removeListTopicReplyByTopicId(topicId);

        int removeTopicEffectRow = topicDAO.removeTopicById(topicId);
        if (removeTopicEffectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_04);
        }
    }

    @Override
    public void removeReply(int replyId) throws Exception {
        //判断是否存在
       TopicReplyDO topicReply = topicReplyDAO.getTopicReplyById(replyId);
        if (topicReply == null) {
            throw new TopicErrorException(ApiMessage.NO_TOPIC).log(LogWarn.TOPIC_11);
        }

        //删除回复
       int removeTopicReplyEffectRow = topicReplyDAO.removeTopicReplyById(replyId);
       if (removeTopicReplyEffectRow == 0) {
           throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_05);
       }

        //评论数 -1
       int updateTopicEffectRow = topicDAO.updateCommentCutOneById(topicReply.getTopicid());
       if (updateTopicEffectRow == 0) {
           throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_07);
       }
    }

    @Override
    public void alterTopicContent(int topicId, String newCategory, String newTitle, String newTopicContent)
                                        throws Exception {
        if (topicDAO.getTopicById(topicId) == null) {
            throw new TopicErrorException(ApiMessage.NO_TOPIC).log(LogWarn.TOPIC_10);
        }

        // 修改话题分类，标题
        int updateTopicCategoryEffectRow = topicDAO.updateCategoryById(topicId, newCategory);
        int updateTopicTitleEffectRow = topicDAO.updateTitleById(topicId, newTitle);
        if (updateTopicCategoryEffectRow == 0 || updateTopicTitleEffectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_07);
        }

        //修改话题内容
        int updateTopicContentEffectRow = topicContentDAO.updateContentByTopicId(topicId, newTopicContent);
        if (updateTopicContentEffectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_08);
        }
    }

    @Override
    public void alterReplyContent(int replyId, String newReplyContent) throws Exception {
        if (topicReplyDAO.getTopicReplyById(replyId) == null) {
            throw new TopicErrorException(ApiMessage.NO_REPLY).log(LogWarn.TOPIC_11);
        }

        int updateTopicReplyEffectRow = topicReplyDAO.updateContentByIdByContent(replyId, newReplyContent);
        if (updateTopicReplyEffectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_09);
        }
    }
}
