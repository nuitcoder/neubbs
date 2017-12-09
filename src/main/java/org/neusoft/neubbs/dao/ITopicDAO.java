package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicDO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 话题基本信息数据访问接口
 *      - 针对 forum_topic
 *      - resources/mapping/TopicMapper.xml 配置 SQL
 *
 * @author Suvan
 */
@Repository
public interface ITopicDAO {

    /**
     * 保存话题
     *      - userid, categoryid, title
     *      - TopicDO 对象的 id 属性会注入新生成的自增 id
     *
     * @param topic 话题对象
     * @return int 插入行数
     */
    int saveTopic(TopicDO topic);

    /**
     * 删除话题
     *
     * @param topicId 话题id
     * @return int 删除行数
     */
    int removeTopicById(int topicId);

    /**
     * 统计话题总数
     *
     * @return int 话题总数
     */
    int countTopic();

    /**
     * （分类）统计话题总数
     *
     * @param categoryId 话题分类id
     * @return int 话题总数
     */
    int countTopicByCategoryId(int categoryId);

    /**
     * （用户 id）统计话题总数
     *
     * @param userId 用户id
     * @return int 话题总数
     */
    int countTopicByUserId(int userId);


    /**
     * （话题分类 id，用户 id）统计话题总数
     *
     * @param categoryId 话题分类id
     * @param userId 用户id
     * @return int 话题总数
     */
    int countTopicByCategoryIdByUserId(int categoryId, int userId);

    /**
     * 获取最大话题 id
     *      - 最新发布话题 id
     *
     * @return int 最新话题id
     */
    int getMaxTopicId();

    /**
     * 获取话题对象
     *
     * @param topicId 话题id
     * @return TopicDO 话题对象
     */
    TopicDO getTopicById(int topicId);


    /**
     * （降序，仅输入 count）获取话题列表
     *      - 最新发布
     *
     * @param count 指定显示数量
     * @return List 话题集合
     */
    List<TopicDO> listTopicDESCByCount(int count);

    /**
     * （降序）获取话题列表
     *      - 最新发布
     *
     * @param startRow 开始行数
     * @param count 指定显示数量
     * @return List 话题列表
     */
    List<TopicDO> listTopicDESCByStartRowByCount(int startRow, int count);

    /**
     * （降序，分类）获取话题列表
     *      - 最新发布
     *
     * @param startRow 开始行数
     * @param count 指定显示数量
     * @param categoryId 话题分类id
     * @return List 话题列表
     */
    List<TopicDO> listTopicDESCByStartRowByCountByCategoryId(int startRow, int count, int categoryId);

    /**
     * （降序，用户 id 分类）获取话题列表
     *      - 最新发布
     *
     * @param startRow 开始行数
     * @param count 指定显示数量
     * @param userId 用户id
     * @return List 话题列表
     */
    List<TopicDO> listTopicDESCByStartRowByCountByUserId(int startRow, int count, int userId);

    /**
     * （降序，话题分类 id，用户 id）获取话题列表
     *      - 最新发布
     *
     * @param startRow 开始行数
     * @param count 指定显示数量
     * @param categoryId 话题分类id
     * @param userId 用户id
     * @return List 话题列表
     */
    List<TopicDO> listTopicDESCByStartRowByCountByCategoryIdByUserId(int startRow, int count,
                                                                     int categoryId, int userId);
    /**
     * 更新话题分类
     *
     * @param topicId 话题id
     * @param newCategoryId 新话题分类id
     * @return int 更新行数
     */
    int updateCategoryById(int topicId, int newCategoryId);

    /**
     * 更新话题名
     *
     * @param topicId 话题内容
     * @param newTitle 新话题名
     * @return int 更新行数
     */
    int updateTitleById(int topicId, String newTitle);

    /**
     * 更新评论回复数（自动 +1）
     *
     * @param topicId 话题id
     * @return int 更新行数
     */
    int updateRepliesAddOneById(int topicId);

    /**
     * 更新评论回复数（自动 -1）
     *
     * @param topicId 话题id
     * @return int 更新行数
     */
    int updateRepliesCutOneById(int topicId);

    /**
     * 更新最后回复人id
     *
     * @param topicId 话题
     * @param lastReplyUserId 最后回复人id
     * @return int 更新行数
     */
    int updateLastReplyUserIdById(int topicId, int lastReplyUserId);

    /**
     * 更新最后回复时间
     *
     * @param topicId 话题id
     * @param lastReplyTime 最后回复时间
     * @return int 更新行数
     */
    int updateLastReplyTimeById(int topicId, Date lastReplyTime);

}
