package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.User;
/**
 *  【业务逻辑层】UserService接口
 *
 *
 * @Author Suvan
 * @Date 2017-09-21
 */
public interface IUserService{

    public String insertUser(User user);

    public String deleteUserById(Integer id);


    public User selectUserById(Integer id);
    public User selectUserByName(String name);
    public String selectByNameOnlyUser(String name);

    public String updateUser(User user);    //更新用户(根据id)
}
