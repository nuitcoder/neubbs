package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.User;

import java.util.Map;


/**
 * 【数据库层】IUserDAO接口对象
 *
 * @Author Suvan
 * @Date 2017-05-19-9:43
 */
public interface IUserDao {

    int insertUser(User user);

    int deleteUserById(Integer id);

    User selectUserById(Integer id);
    User selectUserByName(String name);
    Map<String,String> selectUserByNameGetMap(String name);  //返回一条记录是Map<字段名,字段值>,多条记录List<Map<字段名,字段值>>

    int updateUser(User user);



}
