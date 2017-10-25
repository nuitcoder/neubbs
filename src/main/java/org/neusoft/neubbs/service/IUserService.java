package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;

/**
 * 用户业务接口
 *
 * @author Suvan
 */
public interface IUserService{

    /**
     * 注册用户
     *
     * @param user 用户对象
     * @throws Exception 所有异常
     */
    void registerUser(UserDO user) throws Exception;

    /**
     * id 获取用户信息
     *
     * @param id 用户id
     * @return UserDO 用户对象
     */
    UserDO getUserInfoById(int id);

    /**
     * name 获取用户信息
     *
     * @param username 用户名
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
     * 修改用户密码
     *
     * @param username 用户名
     * @param password 新用户名密码
     * @throws Exception 所有异常
     */
     void alterUserPassword(String username, String password) throws Exception;

    /**
     * 修改用户邮箱
     *
     * @param username 用户名
     * @param password 密码
     * @throws Exception 所有异常
     */
    void alterUserEmail(String username, String password) throws Exception;

    /**
     * 激活用户
     *
     * @param email 用户邮箱
     * @throws Exception 所有异常
     */
     void activationUser(String email) throws Exception;

    /**
     * 上传用户头像
     *
     * @param username 用户名
     * @param image 用户图片名
     * @throws Exception 所有异常
     */
    void uploadUserImage(String username, String image) throws Exception;
}