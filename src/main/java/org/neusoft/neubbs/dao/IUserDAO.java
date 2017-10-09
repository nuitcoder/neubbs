package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.UserDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * forum_user表 数据访问对象（UserMapper.xml 映射接口）
 * 【Data Access Object】
 */
@Repository
public interface IUserDAO {
    Integer saveUser(UserDO user);

    Integer removeUserById(int id);

    UserDO getUserById(int id);
    UserDO getUserByName(String name);
    List<UserDO> getAllAdminUser();
    List<UserDO> getAssignDateRegisterUserByYearMonth(int year, int month);
    List<UserDO> getAllUser();

    Integer updateUserPasswordByName(String username, String password);
    Integer updateUserRankByName(String username, String rank);
}
