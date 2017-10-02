package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;

import java.util.Map;

/**
 * forum_user表 Service接口
 */
public interface IUserService{
    public String saveUserByUser(UserDO user);

    public String removeUserById(Integer id);

    public UserDO getUserById(Integer id);
    public UserDO getUserByName(String name);
    public Map<String,String> listUserInfoByName(String name);

    public String updateUserByUser(UserDO user);

    public String truncateUserTable(String table);
}