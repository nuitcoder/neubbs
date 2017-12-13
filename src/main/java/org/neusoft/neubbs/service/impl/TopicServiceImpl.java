package org.neusoft.neubbs.service.impl;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.dao.ITopicCategoryDAO;
import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.TopicCategoryDO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.entity.properties.NeubbsConfigDO;
import org.neusoft.neubbs.exception.AccountErrorException;
import org.neusoft.neubbs.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.exception.TopicErrorException;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.MapFilterUtil;
import org.neusoft.neubbs.utils.StringUtil;
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
    private final ITopicCategoryDAO topicCategoryDAO;
    private final ITopicReplyDAO topicReplyDAO;
    private final IUserDAO userDAO;

    private final NeubbsConfigDO neubbsConfig;

    @Autowired
    public TopicServiceImpl(ITopicDAO topicDAO, ITopicContentDAO topicContentDAO,
                            ITopicReplyDAO topicReplyDAO, ITopicCategoryDAO topicCategoryDAO,
                            IUserDAO userDAO, NeubbsConfigDO neubbsConfig) {
        this.topicDAO = topicDAO;
        this.topicContentDAO = topicContentDAO;
        this.topicReplyDAO = topicReplyDAO;
        this.topicCategoryDAO = topicCategoryDAO;
        this.userDAO = userDAO;
        this.neubbsConfig = neubbsConfig;
    }

    @Override
    public int saveTopic(int userId, String categoryNick, String title, String topicContent) {
        //because userId of cookie already cookie, so no null empty check
        TopicCategoryDO category = this.getTopicCategoryNotNullByNick(categoryNick);

        //insert forum_topic table
        TopicDO topic = new TopicDO();
            topic.setUserid(userId);
            topic.setCategoryid(category.getId());
            topic.setTitle(title);

        if (topicDAO.saveTopic(topic) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_01);
        }

        //insert forum_topic_content
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
        this.getTopicNotNull(topicId);

        //insert forum_topic_reply
        TopicReplyDO topicReply = new TopicReplyDO();
            topicReply.setUserid(userId);
            topicReply.setTopicid(topicId);
            topicReply.setContent(replyContent);

        if (topicReplyDAO.saveTopicReply(topicReply) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_03);
        }

        //update comment, lastreplyuserid, lastreplytime from forum_topic
        Date topicLastReplyTime = topicReplyDAO.getTopicReplyById(topicReply.getId()).getCreatetime();
        if (topicDAO.updateRepliesAddOneById(topicId) == 0
                ||  topicDAO.updateLastReplyUserIdById(topicId, userId) == 0
                || topicDAO.updateLastReplyTimeById(topicId, topicLastReplyTime) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_03);
        }

        return topicReply.getId();
    }

    @Override
    public Map<String, Object> saveCategory(String categoryNick, String categoryName) {

        if (topicCategoryDAO.getTopicCategoryByNick(categoryNick) != null) {
            throw new TopicErrorException(ApiMessage.ALREAD_EXIST_CATEGORY_NICK)
                    .log(categoryNick + LogWarn.TOPIC_15);
        }

        if (topicCategoryDAO.getTopicCategoryByName(categoryName) != null) {
            throw new TopicErrorException(ApiMessage.ALREAD_EXIST_CATEGORY_NAME)
                    .log(categoryName + LogWarn.TOPIC_16);
        }

        TopicCategoryDO category = new TopicCategoryDO();
            category.setNick(categoryNick);
            category.setName(categoryName);
        topicCategoryDAO.saveTopicCategory(category);

        return this.getTopicCategoryInfoMap(category);
    }

    @Override
    public void removeTopic(int topicId) {
        this.getTopicNotNull(topicId);

        //delete topic data from forum_topic_content
        if (topicContentDAO.removeTopicContentByTopicId(topicId) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_05);
        }

        //delete topic all reply from forum_topic_reply
        if (topicReplyDAO.removeTopicAllReplyByTopicId(topicId) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_06);
        }

        //delete topic from forum_topic
        if (topicDAO.removeTopicById(topicId) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_04);
        }
    }

    @Override
    public void removeReply(int replyId) {
        TopicReplyDO reply = this.getTopicReplyNotNull(replyId);

        //delete reply from forum_topic_reply
        if (topicReplyDAO.removeTopicReplyById(replyId) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_05);
        }

        //update replies -1 from forum_topic (lastreplytime no update)
        if (topicDAO.updateRepliesCutOneById(reply.getTopicid()) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_07);
        }
    }

    @Override
    public void removeCategory(String categoryNick) {
        TopicCategoryDO category = this.getTopicCategoryNotNullByNick(categoryNick);
        userDAO.removeUserById(category.getId());
    }

    @Override
    public int countTopicTotalPages(int limit, String categoryNick, String username) {
        if (limit == SetConst.ZERO) {
            limit = neubbsConfig.getTopicsApiRequestParamLimitDefault();
        }

        //confirm user input categoryNick or username param (null express user no input)
        int categoryId = categoryNick == null
                ? 0 : this.getTopicCategoryNotNullByNick(categoryNick).getId();
        int userId = username == null
                ? 0 : this.getUserNotNullByName(username).getId();

        int topicNumber = this.countTopicCount(categoryId, userId);
        this.confirmQueryTopicListResultSizeNotEqualZero(topicNumber);

        //count totalpages by limit by topicNumber
        return topicNumber % limit == 0 ? topicNumber / limit : topicNumber / limit + 1;
    }

    @Override
    public Map<String, Object> getTopicContentPageModelMap(int topicId) {
        TopicDO topic = this.getTopicNotNull(topicId);
        TopicContentDO topicContent = this.getTopicContentNotNull(topicId);
        TopicCategoryDO topicCategory = this.getTopicCategoryNotNullById(topic.getCategoryid());
        UserDO topicAuthorUser = this.getUserNotNullById(topic.getUserid());

        Map<String, Object> topicInfoMap = this.getTopicInfoMap(topic);
        Map<String, Object> topicContentInfoMap = this.getTopicContentInfoMap(topicContent);
        Map<String, Object> topicCategoryInfoMap = this.getTopicCategoryInfoMap(topicCategory);
        Map<String, Object> authorUserInfoMap = this.getTopicUserInfoMap(topicAuthorUser);

        //consider topic no reply user
        Integer topicLastReplyUserId = topic.getLastreplyuserid();
        Map<String, Object> lastReplyUserMap = topicLastReplyUserId == null
                ? this.getTopicUserInfoMap(null)
                : this.getTopicUserInfoMap(this.getUserNotNullById(topicLastReplyUserId));


        //get topic reply list map,
        List<TopicReplyDO> listReply = topicReplyDAO.listTopicReplyByTopicId(topicId);
        List<Map<String, Object>> listReplyInfoMap = new ArrayList<>(listReply.size());
        for (TopicReplyDO reply : listReply) {
            Map<String, Object> replyInfoMap = this.getTopicReplyInfoMap(reply);

            //allow user empty(if replyUser=null, explain database exist garbage data)
            replyInfoMap.put(ParamConst.USER, this.getTopicUserInfoMap(userDAO.getUserById(reply.getUserid())));

            listReplyInfoMap.add(replyInfoMap);
        }

        //merge all information map
        topicInfoMap.putAll(topicContentInfoMap);
        topicInfoMap.put(ParamConst.CATEGORY, topicCategoryInfoMap);
        topicInfoMap.put(ParamConst.USER, authorUserInfoMap);
        topicInfoMap.put(ParamConst.LAST_REPLY_USER, lastReplyUserMap);
        topicInfoMap.put(ParamConst.REPLYS, listReplyInfoMap);

        //topic read + 1
        if (topicContentDAO.updateReadAddOneByTopicId(topicId) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_07);
        }

        return topicInfoMap;
    }

    @Override
    public Map<String, Object> getReplyPageModelMap(int replyId) {
        TopicReplyDO reply = this.getTopicReplyNotNull(replyId);
        Map<String, Object> replyInfoMap = this.getTopicReplyInfoMap(reply);

        //if user == null, throw exception
        UserDO replyUser = this.getUserNotNullById(reply.getUserid());
        replyInfoMap.put(ParamConst.USER, this.getTopicUserInfoMap(replyUser));

        //remove replyid, because the function is based on replyid to query
        replyInfoMap.remove(ParamConst.REPLY_ID);

        return replyInfoMap;
    }

    @Override
    public List<Map<String, Object>> listTopics(int limit, int page, String categoryNick, String username) {
        //if limit = 0, explain no input limit, use neubbs.properties default param
        if (limit == SetConst.ZERO) {
            limit = neubbsConfig.getTopicsApiRequestParamLimitDefault();
        }

        this.confirmNoExceedTopicNumber(limit, page);

        int categoryId = StringUtil.isEmpty(categoryNick)
                ? 0 : this.getTopicCategoryNotNullByNick(categoryNick).getId();
        int userId = StringUtil.isEmpty(username)
                ? 0 : this.getUserNotNullByName(username).getId();

        List<TopicDO> dbTopicList = this.getTopicList(limit, page, categoryId, userId);
        this.confirmQueryTopicListResultSizeNotZero(dbTopicList);

        List<Map<String, Object>> resultTopicList = new ArrayList<>(dbTopicList.size());
        //foreach topic list
        for (TopicDO topic : dbTopicList) {
            //get information map (if obj == null, explain database exist garbage data)
            Map<String, Object> topicInfoMap = this.getTopicInfoMap(topic);
            Map<String, Object> topicContentInfoMap = this.getTopicContentInfoMap(this.getTopicContent(topic.getId()));
            Map<String, Object> topicCategoryInfoMap
                    = this.getTopicCategoryInfoMap(this.getTopicCategory(topic.getCategoryid()));
            Map<String, Object> authorUserMap = this.getTopicUserInfoMap(this.getUser(topic.getUserid()));
            Map<String, Object> lastReplyUserMap = this.getTopicUserInfoMap(this.getUser(topic.getLastreplyuserid()));

            //merge all information map
            topicInfoMap.putAll(topicContentInfoMap);
            topicInfoMap.put(ParamConst.CATEGORY, topicCategoryInfoMap);
            topicInfoMap.put(ParamConst.USER, authorUserMap);
            topicInfoMap.put(ParamConst.LAST_REPLY_USER, lastReplyUserMap);

            resultTopicList.add(topicInfoMap);
        }

        return resultTopicList;
    }

    @Override
    public List<Map<String, Object>> listAllTopicCategorys() {
        List<TopicCategoryDO> allCategoryList = topicCategoryDAO.listAllTopicCategory();

        List<Map<String, Object>> resultCategoryList = new ArrayList<>(allCategoryList.size());
        for (TopicCategoryDO category: allCategoryList) {
            resultCategoryList.add(this.getTopicCategoryInfoMap(category));
        }

        return resultCategoryList;
    }

    @Override
    public void alterTopicContent(int topicId, String categoryNick, String newTitle, String newTopicContent) {
        //confirm topicid not null
        this.getTopicNotNull(topicId);
        this.getTopicContentNotNull(topicId);
        TopicCategoryDO category = this.getTopicCategoryNotNullByNick(categoryNick);

        //update category,title from forum_topic
        if (topicDAO.updateCategoryById(topicId, category.getId()) == 0
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
        //confirm reply not null
        this.getTopicReplyNotNull(replyId);

        //update reply content from forum_topic_reply
        if (topicReplyDAO.updateContentByIdByContent(replyId, newReplyContent) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_09);
        }
    }

    @Override
    public void alterTopicReadAddOne(int topicId) {
        this.getTopicNotNull(topicId);

        if (topicContentDAO.updateReadAddOneByTopicId(topicId) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_07);
        }
    }

    @Override
    public int alterTopicLikeByInstruction(boolean isCurrentUserLikeTopic, int topicId, String instruction) {
        //judge current user whether repeat operation(no repeat input 'inc' or 'dec')
        boolean isIncOfInstruction = instruction.equals(SetConst.INC);
        if (isCurrentUserLikeTopic && isIncOfInstruction) {
            throw new TopicErrorException(ApiMessage.NO_REAPEAT_INC_TOPIC_LIKE).log(LogWarn.TOPIC_20);
        } else if (!isCurrentUserLikeTopic && !isIncOfInstruction) {
            throw new TopicErrorException(ApiMessage.NO_REAPEAT_DEC_TOPIC_LIKE).log(LogWarn.TOPIC_21);
        }

        //update forum_topic_content 'like'
        TopicContentDO topicContent = this.getTopicContentNotNull(topicId);
        int effectRow = isIncOfInstruction
                ? topicContentDAO.updateLikeAddOneByTopicId(topicId)
                : topicContentDAO.updateLikeCutOneByTopicId(topicId);
        if (effectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.TOPIC_07);
        }

        return isIncOfInstruction
                ? topicContent.getLike() + SetConst.ONE : topicContent.getLike() - SetConst.ONE;
    }

    @Override
    public void alterTopicCategoryDescription(String categoryNick, String newDescription) {
        this.getTopicCategoryNotNullByNick(categoryNick);

        if (topicCategoryDAO.updateDescriptionByNick(categoryNick, newDescription) == 0) {
            throw new DatabaseOperationFailException(categoryNick + ApiMessage.DATABASE_EXCEPTION)
                    .log(LogWarn.TOPIC_19);
        }
    }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /*
     * ***********************************************
     * count method
     * ***********************************************
     */

    /**
     * 获取话题数量
     *      - 话题分类 id + 用户 id
     *      - 话题分类 id
     *      - 用户 id
     *      - 默认
     *
     * @param categoryId 话题分类id
     * @param userId 用户id
     * @return int 话题数量
     */
    private int countTopicCount(int categoryId, int userId) {
        //if categoryId=0 or userId=0, explain user no input
        int topicCount;
        if (categoryId != 0 && userId != 0) {
            topicCount = topicDAO.countTopicByCategoryIdByUserId(categoryId, userId);
        } else if (categoryId != 0) {
            topicCount = topicDAO.countTopicByCategoryId(categoryId);
        } else if (userId != 0) {
            topicCount = topicDAO.countTopicByUserId(userId);
        } else {
            topicCount = topicDAO.countTopic();
        }

        return topicCount;
    }

    /*
     * ***********************************************
     * confirm method
     * ***********************************************
     */

    /**
     * 确认不会超过话题数量
     *
     * @param limit 每页显示限制
     * @param page 跳转指定页数
     */
    private void confirmNoExceedTopicNumber(int limit, int page) {
        int allTopicTotals = topicDAO.countTopic();
        int maxPage = allTopicTotals % limit == 0
                ? allTopicTotals / limit : allTopicTotals / limit + 1;

        if (limit > allTopicTotals || page > maxPage) {
            throw new TopicErrorException(ApiMessage.QUERY_EXCEED_TOPIC_NUMBER)
                    .log(LogWarn.TOPIC_12
                            + "（话题总数 = " + allTopicTotals
                            + "，若 limit = " + limit
                            + "，最多跳转至 " + maxPage + " 页）");
        }
    }

    /**
     * 确认查询话题列表结果不为空
     *      - 根据条件查询的话题列表，列表长度不为 0
     *
     * @param topicList 话题集合
     */
    private void confirmQueryTopicListResultSizeNotZero(List<TopicDO> topicList) {
        if (topicList.size() == SetConst.ZERO) {
            throw new TopicErrorException(ApiMessage.NO_QUERY_TOPICS).log(LogWarn.TOPIC_17);
        }
    }

    /**
     * 确认查询话题总页数不等于 0
     *      - 根据条件查询的获得的话题总页数，结果不为 0
     *
     * @param topicNumber 话题数量
     */
    private void confirmQueryTopicListResultSizeNotEqualZero(int topicNumber) {
        if (topicNumber == SetConst.ZERO) {
            throw new TopicErrorException(ApiMessage.NO_QUERY_TOPICS).log(LogWarn.TOPIC_18);
        }
    }

    /*
     * ***********************************************
     * get method
     * ***********************************************
     */

    /**
     * 获取用户对象
     *      - 可以为空
     *
     * @param userId 用户id
     * @return UserDO 用户对象
     */
    private UserDO getUser(Integer userId) {
        return userId == null ? null : userDAO.getUserById(userId);
    }

    /**
     * 获取话题对象
     *      - 可以为空
     *
     * @param topicId 话题 id
     * @return TopicDO 话题对象
     */
    private TopicDO getTopic(Integer topicId) {
        return topicId == null ? null : topicDAO.getTopicById(topicId);
    }

    /**
     * 获取话题内容对象
     *      - 可以为空
     *
     * @param topicId 话题 id
     * @return TopicContentDO 话题内容对象
     */
    private TopicContentDO getTopicContent(Integer topicId) {
        return topicId == null ? null : topicContentDAO.getTopicContentByTopicId(topicId);
    }

    /**
     * 获取话题分类对象
     *      - 可以为空
     *
     * @param categoryId 分类id
     * @return TopicCategoryDO 话题分类对象
     */
    private TopicCategoryDO getTopicCategory(Integer categoryId) {
        return categoryId == null ? null : topicCategoryDAO.getTopicCategoryById(categoryId);
    }

    /**
     * 获取话题回复对象
     *      - 可以为空
     *
     * @param replyId 回复 id
     * @return TopicReplyDO 话题回复对象
     */
    private TopicReplyDO getTopicReply(Integer replyId) {
        return replyId == null ? null : topicReplyDAO.getTopicReplyById(replyId);
    }

    /**
     * 获取话题，不能为空
     *      - forum_topic
     *
     * @param topicId 话题id
     * @return TopicDO 话题对象
     */
    private TopicDO getTopicNotNull(int topicId) {
        TopicDO topic = topicDAO.getTopicById(topicId);
        if (topic == null) {
            throwNoTopicException(topicId);
        }

        return topic;
    }

    /**
     * 获取话题内容，不能为空
     *      - forum_topic_content
     *
     * @param topicId 话题id
     * @return TopicContentDO 话题内容对象
     */
    private TopicContentDO getTopicContentNotNull(int topicId) {
        TopicContentDO topicContent = topicContentDAO.getTopicContentByTopicId(topicId);
        if (topicContent == null) {
            throwNoTopicContentException(topicId);
        }

        return topicContent;
    }

    /**
     * （id）确认话题分类不为空
     *      - forum_topic_category
     *
     * @param categoryId 分类Id
     * @return  TopicCategoryDO 话题分类对象
     */
    private TopicCategoryDO getTopicCategoryNotNullById(int categoryId) {
        TopicCategoryDO category = topicCategoryDAO.getTopicCategoryById(categoryId);
        if (category == null) {
            throwNotCategoryExceptionById(categoryId);
        }

        return category;
    }

    /**
     * （Nick）确认话题分类不为空
     *      - forum_topic_category
     *
     * @param categoryNick 话题昵称（英文）
     * @return TopicCategoryDO 话题分类对象
     */
    private TopicCategoryDO getTopicCategoryNotNullByNick(String categoryNick) {
        TopicCategoryDO category = topicCategoryDAO.getTopicCategoryByNick(categoryNick);
        if (category == null) {
            throwNotCategoryExceptionByNick(categoryNick);
        }

        return category;
    }

    /**
     * 获取话题列表
     *      - 话题分类 id + 用户名 id
     *      - 话题分类 id
     *      - 用户 id
     *      - 默认
     *
     * @param limit 每页显示数量
     * @param page 跳转指定页数
     * @param categoryId 话题分类id
     * @param userId 用户id
     * @return 话题集合列表
     */
    private List<TopicDO> getTopicList(int limit, int page, int categoryId, int userId) {
        //if categoryId=0 or userId=0, explain user no input
        int startRow = (page - 1) * limit;
        List<TopicDO> topicList;
        if (categoryId != 0 && userId != 0) {
            topicList
                    = topicDAO.listTopicDESCByStartRowByCountByCategoryIdByUserId(startRow, limit, categoryId, userId);
        } else if (categoryId != 0) {
            topicList = topicDAO.listTopicDESCByStartRowByCountByCategoryId(startRow, limit, categoryId);
        } else if (userId != 0) {
            topicList = topicDAO.listTopicDESCByStartRowByCountByUserId(startRow, limit, userId);
        } else {
            topicList = topicDAO.listTopicDESCByStartRowByCount(startRow, limit);
        }

        return topicList;
    }

    /**
     * 确认话题回复不为空
     *      - forum_topic_reply
     *
     * @param replyId 话题回复id
     * @return TopicReplyDO 话题回复对象
     */
    private TopicReplyDO getTopicReplyNotNull(int replyId) {
        TopicReplyDO reply = topicReplyDAO.getTopicReplyById(replyId);
        if (reply == null) {
            throw new TopicErrorException(ApiMessage.NO_REPLY).log(replyId + LogWarn.TOPIC_11);
        }

        return reply;
    }

    /**
     * 获取话题回复列表
     *      - 查询列表返回的是空对象
     *
     * @param topicId 话题id
     * @return List 话题回复列表
     */
    private List<TopicReplyDO> getTopicReplyDOList(int topicId) {
        List<TopicReplyDO> replys = topicReplyDAO.listTopicReplyByTopicId(topicId);

        return replys.size() != 0 ? replys : new ArrayList<>(SetConst.SIZE_TWO);
    }

    /**
     * （name）确认用户不能为空
     *      - forum_user
     *
     * @param username 用户名
     * @return UserDO 用户对象
     */
    private UserDO getUserNotNullByName(String username) {
        UserDO user = userDAO.getUserByName(username);
        if (user == null) {
            throwNoUserExceptionByName(username);
        }

        return user;
    }

    /**
     * （id）确认用户不能为空
     *
     * @param userId 用户名
     * @return UserDO 用户对象
     */
    private UserDO getUserNotNullById(int userId) {
        UserDO user = userDAO.getUserById(userId);
        if (user == null) {
            throwNoUserExceptionById(userId);
        }

        return user;
    }

    /*
     * ***********************************************
     * filter information map (use util/MapFilter.java)
     * ***********************************************
     */

    /**
     * 获取话题基本信息 Map
     *
     * @param topic 话题对象
     * @return Map 已处理的话题信息
     */
    private Map<String, Object> getTopicInfoMap(TopicDO topic) {
        if (topic == null) {
            return new LinkedHashMap<>(SetConst.SIZE_ONE);
        }

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
        if (topicContent == null) {
            return new LinkedHashMap<>(SetConst.SIZE_ONE);
        }

        Map<String, Object> topicContentInfoMap = JsonUtil.toMapByObject(topicContent);
        MapFilterUtil.filterTopicContentInfo(topicContentInfoMap);

        return topicContentInfoMap;
    }

    private Map<String, Object> getTopicCategoryInfoMap(TopicCategoryDO category) {
        if (category == null) {
            return new LinkedHashMap<>(SetConst.SIZE_ONE);
        }

        Map<String, Object> topicCategoryInfoMap = JsonUtil.toMapByObject(category);
        MapFilterUtil.filterTopicCategory(topicCategoryInfoMap);

        return topicCategoryInfoMap;
    }

    /**
     * 获取话题用户信息 Map
     *
     * @param topicUser 话题用户对象
     * @return Map 已处理的话题用户信息
     */
    private Map<String, Object> getTopicUserInfoMap(UserDO topicUser) {
        if (topicUser == null) {
            //build empty map
            LinkedHashMap<String, Object> topicUserInfoMap = new LinkedHashMap<>(SetConst.SIZE_TWO);
                topicUserInfoMap.put(ParamConst.USERNAME, "");
                topicUserInfoMap.put(ParamConst.AVATOR, "");

            return topicUserInfoMap;
        }

        //json -> Map, filter topic user information map
        Map<String, Object> topicUserInfoMap = JsonUtil.toMapByObject(topicUser);
        MapFilterUtil.filterTopicUserInfo(topicUserInfoMap);

        return topicUserInfoMap;
    }

    /**
     * 获取话题回复信息 Map
     *
     * @param topicReply 话题回复对象
     * @return Map 已处理的话题回复信息
     */
    private Map<String, Object> getTopicReplyInfoMap(TopicReplyDO topicReply) {
        if (topicReply == null) {
            return new LinkedHashMap<>(SetConst.SIZE_ONE);
        }

        Map<String, Object> topicReplyInfoMap = JsonUtil.toMapByObject(topicReply);
        MapFilterUtil.filterTopicReply(topicReplyInfoMap);

        return topicReplyInfoMap;
    }

    /*
     * ***********************************************
     * throw exception method
     * ***********************************************
     */

    /**
     * 抛出不存在话题异常
     *
     * @param topicId 话题id
     */
    private void throwNoTopicException(int topicId) {
        throw new TopicErrorException(ApiMessage.NO_TOPIC)
                .log("topicId=" + topicId + LogWarn.TOPIC_10);
    }

    /**
     * 抛出不存在话题内容异常
     *
     * @param topicId 话题id
     */
    private void throwNoTopicContentException(int topicId) {
        throw new TopicErrorException(ApiMessage.NO_TOPIC)
                .log("topic content topicId=" + topicId + LogWarn.TOPIC_10);
    }

    /**
     * (id)抛出不存在用户异常
     *
     * @param userId 用户id
     */
    private void throwNoUserExceptionById(int userId) {
        throw new AccountErrorException(ApiMessage.NO_USER)
                .log("userId=" + userId + LogWarn.ACCOUNT_01);
    }

    /**
     * （username）抛出不存在用户异常
     *
     * @param username 用户名
     */
    private void throwNoUserExceptionByName(String username) {
        throw new AccountErrorException(ApiMessage.NO_USER)
                .log("username=" + username + LogWarn.ACCOUNT_01);
    }

    /**
     * （id）抛出不存话题分类异常
     */
    private void throwNotCategoryExceptionById(int categoryId) {
        throw new TopicErrorException(ApiMessage.NO_CATEGORY)
                .log("nategroyId=" + categoryId + LogWarn.TOPIC_14);
    }

    /**
     * （nick）抛出不存话题分类异常
     */
    private void throwNotCategoryExceptionByNick(String nick) {
        throw new TopicErrorException(ApiMessage.NO_CATEGORY)
                .log("CategoryNick=" + nick + LogWarn.TOPIC_14);
    }

    /**
     * 抛出不存在话题回复异常
     *
     * @param replyId 回复id
     */
    private void throwNoReplyException(int replyId) {
        throw new TopicErrorException(ApiMessage.NO_REPLY)
                .log("replyId=" + replyId + LogWarn.TOPIC_11);
    }
}
