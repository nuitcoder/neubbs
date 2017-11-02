package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  IUserServer 接口实现类
 *
 *  @author Suva
 */
@Service("userServiceImpl")
public class UserServiceImpl implements IUserService {

    private final IUserDAO userDAO;

    /**
     * Constuctor
     */
    @Autowired
    public UserServiceImpl(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    public void registerUser(UserDO user) throws Exception {
        int effectRow = userDAO.saveUser(user);
        if (effectRow == 0) {
            //处理异常
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_01);
        }
    }

    @Override
    public UserDO getUserInfoById(int id) throws Exception {
        UserDO user = userDAO.getUserById(id);
        if (user == null) {
            throw new AccountErrorException(ApiMessage.NO_USER).log(id + LogWarn.ACCOUNT_01);
        }

        return user;
    }

    @Override
   public UserDO getUserInfoByName(String username) throws Exception {
        UserDO user = userDAO.getUserByName(username);
        if (user == null) {
            throw new AccountErrorException(ApiMessage.NO_USER).log(username + LogWarn.ACCOUNT_01);
        }
       return user;
   }

    @Override
    public UserDO getUserInfoByEmail(String email) throws Exception {
        UserDO user = userDAO.getUserByEmail(email);
        if (user == null) {
            throw new AccountErrorException(ApiMessage.NO_USER).log(email + LogWarn.ACCOUNT_01);
        }

        return userDAO.getUserByEmail(email);
    }

   @Override
   public void alterUserPassword(String username, String password) throws Exception {
       int effectRow = userDAO.updateUserPasswordByName(username, password);
       if (effectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_02);
       }
   }

   @Override
   public void alterUserEmail(String username, String email) throws Exception {
       int effectRow = userDAO.updateUserEmailByName(username, email);
       if (effectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_02);
       }
   }

    @Override
    public void activationUser(String email) throws Exception {
        int effectRow = userDAO.updateUserStateForActivationByEmail(email);
        if (effectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_02);
        }
    }

    @Override
    public void uploadUserImage(String username, String image) throws Exception {
        int effectRow = userDAO.updateUserImageByName(username, image);
        if (effectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_02);
        }
    }

    @Override
    public void isOccupyByUsername(String username) throws Exception {
        if (userDAO.getUserByName(username) != null) {
            throw new AccountErrorException(ApiMessage.USERNAME_REGISTERED).log(username + LogWarn.ACCOUNT_02);
        }
    }

    @Override
    public void isOccupyByEmail(String email) throws Exception {
        if (userDAO.getUserByEmail(email) != null) {
            throw new AccountErrorException(ApiMessage.EMAIL_REGISTERED).log(email + LogWarn.ACCOUNT_08);
        }
    }
}
