package org.neusoft.neubbs.service.impl;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.MapFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ITopicService 接口实现类
 *
 * @author Suvan
 */
@Service("topicServiceImpl")
public class TopicServiceImpl implements ITopicService {

    private final IUserService userService;

    private final ITopicDAO topicDAO;
    private final ITopicContentDAO topicContentDAO;
    private final ITopicReplyDAO topicReplyDAO;

    private Logger logger = Logger.getLogger(TopicServiceImpl.class);

    /**
     * Constructor
     */
    @Autowired
    public TopicServiceImpl(IUserService userService, ITopicDAO topicDAO,
                                 ITopicContentDAO topicContentDAO, ITopicReplyDAO topicReplyDAO) {
        this.userService = userService;
        this.topicDAO = topicDAO;
        this.topicContentDAO = topicContentDAO;
        this.topicReplyDAO = topicReplyDAO;
    }

    @Override
    public TopicDO getTopicDOByTopicId(int topicId) throws TopicErrorException {
        TopicDO topic = topicDAO.getTopicById(topicId);
        if (topic == null) {
            throw new TopicErrorException(ApiMessage.NO_TOPIC).log(topicId + LogWarn.TOPIC_10);
        }
        return topic;
    }

    @Override
    public TopicContentDO getTopicContentDOByTopicId(int topicId) throws TopicErrorException {
        TopicContentDO topicContent = topicContentDAO.getTopicContentById(topicId);
        if (topicContent == null) {
            throw new TopicErrorException(ApiMessage.NO_TOPIC).log(topicId + LogWarn.TOPIC_10);
        }
        return topicContent;
    }

    @Override
    public TopicReplyDO getTopicReplyDOByReplyId(int replyId) throws TopicErrorException {
        TopicReplyDO topicReplyDO = topicReplyDAO.getTopicReplyById(replyId);
        if (topicReplyDO == null) {
            throw new TopicErrorException(ApiMessage.NO_REPLY).log(replyId + LogWarn.TOPIC_11);
        }
        return topicReplyDO;
    }

    @Override
    public Map<String, Object> getTopic(int topicId) throws TopicErrorException, AccountErrorException {

        //获取数据
        TopicDO topic = this.getTopicDOByTopicId(topicId);
        Map<String, Object> topicMap = JsonUtil.toMapByObject(topic);

        Map<String, Object> topicContentMap = JsonUtil.toMapByObject(this.getTopicContentDOByTopicId(topicId));
        Map<String, Object> authorUserMap = JsonUtil.toMapByObject(userService.getUserInfoById(topic.getUserid()));

        Integer lastReplyUserId = topic.getLastreplyuserid();
        Map<String, Object> lastReplyUserMap = null;
        if (lastReplyUserId != null) {
            lastReplyUserMap = JsonUtil.toMapByObject(userService.getUserInfoById(lastReplyUserId));
            MapFilterUtil.filterTopicUserInfo(lastReplyUserMap);
        }

        //过滤信息
        MapFilterUtil.filterTopicInfo(topicMap);
        MapFilterUtil.filterTopicContentInfo(topicContentMap);
        MapFilterUtil.filterTopicUserInfo(authorUserMap);


        //回复列表
        List<Map<String, Object>> listReplyMap = new ArrayList<>();
        List<TopicReplyDO> listReply = topicReplyDAO.listTopicReplyByTopicId(topicId);
        for (TopicReplyDO reply : listReply) {
            Map<String, Object> replyMap = JsonUtil.toMapByObject(reply);
            Map<String, Object> replyUserMap = JsonUtil.toMapByObject(userService.getUserInfoById(reply.getUserid()));

            MapFilterUtil.filterTopicReply(replyMap);
            MapFilterUtil.filterTopicUserInfo(replyUserMap);

            replyMap.put("user", replyUserMap);
            listReplyMap.add(replyMap);
        }

        //最后结果储存至一个 map
        topicMap.putAll(topicContentMap);
        topicMap.put("user", authorUserMap);
        topicMap.put("lastreplyuser", lastReplyUserMap);
        topicMap.put("replys", listReplyMap);

        return topicMap;
    }

    @Override
    public Map<String, Object> getReply(int replyId) throws AccountErrorException {
        TopicReplyDO reply = topicReplyDAO.getTopicReplyById(replyId);
        Map<String, Object> replyMap = JsonUtil.toMapByObject(reply);

        Map<String, Object> replyUserMap = JsonUtil.toMapByObject(userService.getUserInfoById(reply.getUserid()));

        MapFilterUtil.filterTopicReply(replyMap);
        MapFilterUtil.filterTopicUserInfo(replyUserMap);

        replyMap.put("user", replyUserMap);

        return replyMap;
    }

    @Override
    public List<Map<String, Object>> listTopics(int limit, int page) throws TopicErrorException, AccountErrorException {
        //获取话题总数，判断输入 page，limit 是否超出指定范围
        int topicCount = topicDAO.countTopic();
        int maxPage = topicCount % limit == 0 ? topicCount / limit : topicCount / limit + 1;
        if (limit > topicCount || page > maxPage) {
            throw new TopicErrorException(ApiMessage.FAIL_GET_TOPIC_LSIT)
                    .log(LogWarn.TOPIC_12
                            + "（话题总数 = " + topicCount
                            + "，若 limit = " + limit
                            + "，最多跳转至 " + maxPage  + " 页）");
        }

        //根据 page and limit，获取指定列数的话题列表
        List<TopicDO> listTopic = topicDAO.listTopicByStartRowByCount((page - 1) * limit, limit);

        //遍历话题列表
        List<Map<String, Object>> resultTopics = new ArrayList<>();
        for (TopicDO topic : listTopic) {
            Map<String, Object> topicMap = JsonUtil.toMapByObject(topic);

            TopicContentDO topicContent = topicContentDAO.getTopicContentById(topic.getId());
            if (topicContent == null) {
                //forum_topic_content 无对应 id，跳过该条记录
                logger.warn(topic.getId() + LogWarn.TOPIC_10);
                continue;
            }
            Map<String, Object> topicContentMap = JsonUtil.toMapByObject(topicContent);

            Map<String, Object> authorUserMap = JsonUtil.toMapByObject(userService.getUserInfoById(topic.getUserid()));

            Map<String, Object> lastReplyUserMap = new LinkedHashMap<>();
            if (topic.getLastreplyuserid() != null) {
                lastReplyUserMap = JsonUtil.toMapByObject(userService.getUserInfoById(topic.getLastreplyuserid()));
                MapFilterUtil.filterTopicUserInfo(lastReplyUserMap);
            }
            
            MapFilterUtil.filterTopicInfo(topicMap);
            MapFilterUtil.filterTopicContentInfo(topicContentMap);
            MapFilterUtil.filterTopicUserInfo(authorUserMap);

            topicMap.putAll(topicContentMap);
            topicMap.put("user", authorUserMap);
            topicMap.put("lastreplyuser", lastReplyUserMap);

            resultTopics.add(topicMap);
        }

        return resultTopics;
    }

    @Override
    public int saveTopic(int userId, String category, String title, String topicContent)
            throws AccountErrorException, DatabaseOperationFailException {
        //判断用户是否存在
        userService.getUserInfoById(userId);

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
    public int saveReply(int userId, int topicId, String replyContent)
            throws AccountErrorException, TopicErrorException, DatabaseOperationFailException {
        //验证用户 id 和 话题 id
        userService.getUserInfoById(userId);
        this.getTopicDOByTopicId(topicId);

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
    public void removeTopic(int topicId) throws TopicErrorException, DatabaseOperationFailException {
        //验证话题 id
        this.getTopicDOByTopicId(topicId);

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
    public void removeReply(int replyId) throws TopicErrorException, DatabaseOperationFailException {
        //判断是否存在
       TopicReplyDO topicReply = getTopicReplyDOByReplyId(replyId);

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
            throws TopicErrorException, DatabaseOperationFailException {
        //判断话题是否存在
        this.getTopicDOByTopicId(topicId);
        this.getTopicContentDOByTopicId(topicId);

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
    public void alterReplyContent(int replyId, String newReplyContent)
            throws TopicErrorException, DatabaseOperationFailException {
        //判断回复是否存在
        this.getTopicReplyDOByReplyId(replyId);

        int updateTopicReplyEffectRow = topicReplyDAO.updateContentByIdByContent(replyId, newReplyContent);
        if (updateTopicReplyEffectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_09);
        }
    }
}
