package org.neusoft.neubbs.service;

import java.util.List;
import java.util.Map;

/**
 * 话题业务接口
 *      - 增删查改顺序
 *
 * @author Suvan
 */
public interface ITopicService {

    /**
     * 保存话题
     *
     * @param userId 用户id
     * @param categoryNick 话题分类昵称
     * @param title 话题标题
     * @param topicContent 话题内容
     * @return int 新增的话题id
     */
    int saveTopic(int userId, String categoryNick, String title, String topicContent);

    /**
     * 保存回复
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @param replyContent 话题内容
     * @return int 新增的回复id
     */
    int saveReply(int userId, int topicId, String replyContent);

    /**
     * 保存话题分类
     *
     * @param categoryNick 话题分类昵称（英文）
     * @param categoryName 话题分类名称（中文）
     * @return Map 话题分类Map（包含 id（原 nick，英文），name）
     */
    Map<String, Object> saveCategory(String categoryNick, String categoryName);

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
     * 删除话题分类
     *
     * @param categoryNick 话题分类昵称（英文）
     */
    void removeCategory(String categoryNick);

    /**
     * 统计话题总页数
     *
     * @param limit 每页限制多少条
     * @param categoryNick 话题分类昵称
     * @param username 用户名
     * @return int 总页数
     */
    int countTopicTotalPages(int limit, String categoryNick, String username);

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
     * @param categoryNick 话题分类昵称
     * @param username 用户名
     * @return List<Map> 话题列表
     */
    List<Map<String, Object>> listTopics(int limit, int page, String categoryNick, String username);

    /**
     * 获取所有话题分类
     *      - map 包含（id(过滤后)，name）
     *
     * @return List<String> 话题分类列表
     */
    List<Map<String, Object>> listAllTopicCategorys();

    /**
     * 修改话题内容
     *
     * @param topicId 话题id
     * @param categoryNick 话题分类昵称
     * @param newTitle 新标题
     * @param newTopicContent 新话题内容
     */
    void alterTopicContent(int topicId, String categoryNick, String newTitle, String newTopicContent);

    /**
     * 修改回复内容
     *
     * @param replyId 回复id
     * @param newReplyContent 新回复内容
     */
    void alterReplyContent(int replyId, String newReplyContent);

    /**
     * 修改话题回复数（+1）
     *
     * @param topicId 话题id
     */
    void alterTopicReadAddOne(int topicId);

    /**
     * 修改话题喜欢人数（+1）
     *
     * @param topicId 话题id
     * @return int 当前话题点赞数
     */
    int alterTopicLikeAddOne(int topicId);

    /**
     * 修改话题分类描述
     *
     * @param categoryNick 话题分类昵称（英文）
     * @param newDescription 新的话题分类描述
     */
    void alterTopicCategoryDescription(String categoryNick, String newDescription);
}
