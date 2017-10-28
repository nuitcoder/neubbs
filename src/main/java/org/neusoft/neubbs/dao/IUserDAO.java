package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.UserDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * forum_user表 数据访问对象（UserMapper.xml 映射接口）
 * 【Data Access Object】
 *
 * @author Suvan
 */
@Repository
public interface IUserDAO {
    /**
     *  保存用户
     *
     * @param user 用户对象
     * @return int 插入行数
     */
    int saveUser(UserDO user);

    /**
     * 删除用户
     *
     * @param userId 用户id
     * @return int 删除行数
     */
    int removeUserById(int userId);

    /**
     * 获取用户最大 id（最新插入的id）
     *
     * @return int 最新插入用户id
     */
    int getUserMaxId();

    /**
     * id获取用户对象
     *
     * @param userId 用户id
     * @return UserDO 用户对象
     */
    UserDO getUserById(int userId);

    /**
     * name 获取用户对象
     *
     * @param name 用户名
     * @return UserDO 用户对象
     */
    UserDO getUserByName(String name);

    /**
     * email 获取用户对象
     *
     * @param email 用户邮箱
     * @return UserDO 用户对象
     */
    UserDO getUserByEmail(String email);

    /**
     * 获取所有管理员用户
     *
     * @return List<UserDO> 用户列表
     */
    List<UserDO> listAllAdminUser();

    /**
     * 获取指定 年-月 注册用户
     *
     * @param year 年
     * @param month 月
     * @return List<UserDO> 用户列表
     */
    List<UserDO> listAssignDateRegisterUserByYearMonth(int year, int month);

    /**
     * 获取所有用户
     *
     * @return List<UserDO> 用户列表
     */
    List<UserDO> listAllUser();

    /**
     * 更新用户密码
     *
     * @param name 用户名
     * @param password 用户密码
     * @return int 更新行数
     */
    int updateUserPasswordByName(String name, String password);

    /**
     * 更新用户邮箱
     *
     * @param name 用户名
     * @param email 新邮箱
     * @return int 更新行数
     */
    int updateUserEmailByName(String name, String email);

    /**
     * 更新用户权限
     *
     * @param name 用户名
     * @param rank 用户权限
     * @return int 更新行数
     */
    int updateUserRankByName(String name, String rank);

    /**
     * 更新用户头像地址
     *
     * @param name 用户名
     * @param image 新头像地址
     * @return int 更新行数
     */
    int updateUserImageByName(String name, String image);

    /**
     * 更新用户激活状态（直接激活，state字段修改为 1<0-未激活，1-激活>）
     *
     * @param email 用户邮箱
     * @return int 更新行数
     */
    int updateUserStateForActivationByEmail(String email);

    /**
     * 删减表
     */
    void truncateUserTable();
}
