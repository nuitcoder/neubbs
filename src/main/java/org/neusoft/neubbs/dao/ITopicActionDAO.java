package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicActionDO;
import org.springframework.stereotype.Repository;

/**
 * 话题行为数据访问接口
 *      - 针对  forum_topic_action 表
 *      - resources/mapping/TopicActionMapper.xml 配置 SQL
 *
 * @author Suvan
 */
@Repository
public interface ITopicActionDAO {

    /**
     * 保存话题行为
     *      - 初始化插入数据（用户新建话题时候）
     *      - TopicActionDO 对象仅需注入 topicId
     *
     * @param topicAction　用户行为对象
     * @return int 插入行数
     */
    int saveTopicAction(TopicActionDO topicAction);

    /**
     * 删除话题行为
     *
     * @param topicId 话题 id
     * @return int 删除行数
     */
    int removeTopicAction(int topicId);

    /**
     * 获取所有话题行为
     *      - 回复用户 id 数组
     *      - 喜欢用户 id 数组
     *      - 收藏用户 id 数组
     *      - 关注用户 id 数组
     *
     * @param topicId 话题 id
     * @return TopicActionDO 话题行为对象
     */
    TopicActionDO getTopicAction(int topicId);

    /**
     * 获取话题行为回复用户 id 数组
     *      - 注入 TopicActionDO 对象 replyUserIdJsonArray 属性
     *
     * @param  topicId 话题 id
     * @return String 回复话题的用户 id，JSON 数组字符串
     */
    String getTopicActionReplyUserIdJsonArray(int topicId);

    /**
     * 获取话题行为喜欢用户 id 数组
     *      - 注入 TopicActionDO 对象 likeUserIdJsonArray 属性
     *
     * @param topicId 话题 id
     * @return String 点赞话题的用户 id，JSON 数组字符串
     */
    String getTopicActionLikeUserIdJsonArray(int topicId);

    /**
     * 获取话题行为收藏用户 id 数组
     *      - 注入 TopicActionDO 对象 collectUserIdJsonArray 属性
     *
     * @param topicId 话题 id
     * @return String 收藏话题的用户 id，JSON 数组字符串
     */
    String getTopicActionCollectUserIdJsonArray(int topicId);

    /**
     * 获取话题行为关注用户 id 数组
     *      - 注入 TopicActionDO 对象 attentionUserIdJsonArray 属性
     *
     * @param topicId 话题 id
     * @return String 关注话题的用户 id，JSON 数组字符串
     */
    String getTopicActionAttentionUserIdJsonArray(int topicId);

    /**
     * 更新话题回复用户 id 数组，JSON 数组末尾追加 1 个用户 id
     *
     * @param topicId 话题 id
     * @param replyUserId 回复用户 id
     * @return int 更新行数
     */
    int updateReplyUserIdJsonArrayByOneUserIdToAppendEnd(int topicId, int replyUserId);

    /**
     * 更新话题喜欢用户 id 数组，JSON 数组末尾追加 1 个用户 id
     *
     * @param topicId 话题 id
     * @param likeUserId 喜欢用户 id
     * @return int 更新行数
     */
    int updateLikeUserIdJsonArrayByOneUserIdToAppendEnd(int topicId, int likeUserId);

    /**
     * 更新话题收藏用户 id 数组，JSON 数组末尾追加 1 个用户 id
     *
     * @param topicId 话题 id
     * @param collectUserId 收藏用户 id
     * @return int 更新行数
     */
    int updateCollectUserIdJsonArrayByOneUserIdToAppendEnd(int topicId, int collectUserId);

    /**
     * 更新话题关注用户 id 数组，JSON 数组末尾追加 1 个用户 id
     *
     * @param topicId 话题 id
     * @param attentionUserId 关注用户 id
     * @return int 更新行数
     */
    int updateAttentionUserIdJsonArrayByOneUserIdToAppendEnd(int topicId, int attentionUserId);

    /**
     * 更新话题回复用户 id 数组，JSON 数组，指定索引，删除元素
     *
     * @param topicId 话题 id
     * @param indexOfReplyUserId 即将删除回复用户 id 的索引
     * @return int 更新行数
     */
    int updateReplyUserIdJsonArrayByIndexToRemoveOneUserId(int topicId, int indexOfReplyUserId);

    /**
     * 更新话题喜欢用户 id 数组，JSON 数组，指定索引，删除元素
     *
     * @param topicId 话题 id
     * @param indexOfLikeUserId 即将删除喜欢用户 id 的索引
     * @return int 更新行数
     */
    int updateLikeUserIdJsonArrayByIndexToRemoveOneUserId(int topicId, int indexOfLikeUserId);

    /**
     * 更新话题收藏用户 id 数组，JSON 数组，指定索引，删除元素
     *
     * @param topicId 话题 id
     * @param indexOfCollectUserId 即将删除收藏用户 id 的索引
     * @return int 更新行数
     */
    int updateCollectUserIdJsonArrayByIndexToRemoveOneUserId(int topicId, int indexOfCollectUserId);

    /**
     * 更新话题关注用户 id 数组，JSON 数组，指定索引，删除元素
     *
     * @param topicId 话题 id
     * @param indexOfAttentionUserId 即将删除关注用户 id 的索引
     * @return int 更新行数
     */
    int updateAttentionUserIdJsonArrayByIndexToRemoveOneUserId(int topicId, int indexOfAttentionUserId);
}
