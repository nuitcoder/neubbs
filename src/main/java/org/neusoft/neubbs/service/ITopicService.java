package org.neusoft.neubbs.service;

import java.util.List;
import java.util.Map;

/**
 * 话题业务接口
 *      - 保存话题
 *      - 保存回复
 *      - 保存话题分类
 *      - 删除话题
 *      - 删除回复
 *      - 统计话题总数
 *      - 统计话题总页数
 *      - 获取话题内容点赞数
 *      - 获取话题内容 Model Map
 *      - 获取话题回复 Model Map
 *      - 获取热门话题列表
 *      - 获取话题列表
 *      - 获取所有话题分类
 *      - 修改话题内容
 *      - 修改回复内容
 *      - 增加话题阅读数（+1）
 *      - （通过指令）修改话题喜欢人数（+1 or -1）
 *      - 修改话题分类描述
 *      - 是否喜欢话题
 *      - 判断用户是否收藏话题
 *      - 判断用户是否关注话题
 *      - 操作喜欢话题
 *      - 操作收藏话题
 *      - 操作关注话题
 *
 * @author Suvan
 */
public interface ITopicService {

    /**
     * 保存话题
     *      - userId 无需判断存在性（用户必须登陆，才能访问相应接口，发布主题）
     *
     * @param userId 用户 id
     * @param categoryNick 话题分类昵称
     * @param title 话题标题
     * @param topicContent 话题内容
     * @return Map 保存（新增的话题 id）
     */
    Map<String, Object> saveTopic(int userId, String categoryNick, String title, String topicContent);

    /**
     * 保存回复
     *      - userId 无需判断存在性
     *      - topicId 需判断存在性
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @param replyContent 话题内容
     * @return Map 保存（新增的回复 id）
     */
    Map<String, Object> saveReply(int userId, int topicId, String replyContent);

    /**
     * 保存话题分类
     *      - categoryNick 和 categoryName 都需要验证存在性（不能重复）
     *
     * @param categoryNick 话题分类昵称（英文）
     * @param categoryName 话题分类名称（中文）
     * @return Map 话题分类 Map（包含 id（原 nick，英文），name）
     */
    Map<String, Object> saveCategory(String categoryNick, String categoryName);

    /**
     * 删除话题
     *      - topicId 需判断存在性
     *      - 删除话题所有回复
     *      - 删除话题行为（未实现）
     *      - 删除话题
     *
     * @param topicId 要删除的话题 id
     */
    void removeTopic(int topicId);

    /**
     * 删除回复
     *      - replyId 需验证存在性
     *
     * @param replyId 回复 id
     */
    void removeReply(int replyId);

    /**
     * 统计话题总数
     *
     * @return int 话题总数
     */
    int countTopicTotals();

    /**
     * 统计话题回复总数
     *      - 所有回复
     *
     * @return int 回复总数
     */
    int countReplyTotals();

    /**
     * 统计话题总页数
     *
     * @param limit 每页限制多少条
     * @param categoryNick 话题分类昵称
     * @param username 用户名
     * @return Map 显示总页数
     */
    Map<String, Object> countTopicTotalPages(int limit, String categoryNick, String username);

    /**
     * 获取话题内容点赞数
     *
     * @param topicId 话题 id
     * @return int 当前话题点赞数量
     */
    int countTopicContentLike(int topicId);

    /**
     * 获取话题内容 Model Map
     *      - topicId 需验证存在性
     *      - 话题信息（基本信息 + 内容 + 分类）
     *      - 回复信息
     *
     * @param topicId 话题 id
     * @return Map 话题内容页信息
     */
    Map<String, Object> getTopicContentModelMap(int topicId);

    /**
     * 获取话题回复 Model Map
     *      - 单条回复
     *
     * @param replyId 回复 id
     * @return Map 回复信息
     */
    Map<String, Object> getTopicReplyModelMap(int replyId);

    /**
     * 获取热门话题列表
     *      - 每天回复数最高
     *      - 默认 10 条（不足 10 条，则补充前一天热议话题）
     *
     * @return List 热门话题列表
     */
    List<Map<String, Object>> listHotTopics();

    /**
     * 获取话题列表
     *      - username 和 categoryNick 需验证存在性
     *      - limit 可为 0，表示使用 neubbs.properties 默认配置文件
     *      - categoryNick 和 username 可以为 null（即表示不参与查询条件）
     *
     * @param limit 每页显示数量
     * @param page 跳转到指定页数
     * @param categoryNick 话题分类昵称（英文）
     * @param username 用户名
     * @return List 话题列表
     */
    List<Map<String, Object>> listTopics(int limit, int page, String categoryNick, String username);

    /**
     * 获取所有话题分类
     *
     * @return List<String> 话题分类列表
     */
    List<Map<String, Object>> listAllTopicCategories();

    /**
     * 修改话题内容
     *      - topicId，newCategoryNick 需验证存在性
     *
     * @param topicId 话题 id
     * @param newCategoryNick 话题分类昵称
     * @param newTitle 新标题
     * @param newTopicContent 新话题内容
     */
    void alterTopicContent(int topicId, String newCategoryNick, String newTitle, String newTopicContent);

    /**
     * 修改回复内容
     *      - replyId 需验证存在性
     *
     * @param replyId 回复 id
     * @param newReplyContent 新回复内容
     */
    void alterReplyContent(int replyId, String newReplyContent);

    /**
     * 增加话题阅读数（+1）
     *      - topicId 需验证存在性
     *
     * @param topicId 话题 id
     */
    void increaseTopicRead(int topicId);

    /**
     * （通过指令）修改话题喜欢人数（+1 or -1）
     *      - user service 从中获取用户当前用户是否已经点击过喜欢按钮
     *      - param check service 将会检查指令是否为 'inc' or 'dec'
     *
     * @param isCurrentUserLikeTopic 当前用户是否喜欢该话题（是否已经点赞）
     * @param topicId 话题 id
     * @param userId 用户 id（已登陆用户 id）
     * @param command 操作指令（inc - 自增1，dec- 自减1）
     * @return int 最新的话题点赞数
     */
     int alterTopicLikeByCommand(boolean isCurrentUserLikeTopic, int topicId,
                                                 int userId, String command);

    /**
     * 修改话题分类描述
     *      - categoryNic 需验证存在性
     *
     * @param categoryNick 话题分类昵称（英文）
     * @param newDescription 新的话题分类描述
     */
    void alterTopicCategoryDescription(String categoryNick, String newDescription);

    /**
     * 是否喜欢话题
     *      - userId 和 topicId 需验证存在性
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @return boolean 判断结果（true - 已喜欢，false - 未喜欢）
     */
    boolean isLikeTopic(int userId, int topicId);

    /**
     * 判断用户是否收藏话题
     *      - userId 和 topicId 需验证存在性
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @return boolean 判断结果（true - 已收藏，false - 未关注）
     */
    boolean isCollectTopic(int userId, int topicId);

    /**
     * 判断用户是否关注话题
     *      - userId 和 topicId 需验证存在性
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @return boolean 判断结果（true - 已关注，false - 未关注）
     */
    boolean isAttentionTopic(int userId, int topicId);

    /**
     * 操作喜欢话题
     *      - 操作取反（已喜欢 -> 未喜欢，未喜欢 -> 已喜欢）
     *      - controller 还要组装，所以返回 List
     *
     * @param userId　用户 id
     * @param topicId 话题 id
     * @return List 用户目前喜欢话题 id 列表
     */
    List<Integer> operateLikeTopic(int userId, int topicId);

    /**
     * 操作收藏话题
     *      - 操作取反（已收藏 -> 未收藏，未收藏 -> 已收藏）
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @return Map 保存用户目前收藏话题 id 列表
     */
    Map<String, Object> operateCollectTopic(int userId, int topicId);

    /**
     * 操作关注话题
     *      - 操作取反（已关注 -> 未关注， 未关注 -> 已关注）
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @return Map 保存用户目前关注话题 id 列表
     */
    Map<String, Object> operateAttentionTopic(int userId, int topicId);
}
