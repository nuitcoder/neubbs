package org.neusoft.neubbs.service.impl;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
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
import org.neusoft.neubbs.entity.properties.NeubbsConfigDO;
import org.neusoft.neubbs.service.ITopicService;
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

    private Logger logger = Logger.getLogger(TopicServiceImpl.class);

    private final ITopicDAO topicDAO;
    private final ITopicContentDAO topicContentDAO;
    private final ITopicReplyDAO topicReplyDAO;
    private final IUserDAO userDAO;

    private final NeubbsConfigDO neubbsConfig;

    @Autowired
    public TopicServiceImpl(ITopicDAO topicDAO, ITopicContentDAO topicContentDAO,
                            ITopicReplyDAO topicReplyDAO, IUserDAO userDAO,
                            NeubbsConfigDO neubbsConfig) {
        this.topicDAO = topicDAO;
        this.topicContentDAO = topicContentDAO;
        this.topicReplyDAO = topicReplyDAO;
        this.userDAO = userDAO;
        this.neubbsConfig = neubbsConfig;
    }

    @Override
    public void alterTopicContent(int topicId, String newCategory, String newTitle, String newTopicContent) {
        this.confirmTopicNotNull(topicId);

        //update category,title from forum_topic
        if (topicDAO.updateCategoryById(topicId, newCategory) == 0
                || topicDAO.updateTitleById(topicId, newTitle) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_07);
        }

        //update conent from forum_topic_content
        if (topicContentDAO.updateContentByTopicId(topicId, newTopicContent) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_08);
        }
    }

    @Override
    public void alterReplyContent(int replyId, String newReplyContent) {
        this.confirmTopicReplyNotNull(replyId);

        //update reply content from forum_topic_reply
        if (topicReplyDAO.updateContentByIdByContent(replyId, newReplyContent) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_09);
        }
    }

    @Override
    public int countTopicTotalPages(int limit, String category, String username) {
        if (limit == SetConst.ZERO) {
            limit = neubbsConfig.getTopicsApiRequestParamLimitDefault();
        }

        int topicCount = this.countTopicCount(category, username);

        return topicCount % limit == 0 ? topicCount / limit : topicCount / limit + 1;
    }

    @Override
    public Map<String, Object> getTopicContentPageModelMap(int topicId) {
        TopicDO topic = this.getTopicDO(topicId);
        TopicContentDO topicContent = this.getTopicContentDO(topicId);
        UserDO topicAuthorUser = this.getTopicAuthorUserDO(topic.getUserid());

        Map<String, Object> topicMap = this.getTopicInfoMap(topic);
        Map<String, Object> topicContentMap = this.getTopicContentInfoMap(topicContent);
        Map<String, Object> authorUserMap = this.getTopicUserInfoMap(topicAuthorUser);

        //consider topic no reply user
        Integer topicLastReplyUserId = topic.getLastreplyuserid();
        Map<String, Object> lastReplyUserMap = topicLastReplyUserId == null
                ? this.getTopicUserInfoMap(null)
                : this.getTopicUserInfoMap(this.getTopicAuthorUserDO(topicLastReplyUserId));


        //get topic reply list map,
        List<TopicReplyDO> listReply = topicReplyDAO.listTopicReplyByTopicId(topicId);
        List<Map<String, Object>> listReplyMap = new ArrayList<>(listReply.size());
        for (TopicReplyDO reply : listReply) {
            Map<String, Object> replyMap = this.getTopicReplyInfoMap(reply);
            replyMap.put(ParamConst.USER, this.getTopicUserInfoMap(this.getTopicReplyAuthorUserDO(reply.getUserid())));

            listReplyMap.add(replyMap);
        }

        //merge all information map
        topicMap.putAll(topicContentMap);
        topicMap.put(ParamConst.USER, authorUserMap);
        topicMap.put(ParamConst.LAST_REPLY_USER, lastReplyUserMap);
        topicMap.put(ParamConst.REPLYS, listReplyMap);

        return topicMap;
    }

    @Override
    public Map<String, Object> getReplyPageModelMap(int replyId) {
        TopicReplyDO reply = this.getTopicReplyDO(replyId);
        Map<String, Object> replyInfoMap = this.getTopicReplyInfoMap(reply);

        //if user == null, no throw exception
        UserDO replyUser = userDAO.getUserById(reply.getUserid());
        replyInfoMap.put(ParamConst.USER, this.getTopicUserInfoMap(replyUser));

        return replyInfoMap;
    }


    @Override
    public List<Map<String, Object>> listTopics(int limit, int page, String category, String username) {
        //if limit = 0, explain no input limit, use neubbs.properties default param
        if (limit == SetConst.ZERO) {
            limit = neubbsConfig.getTopicsApiRequestParamLimitDefault();
        }

        //confirm no exceed database topic number
        this.confirmNoExceedTopicNumber(limit, page);

        //get specified page topic list
        List<TopicDO> listTopic = this.getTopicList(limit, page, category, username);

        //foreach topic list
        List<Map<String, Object>> topicsList = new ArrayList<>(listTopic.size());
        for (TopicDO topic : listTopic) {
            //get information map
            Map<String, Object> topicInfoMap = this.getTopicInfoMap(topic);
            Map<String, Object> topicContentInfoMap
                    = this.getTopicContentInfoMap(this.getTopicContentDO(topic.getId()));
            Map<String, Object> authorUserMap = this.getTopicUserInfoMap(userDAO.getUserById(topic.getUserid()));
            Map<String, Object> lastReplyUserMap
                    = this.getTopicUserInfoMap(userDAO.getUserById(topic.getLastreplyuserid()));

            //result map remove category key-value, when category != null
            if (category != null) {
                topicInfoMap.remove(ParamConst.CATEGORY);
            }
            //result map remove user key-value, when username != null
            if (username != null) {
                topicInfoMap.put(ParamConst.USER, authorUserMap);
            }

            //merge all information map
            topicInfoMap.putAll(topicContentInfoMap);
            topicInfoMap.put(ParamConst.USER, authorUserMap);
            topicInfoMap.put(ParamConst.LAST_REPLY_USER, lastReplyUserMap);
            topicsList.add(topicInfoMap);
        }

        return topicsList;
    }

    @Override
    public List<String> listTopicCategory() {
        return topicDAO.listTopicCategory();
    }

    @Override
    public void removeTopic(int topicId) {
        this.confirmTopicNotNull(topicId);

        //delete topic data from forum_topic_content
        if (topicContentDAO.removeTopicContentById(topicId) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_05);
        }

        //delete topic all reply from forum_topic_reply
        if (topicReplyDAO.removeListTopicReplyByTopicId(topicId) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_06);
        }

        //delete topic from forum_topic
        if (topicDAO.removeTopicById(topicId) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_04);
        }
    }

    @Override
    public void removeReply(int replyId) {
        this.confirmTopicReplyNotNull(replyId);

         //delete reply from forum_topic_reply
         if (topicReplyDAO.removeTopicReplyById(replyId) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_05);
         }

         //update comment -1 from forum_topic
         int replyTopicId = topicReplyDAO.getTopicReplyById(replyId).getTopicid();
         if (topicDAO.updateCommentCutOneById(replyTopicId) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_07);
        }
    }

    @Override
    public int saveTopic(int userId, String category, String title, String topicContent) {
        //because userId of cookie already cookie, so no null empty check

        //insert forum_topic table
        TopicDO topic = new TopicDO();
            topic.setUserid(userId);
            topic.setCategory(category);
            topic.setTitle(title);

        if (topicDAO.saveTopic(topic) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_01);
        }

        //insert forum_topic_content table
        TopicContentDO topicContentDO = new TopicContentDO();
            topicContentDO.setTopicid(topic.getId());
            topicContentDO.setContent(topicContent);

        if (topicContentDAO.saveTopicContent(topicContentDO) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_02);
        }

        return topic.getId();
    }

    @Override
    public int saveReply(int userId, int topicId, String replyContent) {
        this.confirmTopicNotNull(topicId);

        //insert forum_topic_reply
        TopicReplyDO topicReply = new TopicReplyDO();
        topicReply.setUserid(userId);
        topicReply.setTopicid(topicId);
        topicReply.setContent(replyContent);

        if (topicReplyDAO.saveTopicReply(topicReply) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_03);
        }

        //update comment, lastreplyuserid, lastreplytime from forum_topic
        if (topicDAO.updateCommentAddOneById(topicId) == 0
                ||  topicDAO.updateLastreplyuseridById(topicId, userId) == 0
                || topicDAO.updateLastreplytimeById(topicId, new Date()) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_03);
        }

        return topicReply.getId();
    }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /**
     * 确认话题不为空（存在指定 topicId 话题）
     *      - forum_topic
     *      - forum_topic_content
     *
     * @param topicId 话题id
     */
    private void confirmTopicNotNull(int topicId) {
        if (topicDAO.getTopicById(topicId) == null || topicContentDAO.getTopicContentById(topicId) == null) {
            throwNoTopicException(topicId);
        }
    }

    /**
     * 确认话题回复不为空（存在指定 replyId 话题回复）
     *
     * @param replyId 话题回复id
     */
    private void confirmTopicReplyNotNull(int replyId) {
        if (topicReplyDAO.getTopicReplyById(replyId) == null) {
            throw new TopicErrorException(ApiMessage.NO_REPLY).log(replyId + LogWarn.TOPIC_11);
        }
    }

    /**
     * 确认用户不能为空
     *
     * @param user 用户对象
     */
    private void confirmUserNotNull(UserDO user) {
        if (user == null) {
            throwNoUserException();
        }
    }

    /**
     * 确认不会超过话题数量
     *
     * @param limit 每页显示限制
     * @param page 跳转指定页数
     */
    private void confirmNoExceedTopicNumber(int limit, int page) {
        int topicCount = topicDAO.countTopic();
        int maxPage = topicCount % limit == 0 ? topicCount / limit : topicCount / limit + 1;

        if (limit > topicCount || page > maxPage) {
            throw new TopicErrorException(ApiMessage.FAIL_GET_TOPIC_LSIT)
                    .log(LogWarn.TOPIC_12
                            + "（话题总数 = " + topicCount
                            + "，若 limit = " + limit
                            + "，最多跳转至 " + maxPage + " 页）");
        }
    }

    /**
     * 获取话题数量
     *      - 分类 + 用户名 查询
     *      - 分类查询
     *      - 用户名查询
     *      - (no finish) support the same input category and username
     *
     * @param category 话题分类
     * @param username 用户名
     * @return int 话题数量
     */
    private int countTopicCount(String category, String username) {
        //mybatis count value default = 0
        int topicCount;
        if (category != null && username != null) {
            //the same input username and category
            topicCount = 0;
        } else if (category != null) {
            topicCount = topicDAO.countTopicByCategory(category);
        } else if (username != null) {
            topicCount = topicDAO.countTopicByUserid(this.getTopicAuthorUserDOByName(username).getId());
        } else {
            topicCount = topicDAO.countTopic();
        }

        return topicCount;
    }

    /**
     * 获取 TopicDO
     *      - 不存在则抛出异常
     *
     * @param topicId 话题id
     * @return TopicDO 话题对象
     */
    private TopicDO getTopicDO(int topicId) {
        TopicDO topicDO = topicDAO.getTopicById(topicId);
        if (topicDO == null) {
            this.throwNoTopicException(topicId);
        }
        return topicDO;
    }

    /**
     * 获取话题回复列表
     *      - 不存在则抛出异常
     *
     * @param topicId 话题id
     * @return List 话题回复列表
     */
    private List<TopicReplyDO> getTopicReplyDOList(int topicId) {
        List<TopicReplyDO> replys = topicReplyDAO.listTopicReplyByTopicId(topicId);
        if (replys == null) {
            replys = new ArrayList<>(SetConst.SIZE_TWO);
        }

        return replys;
    }

    /**
     * 获取 TopicReplyDO
     *      - 不存在则抛出异常
     *
     * @param replyId 回复id
     * @return TopicReplyDO 话题回复对象
     */
    private TopicReplyDO getTopicReplyDO(int replyId) {
        TopicReplyDO topicReplyDO = topicReplyDAO.getTopicReplyById(replyId);
        if (topicReplyDO == null) {
            throw new TopicErrorException(ApiMessage.NO_REPLY).log(replyId + LogWarn.TOPIC_11);
        }
        return topicReplyDO;
    }


    /**
     * 获取 TopicContentDO
     *      - 不存在抛出异常
     *
     * @param topicId 话题id
     * @return TopicContentDO 话题内容对象
     */
    private TopicContentDO getTopicContentDO(int topicId) {
        TopicContentDO topicContentDO = topicContentDAO.getTopicContentById(topicId);
        if (topicContentDO == null) {
            this.throwNoTopicException(topicId);
        }
        return topicContentDO;
    }

    /**
     * 获取话题作者用户信息
     *
     * @param userId 用户id
     * @return UserDO 话题发表人用户对象
     */
    private UserDO getTopicAuthorUserDO(int userId) {
        return this.getUserNotNull(userId);
    }

    /**
     * 获取 话题回复发表人信息信息
     *
     * @param replyAuthorUserId 发表人用户id
     * @return UserDO 回复发表人用户对象
     */
    private UserDO getTopicReplyAuthorUserDO(int replyAuthorUserId) {
        return this.getUserNotNull(replyAuthorUserId);
    }

    /**
     * 获取用户对象不能为空
     *
     * @param userId 用户id
     * @return UserDO 用户对象
     */
    private UserDO getUserNotNull(int userId) {
        UserDO user = userDAO.getUserById(userId);
        this.confirmUserNotNull(user);

        return user;
    }

    /**
     * 获取话题作者用户信息
     *
     * @param username 用户名
     * @return UserDO 话题发表人用户对象
     */
    private UserDO getTopicAuthorUserDOByName(String username) {
        UserDO user = userDAO.getUserByName(username);
        this.confirmUserNotNull(user);

        return user;
    }

    /**
     * 获取话题列表
     *      - (no finish )support the same input category and username
     *
     * @param limit 每页显示数量
     * @param page 跳转指定页数
     * @param category 话题分类
     * @param username 用户名
     * @return 话题集合列表
     */
    private List<TopicDO> getTopicList(int limit, int page, String category, String username) {
        int startRow = (page - 1) * limit;

        //mybatis default return list is empty object， no equal null
        List<TopicDO> topicList;
        if (category != null && username != null) {
            //the same input username and category
            topicList = new ArrayList<>(SetConst.SIZE_ONE);
        } else if (category != null) {
            topicList = topicDAO.listTopicByStartRowByCountByCategory(startRow, limit, category);
        } else if (username != null) {
            UserDO user = this.getTopicAuthorUserDOByName(username);
            topicList = topicDAO.listTopicByStartRowByCountByUserId(startRow, limit, user.getId());
        } else {
            topicList = topicDAO.listTopicByStartRowByCount(startRow, limit);
        }

        return topicList;
    }

    /**
     * 获取话题信息 Map
     *
     * @param topic 话题对象
     * @return Map 已处理的话题信息
     */
    private Map<String, Object> getTopicInfoMap(TopicDO topic) {
        Map<String, Object> topicInfoMap = JsonUtil.toMapByObject(topic);
        MapFilterUtil.filterTopicInfo(topicInfoMap);

        return topicInfoMap;
    }

    /**
     * 获取话题内容信息 Map
     *
     * @param topicContent 话题内容对象
     * @return Map 已处理的话题内容信息
     */
    private Map<String, Object> getTopicContentInfoMap(TopicContentDO topicContent) {
        Map<String, Object> topicContentInfoMap = JsonUtil.toMapByObject(topicContent);
        MapFilterUtil.filterTopicContentInfo(topicContentInfoMap);

        return topicContentInfoMap;
    }

    /**
     * 获取话题回复信息 Map
     *
     * @param topicReply 话题回复对象
     * @return Map 已处理的话题回复信息
     */
    private Map<String, Object> getTopicReplyInfoMap(TopicReplyDO topicReply) {
        Map<String, Object> topicReplyInfoMap = JsonUtil.toMapByObject(topicReply);
        MapFilterUtil.filterTopicReply(topicReplyInfoMap);

        return topicReplyInfoMap;
    }

    /**
     * 获取话题用户信息 Map
     *
     * @param topicUser 话题用户对象
     * @return Map 已处理的话题用户信息
     */
    private Map<String, Object> getTopicUserInfoMap(UserDO topicUser) {
        if (topicUser == null) {
            LinkedHashMap<String, Object> userInfoMap = new LinkedHashMap<>(SetConst.SIZE_TWO);
                userInfoMap.put(ParamConst.USERNAME, "");
                userInfoMap.put(ParamConst.AVATOR, "");

            return userInfoMap;
        }

        //json -> Map, filter topic user information map
        Map<String, Object> userInfoMap = JsonUtil.toMapByObject(topicUser);
        MapFilterUtil.filterTopicUserInfo(userInfoMap);

        return userInfoMap;
    }

    /**
     * 抛出不存在话题异常
     *
     * @param topicId 话题id
     */
    private void throwNoTopicException(int topicId) {
        throw new TopicErrorException(ApiMessage.NO_TOPIC).log(topicId + LogWarn.TOPIC_10);
    }

    /**
     * 抛出不存在用户异常
     */
    private void throwNoUserException() {
        throw new AccountErrorException(ApiMessage.NO_USER).log(LogWarn.ACCOUNT_01);
    }
}
