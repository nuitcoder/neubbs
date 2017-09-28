package org.neusoft.neubbs.dao;

import org.apache.ibatis.annotations.Param;
import org.neusoft.neubbs.entity.UserDO;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserDAO {

    int saveUserByUser(UserDO user);

    int removeUserById(Integer id);

    UserDO getUserById(Integer id);
    UserDO getUserByName(String name);

    int updateUserByUser(UserDO user);
    int truncateUserTable(@Param("tableName") String tableName);
}
