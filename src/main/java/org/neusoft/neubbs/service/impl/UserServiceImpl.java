package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.db.MySQLRequestStatus;
import org.neusoft.neubbs.constant.user.UserInfo;
import org.neusoft.neubbs.dao.IUserDAO;
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
    public String saveUser(UserDO user){
        int result = userDAO.saveUser(user);

        if(result == 0){
            return MySQLRequestStatus.INSERT_FAIL;
        }
        return MySQLRequestStatus.INSERT_SUCCESS;
    }

    @Override
    public String removeUserById(Integer id){
        int result = userDAO.removeUserById(id);

        if(result == 0){
            return MySQLRequestStatus.DETELE_FAIL;
        }
        return MySQLRequestStatus.DETELE_SUCCESS;
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
    public Map<String,String> listUserInfoByName(String name){
        UserDO user = userDAO.getUserByName(name);

        Map<String, String> userInfoMap = null;
        if(user != null) {
            userInfoMap = new LinkedHashMap<String, String>();
                userInfoMap.put(UserInfo.ID, String.valueOf(user.getId()));
                userInfoMap.put(UserInfo.USERNAME, user.getName());
                userInfoMap.put(UserInfo.PASSWORD, user.getPassword());
                userInfoMap.put(UserInfo.SEX, user.getSex());
                userInfoMap.put(UserInfo.BIRTHDAY, user.getBirthday());
                userInfoMap.put(UserInfo.PHONE, user.getPhone());
                userInfoMap.put(UserInfo.ADDRESS, user.getAddress());

            SimpleDateFormat sdf = new SimpleDateFormat(UserInfo.DATE_FORMATE);
            userInfoMap.put(UserInfo.CREATETIME, sdf.format(user.getCreatetime()));
        }

        return userInfoMap;
    }

}