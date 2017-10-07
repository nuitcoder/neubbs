package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;

import java.util.List;

/**
 * 用户业务接口
 */
public interface IUserService{
    Integer registerUser(UserDO user);

    Integer removeUser(Integer id);

    UserDO getUserById(Integer id);
    UserDO getUserByName(String name);
    List<UserDO> getAllAdminUser();
    List<UserDO> getAssiginDateRegisterUserByYearMonth(Integer year, Integer month);
    List<UserDO> getAllUser();

    Integer updateUserPasswordById(String password, Integer id);
    Integer updateUserRankById(String rank, Integer id);
}