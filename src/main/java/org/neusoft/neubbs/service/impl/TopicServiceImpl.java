package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.dao.ITopicActionDAO;
import org.neusoft.neubbs.dao.ITopicCategoryDAO;
import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.dao.IUserActionDAO;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.TopicActionDO;
import org.neusoft.neubbs.entity.TopicCategoryDO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.entity.properties.NeubbsConfigDO;
import org.neusoft.neubbs.exception.ServiceException;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.MapFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    private final ITopicDAO topicDAO;
    private final ITopicContentDAO topicContentDAO;
    private final ITopicCategoryDAO topicCategoryDAO;
    private final ITopicReplyDAO topicReplyDAO;
    private final IUserDAO userDAO;
    private final IUserActionDAO userActionDAO;
    private final ITopicActionDAO topicActionDAO;

    private final NeubbsConfigDO neubbsConfig;

    @Autowired
    public TopicServiceImpl(ITopicDAO topicDAO, ITopicContentDAO topicContentDAO,
                            ITopicReplyDAO topicReplyDAO, ITopicCategoryDAO topicCategoryDAO,
                            IUserDAO userDAO, NeubbsConfigDO neubbsConfig,
                            IUserActionDAO userActionDAO, ITopicActionDAO topicActionDAO) {
        this.topicDAO = topicDAO;
        this.topicContentDAO = topicContentDAO;
        this.topicReplyDAO = topicReplyDAO;
        this.topicCategoryDAO = topicCategoryDAO;
        this.userDAO = userDAO;
        this.neubbsConfig = neubbsConfig;
        this.userActionDAO = userActionDAO;
        this.topicActionDAO = topicActionDAO;
    }

    @Override
    public Map<String, Object> saveTopic(int userId, String categoryNick, String title, String topicContent) {
        //judge whether exist category(nick)
        TopicCategoryDO category = this.getTopicCategoryNotNullByNick(categoryNick);

        //build TopicDO, TopicContentDO, TopicActionDO
        TopicDO topic = new TopicDO();
            topic.setUserid(userId);
            topic.setCategoryid(category.getId());
            topic.setTitle(title);

        TopicContentDO topicContentDO = new TopicContentDO();
            topicContentDO.setTopicid(topic.getId());
            topicContentDO.setContent(topicContent);

        TopicActionDO topicAction = new TopicActionDO();
            topicAction.setTopicId(topic.getId());

        //insert forum_topic, forum_topic_content, forum_topic_action
        if (topicDAO.saveTopic(topic) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS1);
        }

        if (topicContentDAO.saveTopicContent(topicContentDO) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS2);
        }

        if (topicActionDAO.saveTopicAction(topicAction) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS24);
        }

        return MapFilterUtil.generateMap(ParamConst.TOPIC_ID, topic.getId());
    }

    @Override
    public Map<String, Object> saveReply(int userId, int topicId, String replyContent) {
        this.getTopicNotNull(topicId);

        //save topic reply
        TopicReplyDO topicReply = new TopicReplyDO();
            topicReply.setUserid(userId);
            topicReply.setTopicid(topicId);
            topicReply.setContent(replyContent);

        if (topicReplyDAO.saveTopicReply(topicReply) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS3);
        }

        //update forum_topic 'replies', 'lastReplyUserId', 'lastReplyTime'
        Date topicLastReplyTime = topicReplyDAO.getTopicReplyById(topicReply.getId()).getCreatetime();
        if (topicDAO.updateRepliesAddOneById(topicId) == 0
                ||  topicDAO.updateLastReplyUserIdById(topicId, userId) == 0
                || topicDAO.updateLastReplyTimeById(topicId, topicLastReplyTime) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS3);
        }

        return MapFilterUtil.generateMap(ParamConst.REPLY_ID, topicReply.getId());
    }

    @Override
    public Map<String, Object> saveCategory(String categoryNick, String categoryName) {
        if (topicCategoryDAO.getTopicCategoryByNick(categoryNick) != null) {
            throw new ServiceException(ApiMessage.ALREADY_EXIST_CATEGORY_NICK).log(LogWarnEnum.TS14);
        }

        if (topicCategoryDAO.getTopicCategoryByName(categoryName) != null) {
            throw new ServiceException(ApiMessage.ALREADY_EXIST_CATEGORY_NAME).log(LogWarnEnum.TS15);
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

        //delete forum_topic_reply, forum_topic_content, forum_topic
        if (topicReplyDAO.removeTopicAllReplyByTopicId(topicId) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS6);
        }

        if (topicContentDAO.removeTopicContentByTopicId(topicId) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS5);
        }

        if (topicDAO.removeTopicById(topicId) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS4);
        }
    }

    @Override
    public void removeReply(int replyId) {
        TopicReplyDO reply = this.getTopicReplyNotNull(replyId);

        //delete reply
        if (topicReplyDAO.removeTopicReplyById(replyId) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS5);
        }

        //update replies -1 from forum_topic (but lastReplyTime no update)
        if (topicDAO.updateRepliesCutOneById(reply.getTopicid()) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS7);
        }
    }

    @Override
    public int countTopicTotals() {
        return topicDAO.countTopic();
    }

    @Override
    public int countReplyTotals() {
        return topicReplyDAO.countReply();
    }

    @Override
    public Map<String, Object> countTopicTotalPages(int limit, String categoryNick, String username) {
        if (limit == 0) {
            limit = neubbsConfig.getTopicsApiRequestParamLimitDefault();
        }

        //get categoryId, userId
        int categoryId = categoryNick == null ? 0 : this.getTopicCategoryNotNullByNick(categoryNick).getId();
        int userId = username == null ? 0 : this.getUserNotNullByName(username).getId();

        int topicNumber = this.countTopicCount(categoryId, userId);
        if (topicNumber == 0) {
            throw new ServiceException(ApiMessage.NO_QUERY_TOPICS).log(LogWarnEnum.TS17);
        }

        //count totalPages by limit by topicNumber
        int totalPages =  topicNumber % limit == 0 ? (topicNumber / limit) : (topicNumber / limit + 1);
        return MapFilterUtil.generateMap(ParamConst.TOTAL_PAGES, totalPages);
    }

    @Override
    public int countTopicContentLike(int topicId) {
        return this.getTopicContentNotNull(topicId).getLike();
    }

    @Override
    public Map<String, Object> getTopicContentModelMap(int topicId) {
        TopicDO topic = this.getTopicNotNull(topicId);
        TopicContentDO topicContent = this.getTopicContentNotNull(topicId);
        TopicCategoryDO topicCategory = this.getTopicCategoryNotNullById(topic.getCategoryid());
        UserDO topicAuthorUser = this.getUserNotNullById(topic.getUserid());

        Map<String, Object> topicInfoMap = this.getTopicInfoMap(topic);
        Map<String, Object> topicContentInfoMap = this.getTopicContentInfoMap(topicContent);
        Map<String, Object> topicCategoryInfoMap = this.getTopicCategoryInfoMap(topicCategory);
        Map<String, Object> authorUserInfoMap = this.getTopicUserInfoMap(topicAuthorUser);
        Map<String, Object> lastReplyUserInfoMap
                = this.getTopicUserInfoMap(this.getUserNotNullById(topic.getLastreplyuserid()));


        //get topic reply list, and add to map
        List<TopicReplyDO> listReply = topicReplyDAO.listTopicReplyByTopicId(topicId);
        List<Map<String, Object>> listReplyInfoMap = new ArrayList<>(listReply.size());
        for (TopicReplyDO reply : listReply) {
            Map<String, Object> replyInfoMap = this.getTopicReplyInfoMap(reply);
            replyInfoMap.put(ParamConst.USER, this.getTopicUserInfoMap(userDAO.getUserById(reply.getUserid())));

            listReplyInfoMap.add(replyInfoMap);
        }

        //merge all information map
        topicInfoMap.putAll(topicContentInfoMap);
        topicInfoMap.put(ParamConst.CATEGORY, topicCategoryInfoMap);
        topicInfoMap.put(ParamConst.USER, authorUserInfoMap);
        topicInfoMap.put(ParamConst.LAST_REPLY_USER, lastReplyUserInfoMap);
        topicInfoMap.put(ParamConst.REPLY_LIST, listReplyInfoMap);

        return topicInfoMap;
    }

    @Override
    public Map<String, Object> getTopicReplyModelMap(int replyId) {
        TopicReplyDO reply = this.getTopicReplyNotNull(replyId);
        UserDO replyUser = this.getUserNotNullById(reply.getUserid());

        Map<String, Object> replyInfoMap = this.getTopicReplyInfoMap(reply);
            replyInfoMap.put(ParamConst.USER, this.getTopicUserInfoMap(replyUser));

        return replyInfoMap;
    }

    @Override
    public List<Map<String, Object>> listHotTopics() {
        List<TopicDO> topicList = topicDAO.listTopicOrderByCreatetimeDESCByRepliesDESCLimitTen();

        List<Map<String, Object>> hotTopicMapList = new ArrayList<>(topicList.size());
        for (TopicDO topic: topicList) {
            Map<String, Object> itemTopicMap = this.getTopicInfoMap(topic);
                itemTopicMap.put(ParamConst.CATEGORY,
                        this.getTopicCategoryInfoMap(this.getTopicCategory(topic.getCategoryid())));
                itemTopicMap.put(ParamConst.USER, this.getTopicUserInfoMap(this.getUser(topic.getUserid())));
                itemTopicMap.put(ParamConst.LAST_REPLY_USER,
                        this.getTopicUserInfoMap(this.getUser(topic.getLastreplyuserid())));

                //add to list
                hotTopicMapList.add(itemTopicMap);
        }

        return hotTopicMapList;
    }

    @Override
    public List<Map<String, Object>> listTopics(int limit, int page, String categoryNick, String username) {
        //if limit = 0, explain no input limit, use neubbs.properties default param
        if (limit == 0) {
            limit = neubbsConfig.getTopicsApiRequestParamLimitDefault();
        }

        this.confirmNoExceedTopicTotalNumber(limit, page);

        int categoryId = categoryNick == null ? 0 : this.getTopicCategoryNotNullByNick(categoryNick).getId();
        int userId = username == null ? 0 : this.getUserNotNullByName(username).getId();

        //get database query results
        List<TopicDO> dbQueryTopicList = this.getTopicList(limit, page, categoryId, userId);
        if (dbQueryTopicList.size() == 0) {
            throw new ServiceException(ApiMessage.NO_QUERY_TOPICS).log(LogWarnEnum.TS16);
        }

        List<Map<String, Object>> resultTopicList = new ArrayList<>(dbQueryTopicList.size());
        for (TopicDO topic : dbQueryTopicList) {
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
    public List<Map<String, Object>> listAllTopicCategories() {
        List<TopicCategoryDO> dbQueryCategoryList = topicCategoryDAO.listAllTopicCategory();

        List<Map<String, Object>> resultCategoryList = new ArrayList<>(dbQueryCategoryList.size());
        for (TopicCategoryDO category: dbQueryCategoryList) {
            resultCategoryList.add(this.getTopicCategoryInfoMap(category));
        }

        return resultCategoryList;
    }

    @Override
    public void alterTopicContent(int topicId, String newCategoryNick, String newTitle, String newTopicContent) {
        this.getTopicNotNull(topicId);
        this.getTopicContentNotNull(topicId);

        TopicCategoryDO category = this.getTopicCategoryNotNullByNick(newCategoryNick);

        //update forum_topic 'fucg_id', 'ft_title'
        if (topicDAO.updateCategoryById(topicId, category.getId()) == 0
                || topicDAO.updateTitleById(topicId, newTitle) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS7);
        }

        //update forum_topic_content 'ftc_content'
        if (topicContentDAO.updateContentByTopicId(topicId, newTopicContent) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS8);
        }
    }

    @Override
    public void alterReplyContent(int replyId, String newReplyContent) {
        this.getTopicReplyNotNull(replyId);

        //update forum_topic_reply 'ftr_content'
        if (topicReplyDAO.updateContentByIdByContent(replyId, newReplyContent) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS9);
        }
    }

    @Override
    public void increaseTopicRead(int topicId) {
        this.getTopicNotNull(topicId);

        if (topicContentDAO.updateReadAddOneByTopicId(topicId) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS7);
        }
    }

    @Override
    public Map<String, Object> alterTopicLikeByInstruction(boolean isCurrentUserLikeTopic, int topicId,
                                                           String command) {
        //judge current user whether repeat operation(no repeat input 'inc' or 'dec')
        boolean isIncOfInstruction = command.equals(SetConst.COLLECT_INC);
        if (isCurrentUserLikeTopic && isIncOfInstruction) {
            throw new ServiceException(ApiMessage.NO_REPEAT_INC_TOPIC_LIKE).log(LogWarnEnum.TS19);
        } else if (!isCurrentUserLikeTopic && !isIncOfInstruction) {
            throw new ServiceException(ApiMessage.NO_REPEAT_DEC_TOPIC_LIKE).log(LogWarnEnum.TS20);
        }

        //update forum_topic_content 'like'
        TopicContentDO topicContent = this.getTopicContentNotNull(topicId);
        int effectRow = isIncOfInstruction
                ? topicContentDAO.updateLikeAddOneByTopicId(topicId)
                : topicContentDAO.updateLikeCutOneByTopicId(topicId);
        if (effectRow == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS7);
        }

        int currentTopicLike = isIncOfInstruction ? topicContent.getLike() + 1 : topicContent.getLike() - 1;
        return MapFilterUtil.generateMap(ParamConst.LIKE, currentTopicLike);
    }

    @Override
    public void alterTopicCategoryDescription(String categoryNick, String newDescription) {
        this.getTopicCategoryNotNullByNick(categoryNick);

        if (topicCategoryDAO.updateDescriptionByNick(categoryNick, newDescription) == 0) {
            throw new ServiceException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.TS18);
        }
    }

    @Override
    public boolean isLikeTopic(int userId, int topicId) {
        this.confirmUserAndTopicNotNull(userId, topicId);

        return JsonUtil.isExistIntElement(this.getUserLikeTopicIdJsonArrayStringByUserId(userId), topicId);
    }

    @Override
    public boolean isCollectTopic(int userId, int topicId) {
        this.confirmUserAndTopicNotNull(userId, topicId);

        return JsonUtil.isExistIntElement(this.getUserCollectTopicIdJsonArrayStringByUserId(userId), topicId);
    }

    @Override
    public boolean isAttentionTopic(int userId, int topicId) {
        this.confirmUserAndTopicNotNull(userId, topicId);

        return JsonUtil.isExistIntElement(this.getUserAttentionTopicIdJsonArrayStringByUserId(userId), topicId);
    }

    @Override
    public List<Integer> operateLikeTopic(int userId, int topicId) {
        // isLikeTopic() already checked 'userId' and 'topicId'

        //true -> do 'dec', false -> do 'inc'
        if (this.isLikeTopic(userId, topicId)) {
            this.decUserLikeTopicToUpdateDatabase(userId, topicId);
        } else {
            this.incUserLikeTopicToUpdateDatabase(userId, topicId);
        }

        return JsonUtil.toListByJsonArrayString(this.getUserLikeTopicIdJsonArrayStringByUserId(userId));
    }

    @Override
    public Map<String, Object> operateCollectTopic(int userId, int topicId) {
        //isCollectTopic() already checked 'userId' and 'topicId'

        //true -> do 'dec' , false -> do 'inc'
        if (this.isCollectTopic(userId, topicId)) {
            this.decUserCollectTopicToUpdateDatabase(userId, topicId);
        } else {
            this.incUserCollectTopicToUpdateDatabase(userId, topicId);
        }

        return MapFilterUtil.generateMap(
                ParamConst.USER_COLLECT_TOPIC_ID,
                JsonUtil.toListByJsonArrayString(this.getUserCollectTopicIdJsonArrayStringByUserId(userId))
        );
    }

    @Override
    public Map<String, Object> operateAttentionTopic(int userId, int topicId) {
        // isAttentionTopic() already checked 'userId' and 'topicId'

        //true -> do 'dec', false -> do 'inc'
        if (this.isAttentionTopic(userId, topicId)) {
            this.decUserAttentionTopicToUpdateDatabase(userId, topicId);
        } else {
            this.incUseAttentionTopicToUpdateDatabase(userId, topicId);

        }

        return MapFilterUtil.generateMap(
                ParamConst.USER_ATTENTION_TOPIC_ID,
                JsonUtil.toListByJsonArrayString(this.getUserAttentionTopicIdJsonArrayStringByUserId(userId))
        );
    }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /*
     * ***********************************************
     * database operate method (update forum_user_action and forum_topic_action)
     *      - user like topic
     *      - user collect topic
     *      - user attention topic
     * ***********************************************
     */

    /**
     * inc 用户喜欢话题
     *
     * @param userId 用户id
     * @param topicId 话题id
     */
    private void incUserLikeTopicToUpdateDatabase(int userId, int topicId) {
        if (userActionDAO.updateLikeTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId) == 0) {
            this.throwUserOperateTopicFailException(SetConst.LIKE_INC);
        }

        if (topicActionDAO.updateLikeUserIdJsonArrayByOneUserIdToAppendEnd(topicId, userId) == 0) {
            this.throwTopicOperateFailException(SetConst.LIKE_USER_INC);
        }

        if (topicContentDAO.updateLikeAddOneByTopicId(topicId) == 0) {
            this.throwNoTopicContentException(topicId);
        }
    }

    /**
     * dec 用户喜欢话题
     *
     * @param userId 用户id
     * @param topicId 话题id
     */
    private void decUserLikeTopicToUpdateDatabase(int userId, int topicId) {
        String likeTopicJsonArrayString = this.getUserLikeTopicIdJsonArrayStringByUserId(userId);
        int topicIdIndex = JsonUtil.getIntElementIndex(likeTopicJsonArrayString, topicId);
        if (userActionDAO.updateLikeTopicIdJsonArrayByIndexToRemoveOneTopicId(userId, topicIdIndex) == 0) {
            this.throwUserOperateTopicFailException(SetConst.LIKE_DEC);
        }

        String likedUserJsonArrayString = this.getTopicLikeUserIdJsonArrayStringByTopicId(topicId);
        int userIdIndex = JsonUtil.getIntElementIndex(likedUserJsonArrayString, userId);
        if (topicActionDAO.updateLikeUserIdJsonArrayByIndexToRemoveOneUserId(topicId, userIdIndex) == 0) {
            this.throwTopicOperateFailException(SetConst.LIKE_USER_DEC);
        }

        //alter forum_topic_content like -1
        if (topicContentDAO.updateLikeCutOneByTopicId(topicId) == 0) {
            this.throwNoTopicContentException(topicId);
        }
    }

    /**
     * inc 用户收藏话题
     *
     * @param userId 用户id
     * @param topicId 话题id
     */
    private void incUserCollectTopicToUpdateDatabase(int userId, int topicId) {
        if (userActionDAO.updateCollectTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId) == 0) {
            throwUserOperateTopicFailException(SetConst.COLLECT_INC);
        }

        if (topicActionDAO.updateCollectUserIdJsonArrayByOneUserIdToAppendEnd(topicId, userId) == 0) {
            throwTopicOperateFailException(SetConst.COLLECT_USER_INC);
        }
    }

    /**
     * dec 用户收藏话题
     *
     * @param userId 用户id
     * @param topicId 话题id
     */
    private void decUserCollectTopicToUpdateDatabase(int userId, int topicId) {
        String userCollectJsonArrayString = this.getUserCollectTopicIdJsonArrayStringByUserId(userId);
        int topicIdIndex = JsonUtil.getIntElementIndex(userCollectJsonArrayString, topicId);
        if (userActionDAO.updateCollectTopicIdJsonArrayByIndexToRemoveOneTopicId(userId, topicIdIndex) == 0) {
            this.throwUserOperateTopicFailException(SetConst.COLLECT_DEC);
        }

        String topicCollectJsonArrayString = this.getTopicCollectUserIdJsonArrayStringByTopicId(topicId);
        int userIdIndex = JsonUtil.getIntElementIndex(topicCollectJsonArrayString, userId);
        if (topicActionDAO.updateCollectUserIdJsonArrayByIndexToRemoveOneUserId(topicId, userIdIndex) == 0) {
            this.throwTopicOperateFailException(SetConst.COLLECT_USER_DEC);
        }
    }

    /**
     * inc 用户关注话题
     *
     * @param userId 用户id
     * @param topicId 话题id
     */
    private void incUseAttentionTopicToUpdateDatabase(int userId, int topicId) {
        if (userActionDAO.updateAttentionTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId) == 0) {
            this.throwUserOperateTopicFailException(SetConst.ATTENTION_INC);
        }

        if (topicActionDAO.updateAttentionUserIdJsonArrayByOneUserIdToAppendEnd(topicId, userId) == 0) {
            this.throwTopicOperateFailException(SetConst.ATTENTION_USER_INC);
        }
    }

    /**
     * dec 用户关注话题
     *
     * @param userId 用户id
     * @param topicId 话题id
     */
    private void decUserAttentionTopicToUpdateDatabase(int userId, int topicId) {
        String attentionTopicJsonArrayString = this.getUserAttentionTopicIdJsonArrayStringByUserId(userId);
        int topicIdIndex = JsonUtil.getIntElementIndex(attentionTopicJsonArrayString, topicId);
        if (userActionDAO.updateAttentionTopicIdJsonArrayByIndexToRemoveOneTopicId(userId, topicIdIndex) == 0) {
            this.throwUserOperateTopicFailException(SetConst.ATTENTION_DEC);
        }

        String attentionUserJsonArrayString = this.getTopicAttentionUserIdJsonArrayStringByTopicId(topicId);
        int userIdIndex = JsonUtil.getIntElementIndex(attentionUserJsonArrayString, userId);
        if (topicActionDAO.updateAttentionUserIdJsonArrayByIndexToRemoveOneUserId(topicId, userIdIndex) == 0) {
            this.throwTopicOperateFailException(SetConst.ATTENTION_USER_DEC);
        }
    }


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
    private void confirmNoExceedTopicTotalNumber(int limit, int page) {
        int allTopicTotals = topicDAO.countTopic();
        int maxPage = allTopicTotals % limit == 0
                ? allTopicTotals / limit : allTopicTotals / limit + 1;

        if (limit > allTopicTotals || page > maxPage) {
            throw new ServiceException(ApiMessage.QUERY_EXCEED_TOPIC_NUMBER).log(LogWarnEnum.TS12);
             //"（话题总数 = allTopicTotals，若 limit = limit，最多跳转至 maxPage 页）")
        }
    }

    /**
     * 确认用户和话题不能未空
     *
     * @param userId 用户id
     * @param topicId 话题id
     */
    private void confirmUserAndTopicNotNull(int userId, int topicId) {
        this.getUserNotNullById(userId);
        this.getTopicNotNull(topicId);
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
     * 获取话题分类对象不能为空（通过 nick）
     *      - 对应 forum_topic_category - 'ftcg_nick'
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
            throw new ServiceException(ApiMessage.NO_REPLY).log(LogWarnEnum.TS11);
        }

        return reply;
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

    /**
     * 获取用户喜欢话题 id JSON 数组字符串
     *
     * @param userId 用户id
     * @return String 用户喜欢话题idJSON数组字符串
     */
    private String getUserLikeTopicIdJsonArrayStringByUserId(int userId) {
        return userActionDAO.getUserActionLikeTopicIdJsonArray(userId);
    }

    /**
     * 获取用户收藏话题 id JSON 数组字符串
     *
     * @param userId 用户id
     * @return String 用户收藏话题idJSON数组字符串
     */
    private String getUserCollectTopicIdJsonArrayStringByUserId(int userId) {
        return userActionDAO.getUserActionCollectTopicIdJsonArray(userId);
    }

    /**
     * 获取用户关注话题 id JSON 数组字符串
     *
     * @param userId 用户id
     * @return String 用户关注话题idJSON数组字符串
     */
    private String getUserAttentionTopicIdJsonArrayStringByUserId(int userId) {
        return userActionDAO.getUserActionAttentionTopicIdJsonArray(userId);
    }

    /**
     * 获取话题被喜欢用户 id JSON 数组字符串
     *
     * @param topicId 话题id
     * @return String 话题被喜欢用户idJSON数组字符串
     */
    private String getTopicLikeUserIdJsonArrayStringByTopicId(int topicId) {
        return topicActionDAO.getTopicActionLikeUserIdJsonArray(topicId).getLikeUserIdJsonArray();
    }

    /**
     * 获取话题被收藏用户 id JSON 数组字符串
     *
     * @param topicId 话题id
     * @return String 话题被收藏用户idJSON数组字符串
     */
    private String getTopicCollectUserIdJsonArrayStringByTopicId(int topicId) {
        return topicActionDAO.getTopicActionCollectUserIdJsonArray(topicId).getCollectUserIdJsonArray();
    }

    /**
     * 获取话题被关注用户 id JSON 数组字符串
     *
     * @param topicId 话题id
     * @return String 话题被关注用户idJSON数组字符串
     */
    private String getTopicAttentionUserIdJsonArrayStringByTopicId(int topicId) {
        return topicActionDAO.getTopicActionAttentionUserIdJsonArray(topicId).getAttentionUserIdJsonArray();
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
            return new HashMap<>(0);
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
            return new HashMap<>(0);
        }

        Map<String, Object> topicContentInfoMap = JsonUtil.toMapByObject(topicContent);
        MapFilterUtil.filterTopicContentInfo(topicContentInfoMap);

        return topicContentInfoMap;
    }

    /**
     * 获取话题分类信息 Map
     *
     * @param category 话题分类对象
     * @return Map 已处理的话题分类信息
     */
    private Map<String, Object> getTopicCategoryInfoMap(TopicCategoryDO category) {
        if (category == null) {
            return new HashMap<>(0);
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
            return new HashMap<>(0);
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
        throw new ServiceException(ApiMessage.NO_TOPIC).log(LogWarnEnum.TS10);
    }

    /**
     * 抛出不存在话题内容异常
     *
     * @param topicId 话题id
     */
    private void throwNoTopicContentException(int topicId) {
        //"topic content topicId=" + topicId +
        throw new ServiceException(ApiMessage.NO_TOPIC).log(LogWarnEnum.TS10);
    }

    /**
     * (id)抛出不存在用户异常
     *
     * @param userId 用户id
     */
    private void throwNoUserExceptionById(int userId) {
        throw new ServiceException(ApiMessage.NO_USER).log(LogWarnEnum.US18);
    }

    /**
     * （username）抛出不存在用户异常
     *
     * @param username 用户名
     */
    private void throwNoUserExceptionByName(String username) {
        throw new ServiceException(ApiMessage.NO_USER).log(LogWarnEnum.US18);
    }

    /**
     * （id）抛出不存话题分类异常
     */
    private void throwNotCategoryExceptionById(int categoryId) {
        throw new ServiceException(ApiMessage.NO_CATEGORY).log(LogWarnEnum.TS13);
    }

    /**
     * （nick）抛出不存话题分类异常
     */
    private void throwNotCategoryExceptionByNick(String nick) {
        throw new ServiceException(ApiMessage.NO_CATEGORY).log(LogWarnEnum.TS13);
    }

    /**
     * 抛出用户操作话题失败异常
     *
     * @param userOperate 用户操作
     */
    private void throwUserOperateTopicFailException(String userOperate) {
        throw new ServiceException(ApiMessage.USER_OPERATE_TOPIC_FAIL).log(LogWarnEnum.TS22);
    }

    /**
     * 抛出话题操作失败异常
     *
     * @param topicOperate 话题操作
     */
    private void throwTopicOperateFailException(String topicOperate) {
        throw new ServiceException(ApiMessage.TOPIC_RECORD_OPERATE_FAIL).log(LogWarnEnum.TS23);
    }
}
