package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.User;

import java.util.Map;


/**
 * IUserDAO 接口对象
 */
public interface IUserDao {

    int insertUser(User user);

    int deleteUserById(Integer id);

    User selectUserById(Integer id);

    User selectUserByName(String name);

    // 返回一条记录是 Map<字段名, 字段值>, 多条记录 List<Map<字段名, 字段值>>
    Map<String,String> selectUserByNameGetMap(String name);

    int updateUser(User user);
}
