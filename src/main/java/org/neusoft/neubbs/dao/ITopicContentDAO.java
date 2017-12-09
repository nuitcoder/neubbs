package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.TopicContentDO;
import org.springframework.stereotype.Repository;

/**
 * 话题内容数据访问接口
 *      - 针对 forum_topic_content
 *      - resources/mapping/TopicContentMapper.xml 配置 SQL
 *
 * @author Suvan
 */
@Repository
public interface ITopicContentDAO {

    /**
     * 保存话题
     *      - need topic id from forum_topic
     *      - need topic content
     *
     * @param topicContent 话题对象
     * @return int 插入行数
     */
    int saveTopicContent(TopicContentDO topicContent);

    /**
     * 删除话题
     *
     * @param topicId 话题id（ft_id）
     * @return int 删除行数
     */
    int removeTopicContentByTopicId(int topicId);

    /**
     * （话题 id）获取话题对象
     *
     * @param topicId 话题id（ft_id）
     * @return TopicContentDO 话题对象
     */
    TopicContentDO getTopicContentByTopicId(int topicId);

    /**
     * 更新话题内容
     *
     * @param topicId 话题id（ft_id）
     * @param content 新话题内容
     * @return int 更新行数
     */
    int updateContentByTopicId(int topicId, String content);

    /**
     * 更新话题阅读数（自动 +1）
     *
     * @param topicId 话题id（ft_id）
     * @return int 更新行数
     */
    int updateReadAddOneByTopicId(int topicId);

    /**
     * 更新话题内容喜欢人数（自动 +1）
     *
     * @param topicId 话题id（ft_id）
     * @return int 更新行数
     */
    int updateLikeAddOneByTopicId(int topicId);

    /**
     * 更新话题内容喜欢人数（自动 -1）
     *
     * @param topicId 话题id（ft_id）
     * @return int 更新行数
     */
    int updateLikeCutOneByTopicId(int topicId);
}
