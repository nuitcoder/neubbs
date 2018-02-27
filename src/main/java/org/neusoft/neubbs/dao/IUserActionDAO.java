package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.UserActionDO;
import org.springframework.stereotype.Repository;

/**
 * 用户行为数据访问接口
 *      - 针对 forum_user_action 表
 *      - resources/mapping/UserActionMapper.xml 配置 SQL
 *
 * @author Suvan
 */
@Repository
public interface IUserActionDAO {

    /**
     * 保存用户行为
     *      - 初始化插入数据（用于注册用户时）
     *      - UserActionDO 对象仅需输入 userId
     *
     * @param userAction 用户行为对象
     * @return int 插入行数
     */
    int saveUserAction(UserActionDO userAction);

    /**
     * 获取用户所有行为
     *      - 根据用户 id 查询（每个用户仅有一条行为记录）
     *      - 获取（JSON 格式）
     *          - 喜欢话题 id 数组
     *          - 收藏话题 id 数组
     *          - 关注话题 id 数组
     *          - 关注用户 id 数组
     *          - 被关注用户 id 数组
     *
     * @param userId 用户 id
     * @return UserActionDO 用户行为对象
     */
    UserActionDO getUserAction(int userId);

    /**
     * 获取用户喜欢话题 id 数组
     *      - 'fua_like_ft_id_array' 字段
     *
     * @param userId 用户 id
     * @return String 用户喜欢话题 id，JSON 数组字符串
     */
    String getUserActionLikeTopicIdJsonArray(int userId);

    /**
     * 获取用户收藏话题 id 数组
     *      - 'fua_collect_ft_id_array' 字段
     *
     * @param userId 用户 id
     * @return String 用户收藏话题 id，JSON 数组字符串
     */
    String getUserActionCollectTopicIdJsonArray(int userId);

    /**
     * 获取用户关注话题 id 数组
     *      - 'fua_attention_ft_id_array' 字段
     *
     * @param userId 用户 id
     * @return String 用户关注话题 id，JSON 数组字符串
     */
    String getUserActionAttentionTopicIdJsonArray(int userId);

    /**
     * 获取用户主动关注用户 id 数组
     *      - 'fua_following_fu_id_array' 字段
     *
     * @param userId 用户 id
     * @return String 主动关注用户 id，JSON 数组字符串
     */
    String getUserActionFollowingUserIdJsonArray(int userId);

    /**
     * 获取用户被关注用户 id 数组
     *      - 'fua_followed_fu_id_array' 字段
     *
     * @param userId 用户 id
     * @return String 被关注用户 id，JSON 数组字符串
     */
    String getUserActionFollowedUserIdJsonArray(int userId);

    /**
     * 更新用户喜欢话题 id 数组，JSON 数组末尾追加 1 个话题 id
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @return int 更新行数
     */
    int updateLikeTopicIdJsonArrayByOneTopicIdToAppendEnd(int userId, int topicId);

    /**
     * 更新用户收藏话题 id 数组，JSON 数组末尾追加 1 个话题 id
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @return int 更新行数
     */
    int updateCollectTopicIdJsonArrayByOneTopicIdToAppendEnd(int userId, int topicId);

    /**
     * 更新用户关注话题 id 数组，JSON 数组末尾追加 1 个话题 id
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @return int 更新行数
     */
    int updateAttentionTopicIdJsonArrayByOneTopicIdToAppendEnd(int userId, int topicId);

    /**
     * 更新用户主动关注用户 id 数组，JSON 数组末尾追加 1 个主动关注用户 id
     *
     * @param userId 用户 id
     * @param followingUserId 主动关注用户 id
     * @return int 更新行数
     */
    int updateFollowingUserIdJsonArrayByOneUserIdToAppendEnd(int userId, int followingUserId);

    /**
     * 更新用户被关注用户 id 数组，JSON 数组末尾追加 1 个被关注用户 id
     *
     * @param userId 用户 id
     * @param followedUserId 被关注用户 id
     * @return int 更新行数
     */
    int updateFollowedUserIdJsonArrayByOneUserIdToAppendEnd(int userId, int followedUserId);

    /**
     * 更新用户喜欢话题 id 数组，JSON 数组 根据索引，删除话题 id
     *      - 索引下标从 0 开始
     *
     * @param userId 用户 id
     * @param indexOfTopicId 话题 id 位于 JSON 数组内的索引位置
     * @return int 更新行数
     */
    int updateLikeTopicIdJsonArrayByIndexToRemoveOneTopicId(int userId, int indexOfTopicId);

    /**
     * 更新用户收藏话题 id 数组，JSON 数组 根据索引，删除话题 id
     *
     * @param userId 用户 id
     * @param indexOfTopicId 话题 id 位于 JSON 数组内的索引位置
     * @return int 更新行数
     */
    int updateCollectTopicIdJsonArrayByIndexToRemoveOneTopicId(int userId, int indexOfTopicId);

    /**
     * 更新用户关注话题 id 数组，JSON 数组 根据索引，删除话题 id
     *
     * @param userId 用户 id
     * @param indexOfTopicId 话题 id 位于 JSON 数组内的索引位置
     * @return int 更新行数
     */
    int updateAttentionTopicIdJsonArrayByIndexToRemoveOneTopicId(int userId, int indexOfTopicId);

    /**
     * 更新用户主动关注用户 id 数组，JSON 数组 根据索引，删除用户 id
     *
     * @param userId 用户 id
     * @param indexOfFollowingUserId 用户 id 位于 JSON 数组内的索引位置
     * @return int 更新行数
     */
    int updateFollowingUserIdJsonArrayByIndexToRemoveOneUserId(int userId, int indexOfFollowingUserId);

    /**
     * 更新用户被关注用户 id 数组，JSON 数组 根据索引，删除用户 id
     *
     * @param userId 用户 id
     * @param indexOfFollowedUserId 用户 id 位于 JSON 数组内的索引位置
     * @return int 更新行数
     */
    int updateFollowedUserIdJsonArrayByIndexToRemoveOneUserId(int userId, int indexOfFollowedUserId);
}
