package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.entity.json.LoginJsonDO;


public interface IUserService{

    public String saveUserByUser(UserDO user);

    public String removeUserById(Integer id);

    public UserDO getUserById(Integer id);
    public UserDO getUserByName(String name);
    public LoginJsonDO getLoginJsonByName(String name);

    public String updateUserByUser(UserDO user);

    public String truncateUserTable(String table);
}
