package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.UserDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserMapper.xml映射接口对象
 */
@Repository
public interface IUserDAO {
    int saveUser(UserDO user);

    int removeUserById(Integer id);

    UserDO getUserById(Integer id);
    UserDO getUserByName(String name);
    List<UserDO> getAllAdminUser();
    List<UserDO> getAssignDateRegisterUserByYearMonth(Integer year, Integer month);
    List<UserDO> getAllUser();

    int updateUserPasswordById(String password, Integer id);
    int updateUserRankById(String rank, Integer id);
}
