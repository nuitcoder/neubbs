package org.neusoft.neubbs.service;

import java.util.List;
import java.util.Map;

/**
 * 话题业务接口
 *
 * @author Suvan
 */
public interface ITopicService {

    /**
     * 修改话题内容
     *
     * @param topicId 话题id
     * @param newCategory 分类
     * @param newTitle 新标题
     * @param newTopicContent 新话题内容
     */
    void alterTopicContent(int topicId, String newCategory, String newTitle, String newTopicContent);

    /**
     * 修改回复内容
     *
     * @param replyId 回复id
     * @param newReplyContent 新回复内容
     */
    void alterReplyContent(int replyId, String newReplyContent);

    /**
     * 统计话题总页数
     *
     * @param limit 每页限制多少条
     * @param category 分类
     * @param username 用户名
     * @return int 总页数
     */
    int countTopicTotalPages(int limit, String category, String username);

    /**
     * 获取话题内容页面 Map
     *
     * @param topicId 话题id
     * @return Map 话题内容页信息
     */
    Map<String, Object> getTopicContentPageModelMap(int topicId);

    /**
     * 获取话题回复页面Map
     *      - 单条回复
     *
     * @param replyId 回复id
     * @return Map 回复信息
     */
    Map<String, Object> getReplyPageModelMap(int replyId);

    /**
     * 获取话题列表
     *      - 包含话题基本信息，内容
     *      - 话题用户信息
     *
     * @param limit 每页显示数量
     * @param page 跳转到指定页数
     * @param category 分类目录
     * @param username 用户名
     * @return List<Map> 话题列表
     */
    List<Map<String, Object>> listTopics(int limit, int page, String category, String username);

    /**
     * 获取所有话题分类（去重）
     *
     * @return List<String> 话题分类列表
     */
    List<String> listTopicCategory();

    /**
     * 删除话题
     *
     * @param topicId 要删除的话题id
     */
    void removeTopic(int topicId);

    /**
     * 删除回复
     *
     * @param replyId 回复id
     */
    void removeReply(int replyId);

    /**
     * 保存话题
     *
     * @param userId 用户id
     * @param category 话题分类
     * @param title 话题标题
     * @param topicContent 话题内容
     * @return int 新增的话题id
     */
    int saveTopic(int userId, String category, String title, String topicContent);

    /**
     * 保存回复
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @param replyContent 话题内容
     * @return int 新增的回复id
     */
    int saveReply(int userId, int topicId, String replyContent);
}
