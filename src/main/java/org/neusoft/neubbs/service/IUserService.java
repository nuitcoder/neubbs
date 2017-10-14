package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;

/**
 * 用户业务接口
 */
public interface IUserService{
    Boolean registerUser(UserDO user);

    UserDO getUserInfoById(int id);
    UserDO getUserInfoByName(String username);
    UserDO getUserInfoByEmail(String email);

    Boolean alterUserPasswordByName(String username, String password);
    Boolean activationUserByEmail(String email);
}