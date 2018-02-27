package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicCategoryDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 话题分类数据访问接口
 *      - 针对 forum_topic_category
 *      - resources/mapping/TopicCategoryMapper.xml 配置 SQL
 *
 * @author Suvan
 */
@Repository
public interface ITopicCategoryDAO {

    /**
     * 保存话题分类
     *
     * @param category 话题分类对象
     * @return int 插入行数
     */
    int saveTopicCategory(TopicCategoryDO category);

    /**
     * 删除话题分类
     *
     * @param categoryId 分类 id
     * @return int 删除行数
     */
    int removeTopicCategoryById(int categoryId);

    /**
     * （id）获取话题分类
     *
     * @param categoryId 分类 id
     * @return TopicCategoryDO 话题分类对象
     */
    TopicCategoryDO getTopicCategoryById(int categoryId);

    /**
     * （昵称）获取话题话题分类
     *
     * @param categoryNick 分类昵称（英文）
     * @return TopicCategoryDO 话题分类对象
     */
    TopicCategoryDO getTopicCategoryByNick(String categoryNick);

    /**
     * (name) 获取话题分类
     *
     * @param categoryName 分类名称（中文）
     * @return TopicCategoryDO 话题分类对象
     */
    TopicCategoryDO getTopicCategoryByName(String categoryName);

    /**
     * 获取所有话题分类
     *
     * @return List 话题分类列表
     */
    List<TopicCategoryDO> listAllTopicCategory();

    /**
     * 更新话题分类描述
     *
     * @param categoryNick 话题昵称（英文）
     * @param newDescription 新的描述内容
     * @return int 影响行数
     */
    int updateDescriptionByNick(String categoryNick, String newDescription);
}
