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
     * @return Integer 插入行数
     */
    Integer saveTopic(TopicDO topic);

    /**
     * 删除话题
     *
     * @param id 话题id
     * @return Integer 删除行数
     */
    Integer removeTopicById(int id);

    /**
     * 统计话题总数
     *
     * @return Integer 话题总数
     */
    Integer countTopic();

    /**
     * 获取话题最大id（表中最新插入的id）
     *
     * @return Integer 最新话题id
     */
    Integer getTopicMaxId();

    /**
     * 获取话题对象
     *
     * @param id 话题id
     * @return TopicDO 话题对象
     */
    TopicDO getTopicById(int id);

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
     * 更新话题分类
     *
     * @param id 话题id
     * @param category 新话题类别
     * @return Integer 更新行数
     */
    Integer updateCategoryById(int id, String category);

    /**
     * 更新话题名
     *
     * @param id 话题内容
     * @param title 新话题名
     * @return Integer 更新行数
     */
    Integer updateTitleById(int id, String title);

    /**
     * 更新评论回复数（自动 +1）
     *
     * @param id 话题id
     * @return Integer 更新行数
     */
    Integer updateCommentAddOneById(int id);

    /**
     * 更新评论回复数（自动 -1）
     * @param id 话题id
     * @return Integer 更新行数
     */
    Integer updateCommentCutOneById(int id);

    /**
     * 更新最后回复人id
     *
     * @param id 最后回复人id
     * @return Integer 更新行数
     */
    Integer updateLastreplyuseridById(int id, int lastreplyuserid);

    /**
     * 更新最后回复时间
     *
     * @param id 话题id
     * @param lastreplytime 最后回复数
     * @return Integer 更新行数
     */
    Integer updateLastreplytimeById(int id, Date lastreplytime);

}
