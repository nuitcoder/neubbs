package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;

import java.util.Map;

/**
 * 用户业务接口
 *      - 增删查改排序
 *
 * @author Suvan
 */
public interface IUserService {

    /**
     * 注册用户
     *
     * @param username 用户名
     * @param password 用户密码
     * @param email 用户邮箱
     * @return UserDO 注册成功，获取新用户信息
     */
    UserDO registerUser(String username, String password, String email);

    /**
     * 登录认证
     *
     * @param username 用户名
     * @param password 用户密码
     * @return UserDO 通过登录验证,获取用户信息
     *
     */
    UserDO loginAuthenticate(String username, String password);

    /**
     * （邮箱）确认用户已经激活
     *
     * @param email 用户邮箱
     */
    void confirmUserActivatedByEmail(String email);


    /**
     * 确认用户匹配 Cookie 用户
     *      - 不匹配则抛出异常，无权进行非本用户操作
     *
     * @param inputUsername 输入用户
     * @param cookieUser Cookie用户对象
     */
    void confirmUserMatchCookieUser(String inputUsername, UserDO cookieUser);

    /**
     * 获取用户信息（已经登录状态）
     *
     * @param username 用户名
     * @param email 用户邮箱
     * @return Map<String, Object> userInfoMap用户信息键值对
     */
    Map<String, Object> getUserInfoToPageModelMap(String username, String email);

    /**
     * id 获取用户信息
     *
     * @param userId 用户id
     * @return UserDO 用户对象
     */
    UserDO getUserInfoById(int userId);

    /**
     * name 获取用户信息
     *
     * @param username 用户名（支持 name 或 email 类型）
     * @return UserDO 用户对象
     */
    UserDO getUserInfoByName(String username);

    /**
     * email 获取用户信息
     *
     * @param email 用户邮箱
     * @return UserDO 用户对象
     */
    UserDO getUserInfoByEmail(String email);

    /**
     * 获取用户信息 Map
     *
     * @param user 用户对象
     * @return Map<String, Object> 已过滤过的用户信息Map
     */
    Map<String, Object> getUserInfoMapByUser(UserDO user);

    /**
     * 用户是否存在
     *
     * @param username 用户名
     * @param email 用户邮箱
     * @return boolean 存在结果
     */
    boolean isUserExist(String username, String email);

    /**
     * 获取用户激活状态
     *
     * @param username 用户名
     * @return boolean 激活状态（true-已激活，false-未激活）
     */
    boolean isUserActivatedByName(String username);

    /**
     * 判断用户激活状态
     *
     * @param state 用户激活状态
     * @return boolean 激活状态（true-激活，false-未激活）
     */
    boolean isUserActivatedByState(int state);


    /**
     * 判断用户是否喜欢话题
     *      - 是否已经点击
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @return boolean 是点击过喜欢按钮（true-喜欢，false-未点击）
     */
    boolean isUserLikeTopic(int userId, int topicId);

    /**
     * 修改用户密码
     *      - 失败直接抛出异常
     *
     * @param username 用户名
     * @param newPassword 用户新密码
     */
    void alterUserPasswordByName(String username, String newPassword);

    /**
     * 修改用户密码
     *
     * @param email 用户邮箱
     * @param newPassword 用户新密码
     */
    void alterUserPasswordByEmail(String email, String newPassword);

    /**
     * 修改用户邮箱
     *
     * @param username 用户名
     * @param newEmail 用户新邮箱
     */
    void alterUserEmail(String username, String newEmail);

    /**
     * 修改用户头像
     *
     * @param username 用户名
     * @param newImageName 用户新头像名字
     */
    void alterUserAvatorImage(String username, String newImageName);

    /**
     * (Base64 加密 token)修改用户激活状态
     *
     * @param token Base64加密密文
     */
    void alterUserActivateStateByToken(String token);

    /**
     * 修改用户行为，喜欢话题 id 列表
     *      - 指令只支持（inc-自增1，dec-自减1）
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @param command 操作指令（inc-自增1，dec-自减1）
     */
    void alterUserActionLikeTopicIdArray(int userId, int topicId, String command);
}
