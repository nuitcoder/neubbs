package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;


public interface IUserService{

    public String saveUserByUser(UserDO user);

    public String removeUserById(Integer id);

    public UserDO getUserById(Integer id);

    public String updateUserByUser(UserDO user);

    public String truncateUserTable(String table);
}
