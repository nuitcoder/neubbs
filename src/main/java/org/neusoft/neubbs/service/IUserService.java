package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.User;

/**
 * UserService接口
 */
public interface IUserService{

    public String insertUser(User user);

    public String deleteUserById(Integer id);


    public User selectUserById(Integer id);
    public User selectUserByName(String name);
    public String selectOneUserByName(String name);

    public String updateUser(User user);
}
