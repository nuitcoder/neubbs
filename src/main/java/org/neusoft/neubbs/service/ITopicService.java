package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;

import java.util.List;

/**
 * 话题业务接口
 *
 * @author Suvan
 */
public interface ITopicService {

    /**
     * 保存话题
     *
     * @param userId 用户id
     * @param category 话题分类
     * @param title 话题标题
     * @param content 话题内容
     * @return int 新增的话题id
     * @throws Exception 所有异常
     */
    int saveTopic(int userId, String category, String title, String content) throws Exception;

    /**
     * 保存回复
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @param content 话题内容
     * @return int 新增的回复id
     * @throws Exception 所有异常
     */
    int saveReply(int userId, int topicId, String content) throws Exception;

    /**
     * 删除话题
     *
     * @param topicId 要删除的话题id
     * @throws Exception 所有异常
     */
    void removeTopic(int topicId) throws Exception;

    /**
     * 删除回复
     *
     * @param replyId 回复id
     * @throws Exception 所有异常
     */
    void removeReply(int replyId) throws Exception;

    /**
     * 修改话题内容
     *
     * @param topicId 话题id
     * @param category 分类
     * @param title 标题
     * @param content 新话题内容
     * @throws Exception 所有异常
     */
    void alterTopicContent(int topicId,String category, String title, String content) throws Exception;

    /**
     * 修改回复内容
     *
     * @param replyId 回复id
     * @param content 新回复内容
     * @throws Exception 所有异常
     */
     void alterReplyContent(int replyId, String content) throws Exception;
}
