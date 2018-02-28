package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;

import java.util.List;
import java.util.Map;

/**
 * 用户业务接口
 *      - 注册用户
 *      - 登录验证
 *      - 确认用户匹配 Cookie 用户
 *      - 统计用户总数
 *      - 统计用户发帖总数
 *      - 统计用户回复总数
 *      - 统计用户喜欢话题总数
 *      - 统计用户收藏话题总数
 *      - 统计用户关注话题总数
 *      - 统计用户关注人数（主动关注）
 *      - 统计用户被关注人数（被关注）
 *      - 获取用户信息 Model Map
 *      - id 获取用户信息
 *      - name 获取用户信息
 *      - email 获取用户信息
 *      - 获取用户信息 Model Map
 *      - 获取所有主动关注人用户信息 Model List
 *      - 获取所有被关注用户信息列表 Model List
 *      - 判断用户是否存在
 *      - 判断用户激活状态
 *      - 判断用户激活状态
 *      - 判断用户是否喜欢话题
 *      - 判断用户是否主动关注用户
 *      - 修改用户个人信息
 *      - （name）修改用户密码
 *      - （email）修改用户密码
 *      - 修改用户邮箱
 *      - 修改用户头像文件名
 *      - 修改用户激活状态
 *      - 操作喜欢话题
 *      - 操作关注用户
 *
 * @author Suvan
 */
public interface IUserService {

    /**
     * 注册用户
     *      - username, email 需验证存在性（是否已被占用）
     *
     * @param username 用户名
     * @param password 用户密码
     * @param email 用户邮箱
     * @return UserDO 注册成功，重新获取新用户信息
     */
    UserDO registerUser(String username, String password, String email);

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 用户密码
     * @return UserDO 通过登录验证,获取用户信息
     */
    UserDO loginVerification(String username, String password);

    /**
     * （邮箱）确认用户已经激活
     *
     * @param email 用户邮箱
     */
    void confirmUserActivatedByEmail(String email);

    /**
     * 确认用户匹配 Cookie 用户
     *      - 比较输入用户名与 Cookie 用户名
     *      - 不匹配则抛出“无权限”异常，无权进行非本用户操作
     *
     * @param inputUsername 输入用户名
     * @param cookieUser Cookie 用户对象
     */
    void confirmUserMatchCookieUser(String inputUsername, UserDO cookieUser);

    /**
     * 统计用户总数
     *
     * @return int 用户总数
     */
    int countUserTotals();

    /**
     * 统计用户发帖总数
     *      - userId 需验证存在性
     *
     * @param userId 用户 id
     * @return int 用户发帖总数
     */
    int countUserTopicTotals(int userId);

    /**
     * 统计用户回复总数
     *      - userId 需验证存在性
     *
     * @param userId 用户 id
     * @return int 用户回复总数
     */
    int countUserReplyTotals(int userId);

    /**
     * 统计用户喜欢话题总数
     *      - userId 需验证存在性
     *
     * @param userId 用户 id
     * @return int 喜欢话题总数
     */
    int countUserLikeTopicTotals(int userId);

    /**
     * 统计用户收藏话题总数
     *      - userId 需验证存在性
     *
     * @param userId 用户 id
     * @return int 收藏话题总数
     */
    int countUserCollectTopicTotals(int userId);

    /**
     * 统计用户关注话题总数
     *      - userId 需验证存在性
     *
     * @param userId 用户 id
     * @return int 关注话题总数
     */
    int countUserAttentionTopicTotals(int userId);

    /**
     * 统计用户关注人数（主动关注）
     *      - userId 需验证存在性
     *
     * @param userId 用户 id
     * @return int 关注人总数
     */
    int countUserFollowingTotals(int userId);

    /**
     * 统计用户被关注人数（被关注）
     *      - userId 需验证存在性
     *
     * @param userId 用户 id
     * @return int 被关注人总数
     */
    int countUserFollowedTotals(int userId);


    /**
     * 获取用户信息 Model Map
     *      - username 支持用户格式和邮箱格式
     *      - 若同时输入，优先考虑 username
     *
     * @param username 用户名
     * @param email 用户邮箱
     * @return Map userInfoMap 用户信息键值对
     */
    Map<String, Object> getUserInfoModelMap(String username, String email);

    /**
     * id 获取用户信息
     *      - userId 需验证存在性
     *
     * @param userId 用户 id
     * @return UserDO 用户对象
     */
    UserDO getUserInfoById(int userId);

    /**
     * name 获取用户信息
     *      - username 需验证存在性
     *
     * @param username 用户名
     * @return UserDO 用户对象
     */
    UserDO getUserInfoByName(String username);

    /**
     * email 获取用户信息
     *      - email 需验证存在性
     *
     * @param email 用户邮箱
     * @return UserDO 用户对象
     */
    UserDO getUserInfoByEmail(String email);

    /**
     * 获取用户信息 Model Map
     *
     * @param user 用户对象
     * @return Map 已过滤过的用户信息 Map
     */
    Map<String, Object> getUserInfoModelMap(UserDO user);

    /**
     * 获取所有主动关注人用户信息 Model List
     *
     * @param userId 用户 id
     * @return List 用户主动关注用户信息列表
     */
    List<Map<String, Object>> listAllFollowingUserInfoModelList(int userId);

    /**
     * 获取所有被关注用户信息列表 Model List
     *
     * @param userId 用户 id
     * @return List 用户被关注用户信息列表
     */
    List<Map<String, Object>> listAllFollowedUserInfoModelList(int userId);

    /**
     * 判断用户是否存在
     *      - username 支持用户名 or 邮箱格式
     *
     * @param username 用户名
     * @param email 用户邮箱
     * @return boolean 存在结果（true - 存在，false - 不存在）
     */
    boolean isUserExist(String username, String email);

    /**
     * 判断用户激活状态
     *      - username 支持用户名 or 邮箱格式
     *
     * @param username 用户名
     * @return boolean 激活状态（true - 已激活，false - 未激活）
     */
    boolean isUserActivatedByName(String username);

    /**
     * 判断用户激活状态
     *
     * @param userCurrentState 用户当前激活状态
     * @return boolean 激活状态（true - 激活，false - 未激活）
     */
    boolean isUserActivatedByState(int userCurrentState);

    /**
     * 判断用户是否喜欢话题
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @return boolean 是否点击过喜欢按钮（true - 喜欢，false - 未点击）
     */
    boolean isUserLikeTopic(int userId, int topicId);

    /**
     * 判断用户是否主动关注用户
     *      - currentUserId，followingUserI 需验证存在性
     *
     * @param currentUserId 当前用户 id
     * @param followingUserId 主动关注用户 id
     * @return boolean 是否点击关注用户按钮（true - 已关注，false - 未关注）
     */
    boolean isFollowingUser(int currentUserId, int followingUserId);

    /**
     * 修改用户个人信息
     *      - username 需要验证用户存在性
     *
     * @param username 用户名
     * @param sex 性别（0-女，1-男）
     * @param birthday 出生日期
     * @param position 所在地
     * @param description 个人描述
     * @return Map 修改成功后，重新查询的用户信息 Map
     */
    Map<String, Object> alterUserProfile(String username, int sex, String birthday,
                                         String position, String description);

    /**
     * （name）修改用户密码
     *      - username 需验证存在性
     *
     * @param username 用户名
     * @param newPassword 用户新密码
     */
    void alterUserPasswordByName(String username, String newPassword);

    /**
     * （email）修改用户密码
     *      - email 需验证存在性
     *
     * @param email 用户邮箱
     * @param newPassword 用户新密码
     */
    void alterUserPasswordByEmail(String email, String newPassword);

    /**
     * 修改用户邮箱
     *      - username 需验证存在性
     *      - newEmail 需验证是否被占用
     *
     * @param username 用户名
     * @param newEmail 用户新邮箱
     */
    void alterUserEmail(String username, String newEmail);

    /**
     * 修改用户头像文件名
     *      - username 需验证存在性
     *
     * @param username 用户名
     * @param avatarFileName 用户新头像文件名
     */
    void alterUserAvatar(String username, String avatarFileName);

    /**
     * 修改用户激活状态
     *      - 通过 Base64 加密 email token
     *      - 解密后 email token，得到待激活的邮箱，需验证邮箱是否已被激活
     *
     * @param emailToken 邮件 token（激活邮件内，激活链接）
     * @return UserDO 激活后，重新查询用户信息
     */
    UserDO alterUserActivateStateByEmailToken(String emailToken);

    /**
     * 操作喜欢话题
     *      - 可输入操作指令（inc-自增1，dec-自减1）
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @param command 操作指令（inc - 自增1，dec - 自减1）
     */
    void operateLikeTopic(int userId, int topicId, String command);

    /**
     * 操作关注用户
     *
     * @param currentUserId 当前用户 id
     * @param followingUserId 关注用户 id
     * @return List 用户目前主动关注用户 id 列表
     */
    List<Integer> operateFollowUser(int currentUserId, int followingUserId);
}
