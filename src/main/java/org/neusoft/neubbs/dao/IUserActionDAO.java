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
     *       - SQL 语句里 JSON 默认值 是 []
     *
     * @param userAction 用户行为对象
     * @return int 插入行数
     */
    int saveUserAction(UserActionDO userAction);

    /**
     * 获取用户行为对象
     *      - 根据用户 id 查询（每个用户仅有一条行为记录）
     *
     * @param userId 用户id
     * @return UserActionDO 用户行为对象
     */
    UserActionDO getUserAction(int userId);

    /**
     * 更新用户喜欢话题 id 数组
     *      - 用户话题点赞按钮
     *      - JSON 格式的 String 储存进数据库
     *
     * @param userId 用户id
     * @param userLikeTopicIdArrayJson 用户喜欢话题id数组JSON
     * @return int 更新行数
     */
    int updateLikeTopicIdJsonArray(int userId, String userLikeTopicIdArrayJson);

    /**
     * 更新用户喜欢 id 话题数组，Json 数组 末尾 追加一个 topicId
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @return int 更新行数
     */
    int updateLikeTopicIdJsonArrayByOneTopicIdToAppendEnd(int userId, int topicId);

    /**
     * 更新用户喜欢 id 话题数组，根据索引，删除元素
     *
     * @param userId 用户id
     * @param indexOfTopicId 话题id位于JSON数组内的索引位置
     * @return int 更新行数
     */
    int updateLikeTopicIdJsonArrayRemoveOneTopicIdByIndex(int userId, int indexOfTopicId);
}
