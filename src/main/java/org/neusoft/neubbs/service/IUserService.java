package org.neusoft.neubbs.service;

import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.entity.UserDO;

/**
 * 用户业务接口
 *
 * @author Suvan
 */
public interface IUserService {

    /**
     * 注册用户
     *
     * @param user 用户对象
     * @throws DatabaseOperationFailException 数据操作失败异常
     */
    void registerUser(UserDO user) throws DatabaseOperationFailException;

    /**
     * id 获取用户信息
     *
     * @param id 用户id
     * @return UserDO 用户对象
     * @throws AccountErrorException 账户错误异常
     */
    UserDO getUserInfoById(int id) throws AccountErrorException;

    /**
     * name 获取用户信息
     *
     * @param username 用户名
     * @return UserDO 用户对象
     * @throws AccountErrorException 账户错误异常
     */
    UserDO getUserInfoByName(String username) throws AccountErrorException;

    /**
     * email 获取用户信息
     *
     * @param email 用户邮箱
     * @return UserDO 用户对象
     * @throws AccountErrorException 账户错误异常
     */
    UserDO getUserInfoByEmail(String email) throws AccountErrorException;

    /**
     * 修改用户密码
     *
     * @param username 用户名
     * @param password 新用户名密码
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
     void alterUserPassword(String username, String password) throws DatabaseOperationFailException;

    /**
     * 修改用户邮箱
     *
     * @param username 用户名
     * @param password 密码
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
    void alterUserEmail(String username, String password) throws DatabaseOperationFailException;

    /**
     * 激活用户
     *
     * @param email 用户邮箱
     * @throws AccountErrorException 账户错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
     void activationUser(String email) throws AccountErrorException, DatabaseOperationFailException;

    /**
     * 上传用户头像
     *
     * @param username 用户名
     * @param image 用户图片名
     * @throws DatabaseOperationFailException 数据库操作失败异常
     */
    void uploadUserImage(String username, String image) throws DatabaseOperationFailException;


    /**
     * 判断用户名是否被占用
     *
     * @param username 用户名
     * @throws AccountErrorException 账户错误异常
     */
    void isOccupyByUsername(String username) throws AccountErrorException;

    /**
     * 判断邮箱是否被占用
     *
     * @param email 用户邮箱
     * @throws AccountErrorException 账户错误异常
     */
    void isOccupyByEmail(String email) throws AccountErrorException;


}
