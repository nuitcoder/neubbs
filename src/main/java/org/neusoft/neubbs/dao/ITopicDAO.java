package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicDO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * forum_topic 数据访问对象（TopicMapper.xml 映射接口）
 *
 * @author Suvan
 */
@Repository
public interface ITopicDAO {
    /**
     * 保存话题
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
     * 获取话题最大id（表中最新插入的id）
     *
     * @return int 最新话题id
     */
    int getTopicMaxId();

    /**
     * 获取话题对象
     *
     * @param topicId 话题id
     * @return TopicDO 话题对象
     */
    TopicDO getTopicById(int topicId);

    /**
     * 获取话题分类列表（所有类别，不重复）
     *
     * @return List<String> 话题分类列表
     */
    List<String> listTopicCategory();

    /**
     * 获取最新的话题列表（降序，指定数量）
     *
     * @param count 数量
     * @return list<TopicDO>
     */
    List<TopicDO> listTopicDESCByCount(int count);

    /**
     * 获取话题列表（指定开始行数，数量，用于分页查询）
     *
     * @param startRow 开始行数
     * @param count 话题数
     * @return List<TopicDO> 话题列表
     */
    List<TopicDO> listTopicByStartRowByCount(int startRow, int count);

    /**
     * 获取话题列表（分类获取）
     *
     * @param startRow 开始行数
     * @param count 获取数
     * @param category 分类信息
     * @return List<TopicDO> 话题列表
     */
    List<TopicDO> listTopicByStartRowByCountByCategory(int startRow, int count, String category);

    /**
     * 获取话题列表（用户名获取）
     *
     * @param startRow 开始行数
     * @param count 获取数量
     * @param userId 用户id
     * @return List<TopicDO> 话题列表
     */
    List<TopicDO> listTopicByStartRowByCountByUsername(int startRow, int count, int userId);

    /**
     * 更新话题分类
     *
     * @param topicId 话题id
     * @param category 新话题类别
     * @return int 更新行数
     */
    int updateCategoryById(int topicId, String category);

    /**
     * 更新话题名
     *
     * @param topicId 话题内容
     * @param title 新话题名
     * @return int 更新行数
     */
    int updateTitleById(int topicId, String title);

    /**
     * 更新评论回复数（自动 +1）
     *
     * @param topicId 话题id
     * @return int 更新行数
     */
    int updateCommentAddOneById(int topicId);

    /**
     * 更新评论回复数（自动 -1）
     * @param topicId 话题id
     * @return int 更新行数
     */
    int updateCommentCutOneById(int topicId);

    /**
     * 更新最后回复人id
     *
     * @param topicId 话题
     * @param lastreplyuserid 最后回复人id
     * @return int 更新行数
     */
    int updateLastreplyuseridById(int topicId, int lastreplyuserid);

    /**
     * 更新最后回复时间
     *
     * @param topicId 话题id
     * @param lastreplytime 最后回复数
     * @return int 更新行数
     */
    int updateLastreplytimeById(int topicId, Date lastreplytime);

}
