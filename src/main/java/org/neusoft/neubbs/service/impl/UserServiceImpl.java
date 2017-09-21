package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.dao.IUserDao;
import org.neusoft.neubbs.entity.User;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


/**
 * UserService 接口实现类
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

    @Resource
    protected IUserDao userDao;   //配置文件自动注入

    public String insertUser(User user){
        int result = userDao.insertUser(user);

        if(result == 0){
            return "插入失败";
        }
        return "成功插入！";
    }

    public String deleteUserById(Integer id){
        int result = userDao.deleteUserById(id);

        if(result == 0){
            return "删除失败.";
        }

        return "成功删除！";
    }

    public User selectUserById(Integer id){
        return userDao.selectUserById(id);
    }

    public User selectUserByName(String name){
        return userDao.selectUserByName(name);
    }

    public String selectOneUserByName(String name){
        Map<String,String> map = userDao.selectUserByNameGetMap(name);

       //遍历Map
       // for(Map.Entry<String,String> entry:map.entrySet()){
       //     System.out.println("\n健：" + entry.getKey() + "----值：" +entry.getValue());
       // }

        if(map != null){
            return "用户名已经存在,请重新输入！";
        }

        return "用户唯一";
    }

    public String updateUser(User user){
        int result = userDao.updateUser(user);

        if(result == 0){
            return "更新失败";
        }
        return "更新成功";
    }
}
