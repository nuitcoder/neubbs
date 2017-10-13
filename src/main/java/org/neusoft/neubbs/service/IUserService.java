package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;

/**
 * 用户业务接口
 */
public interface IUserService{
    Boolean registerUser(UserDO user);

    UserDO getUserInfoById(int id);
    UserDO getUserInfoByName(String username);

    Boolean alterUserPasswordByName(String username, String password);
    Boolean alterUserRankByName(String username, String rank);
}