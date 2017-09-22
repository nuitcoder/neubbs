package org.neusoft.neubbs.dao;

import org.apache.ibatis.annotations.Param;
import org.neusoft.neubbs.entity.UserDO;


/**
 * IUserDAO 接口对象
 */
public interface IUserDAO {

    int saveUserByUser(UserDO user);

    int removeUserById(Integer id);

    UserDO getUserById(Integer id);

    int updateUserByUser(UserDO user);
    int truncateUserTable(@Param("tableName") String tableName);
}
