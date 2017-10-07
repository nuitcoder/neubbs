package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userServiceImpl")
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserDAO userDAO;

    /**
     * 注册用户
     * @param user
     * @return effectRow
     */
    @Override
    public Integer registerUser(UserDO user){
        return userDAO.saveUser(user);
    }

    /**
     * 删除用户
     * @param id
     * @return effectRow
     */
   @Override
   public Integer removeUser(Integer id){
       return userDAO.removeUserById(id);
   }

    /**
     * 根据 id 查询用户信息
     * @param id
     * @return
     */
   @Override
   public UserDO getUserById(Integer id){
       return userDAO.getUserById(id);
   }

    /**
     * 根据 name 查询用户信息
     * @param name
     * @return
     */
   @Override
   public UserDO getUserByName(String name){
       return userDAO.getUserByName(name);
   }

    /**
     * 获取所有管理员用户信息
     * @return
     */
   @Override
   public List<UserDO> getAllAdminUser(){
       return userDAO.getAllAdminUser();
   }

    /**
     * 获取指定日期注册用户
     * @param year
     * @param month
     * @return
     */
   @Override
   public List<UserDO> getAssiginDateRegisterUserByYearMonth(Integer year, Integer month){
       return userDAO.getAssignDateRegisterUserByYearMonth(year, month);
   }

    /**
     * 获取所有用户信息
     * @return
     */
   @Override
   public List<UserDO> getAllUser(){
       return userDAO.getAllUser();
   }

    /**
     * 修改用户密码
     * @param password
     * @param id
     * @return effectRow
     */
   @Override
   public Integer updateUserPasswordById(String password, Integer id){
       return userDAO.updateUserPasswordById(password, id);
   }

    /**
     * 修改用户权限
     * @param rank
     * @param id
     * @return effectRow
     */
   @Override
   public Integer updateUserRankById(String rank, Integer id){
       return userDAO.updateUserRankById(rank, id);
   }
}