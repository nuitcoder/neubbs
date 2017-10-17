package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicContentDO;
import org.springframework.stereotype.Repository;

/**
 * forum_topic_content 数据访问对象（TopicContentMapper.xml 映射接口）
 *
 * @author Suvan
 */
@Repository
public interface ITopicContentDAO {
    /**
     * 保存话题
     *
     * @param topicContent 话题对象
     * @return Integer 插入行数
     */
    Integer saveTopicContent(TopicContentDO topicContent);

    /**
     * 删除话题
     *
     * @param id 话题id
     * @return Integer 删除行数
     */
    Integer removeTopicContentById(int id);

    /**
     * 获取话题总数
     *
     * @return Integer 话题总数
     */
    Integer getTopicContentMaxId();

    /**
     * 获取话题对象
     *
     * @param id 话题id
     * @return TopicContentDO 话题对象
     */
    TopicContentDO getTopicContentById(int id);

    /**
     * 更新话题内容
     *
     * @param id    话题id
     * @param content 新话题内容
     * @return Integer 更新行数
     */
    Integer updateContentById(int id, String content);

    /**
     * 更新话题阅读数
     *
     * @param id 话题id
     * @return Integer 更新行数
     */
    Integer updateReadById(int id);
}
