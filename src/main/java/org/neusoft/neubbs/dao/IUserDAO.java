package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.UserDO;
import org.springframework.stereotype.Repository;

/**
 * 用户数据访问接口（Data Access Object）
 *      - 针对 forum_user
 *      - resources/mapping/UserMapper.xml 配置 SQL
 *
 * @author Suvan
 */
@Repository
public interface IUserDAO {

    /**
     * 保存用户
     *      - name, password, email
     *      - UserDO 对象的 id 属性会注入新生成的自增 id
     *
     * @param user 用户对象
     * @return int 插入行数
     */
    int saveUser(UserDO user);

    /**
     * 删除用户
     *
     * @param userId 用户id
     * @return int 已删除行数
     */
    int removeUserById(int userId);

    /**
     * 获取最大值用户 id
     *      - 最新插入用户 id
     *
     * @return int 最新插入用户id
     */
    int getMaxUserId();

    /**
     * （id）获取用户
     *
     * @param userId 用户id
     * @return UserDO 用户对象
     */
    UserDO getUserById(int userId);

    /**
     * （name）获取用户
     *
     * @param name 用户名
     * @return UserDO 用户对象
     */
    UserDO getUserByName(String name);

    /**
     * （email）获取用户
     *
     * @param email 用户邮箱
     * @return UserDO 用户对象
     */
    UserDO getUserByEmail(String email);

    /**
     * （用户名）更新用户密码
     *
     * @param username 用户名
     * @param newPassword 用户密码
     * @return int 更新行数
     */
    int updateUserPasswordByName(String username, String newPassword);

    /**
     * （用户名）更新用户邮箱
     *
     * @param username 用户名
     * @param newEmail 新邮箱
     * @return int 更新行数
     */
    int updateUserEmailByName(String username, String newEmail);

    /**
     * （用户名）更新用户权限
     *
     * @param username 用户名
     * @param newRank 新用户权限
     * @return int 更新行数
     */
    int updateUserRankByName(String username, String newRank);

    /**
     * （用户名）更新用户头像
     *
     * @param username 用户名
     * @param newAvator 新用户头像文件名（服务端）
     * @return int 更新行数
     */
    int updateUserAvatorByName(String username, String newAvator);

    /**
     * （用户邮箱）更新用户激活状态
     *      - 直接激活设置为 1（0-未激活，1-已激活）
     *
     * @param email 用户邮箱
     * @return int 更新行数
     */
    int updateUserStateToActivateByEmail(String email);
}
