package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.constant.db.DatabaseInfo;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  IUserServer 接口实现类
 *
 *  @author Suvan
 */
@Service("userServiceImpl")
public class UserServiceImpl implements IUserService {

    private final IUserDAO userDAO;

    /**
     * Constuctor
     */
    @Autowired
    public UserServiceImpl(IUserDAO userDAO){
        this.userDAO = userDAO;
    }


    @Override
    public void registerUser(UserDO user) throws Exception{
        int effectRow = userDAO.saveUser(user);
        if (effectRow == 0) {
            //处理异常
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.USER_SAVE_FAIL);
        }
    }

    @Override
    public UserDO getUserInfoById(int id){
        return userDAO.getUserById(id);
    }

    @Override
   public UserDO getUserInfoByName(String username){
       return userDAO.getUserByName(username);
   }

    @Override
    public UserDO getUserInfoByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

   @Override
   public void alterUserPassword(String username, String password) throws Exception{
       int effectRow = userDAO.updateUserPasswordByName(username, password);
       if (effectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.USER_ALTER_FAIL);
       }
   }

   @Override
   public void alterUserEmail(String username, String email) throws Exception{
       int effectRow = userDAO.updateUserEmailByName(username, email);
       if (effectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.USER_ALTER_FAIL);
       }
   }

    @Override
    public void activationUser(String email) throws Exception {
        int effectRow = userDAO.updateUserStateForActivationByEmail(email);
        if (effectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.USER_ALTER_FAIL);
        }
    }

    @Override
    public void uploadUserImage(String username, String image) throws Exception{
       int effectRow = userDAO.updateUserImageByName(username, image);
        if (effectRow == 0) {
            throw new DatabaseOperationFailException(DatabaseInfo.DATABASE_EXCEPTION).log(LogWarnInfo.USER_ALTER_FAIL);
        }
    }
}