package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userServiceImpl")
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserDAO userDAO;

    /**
     * 注册用户
     * @param user
     * @return Boolean
     */
    @Override
    public Boolean registerUser(UserDO user){
        int effectRow = userDAO.saveUser(user);
        if (effectRow == 0) {
           return false;
        } else {
            return true;
        }
    }

    /**
     * 根据 id，获取用户信息
     * @param id
     * @return UserDO
     */
    @Override
    public UserDO getUserInfoById(int id){
        return userDAO.getUserById(id);
    }

    /**
     * 根据 username，获取用户信息
     * @param username
     * @return UserDO
     */
   public UserDO getUserInfoByName(String username){
       return userDAO.getUserByName(username);
   }



    /**
     * 修改用户密码
     * @param username
     * @param password
     * @return Boolean
     */
   @Override
   public Boolean alterUserPasswordByName(String username, String password){
       int effectRow = userDAO.updateUserPasswordByName(username, password);
       if (effectRow == 0) {
           return false;
       } else {
           return true;
       }
   }

    /**
     * 修改用户权限
     * @param username
     * @param rank
     * @return Boolean
     */
   @Override
   public Boolean alterUserRankByName(String username, String rank){
       int effectRow = userDAO.updateUserRankByName(username, rank);
       if (effectRow == 0) {
           return false;
       } else {
           return true;
       }
   }
}