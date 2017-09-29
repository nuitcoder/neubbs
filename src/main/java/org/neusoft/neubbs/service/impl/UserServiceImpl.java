package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.db.DBRequestStatus;
import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.constant.user.UserInfo;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.dto.LoginJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * IUserService 实现类
 */
@Service("userServiceImpl")
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
    public UserDO getUserByName(String name){
        return userDAO.getUserByName(name);
    }
    @Override
    public LoginJsonDTO getLoginJsonByName(String name){
        LoginJsonDTO dataLoginJson = new LoginJsonDTO();
        UserDO user = userDAO.getUserByName(name);

        if(user == null){
            dataLoginJson.put(AjaxRequestStatus.SUCCESS, LoginInfo.USER_NOEXIT);
        }else{
            Map<String,String> params = new LinkedHashMap<String, String>();
                params.put(UserInfo.ID,String.valueOf(user.getId()));
                params.put(UserInfo.USERNAME,user.getName());
                params.put(UserInfo.PASSWORD,user.getPassword());
                params.put(UserInfo.SEX,user.getSex());
                params.put(UserInfo.BIRTHDAY,user.getBirthday());
                params.put(UserInfo.PHONE,user.getPhone());
                params.put(UserInfo.ADDRESS,user.getAddress());

                SimpleDateFormat sdf = new SimpleDateFormat(UserInfo.DATE_FORMATE);
                params.put(UserInfo.CREATETIME,sdf.format(user.getCreatetime()));

            dataLoginJson.put(AjaxRequestStatus.SUCCESS,LoginInfo.USER_AUTHENTICATE,params);
        }

        return dataLoginJson;
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
