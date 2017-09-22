package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.DBRequestStatus;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    protected IUserDAO userDAO;

    @Override
    public String saveUserByUser(UserDO user){
        int result = userDAO.saveUserByUser(user);

        if(result == 0){
            return DBRequestStatus.INSERT_FAIL;
        }
        return DBRequestStatus.INSERT_SUCCESS;
    }

    @Override
    public String removeUserById(Integer id){
        int result = userDAO.removeUserById(id);

        if(result == 0){
            return DBRequestStatus.DETELE_FAIL;
        }
        return DBRequestStatus.DETELE_SUCCESS;
    }

    @Override
    public UserDO getUserById(Integer id){
        return userDAO.getUserById(id);
    }

    @Override
    public String updateUserByUser(UserDO user){
        int result = userDAO.updateUserByUser(user);

        if(result == 0){
            return DBRequestStatus.UPDATE_FAIL;
        }
        return DBRequestStatus.UPDATE_SUCCESS;
    }

    @Override
    public String truncateUserTable(String table){
        int result = userDAO.truncateUserTable(table);

        if(result == 0){
            return DBRequestStatus.TRUNCATE_FAIL;
        }
        return DBRequestStatus.TRUNCATE_SUCCESS;
    }
}
