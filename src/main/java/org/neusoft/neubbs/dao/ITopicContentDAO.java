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
     * @return int 插入行数
     */
    int saveTopicContent(TopicContentDO topicContent);

    /**
     * 删除话题
     *
     * @param topicId 话题id
     * @return int 删除行数
     */
    int removeTopicContentById(int topicId);

    /**
     * 获取话题总数
     *
     * @return int 话题总数
     */
    int getTopicContentMaxId();

    /**
     * 获取话题对象
     *
     * @param topicId 话题id
     * @return TopicContentDO 话题对象
     */
    TopicContentDO getTopicContentById(int topicId);

    /**
     * 更新话题内容
     *
     * @param topicId 话题id
     * @param content 新话题内容
     * @return int 更新行数
     */
    int updateContentByTopicId(int topicId, String content);

    /**
     * 更新话题阅读数（+1）
     *
     * @param topicId 话题id
     * @return int 更新行数
     */
    int updateReadAddOneByTopicId(int topicId);

    /**
     * 更新话题内容赞同数，+1
     *
     * @param topicId 话题id
     * @return int 更新行数
     */
    int updateAgreeAddOneByTopicId(int topicId);

    /**
     * 更新话题内容赞同数，-1
     *
     * @param topicId 话题id
     * @return int 更新行数
     */
    int updateAgreeCutOneByTopicId(int topicId);
}
