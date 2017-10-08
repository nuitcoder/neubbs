package test.org.neusoft.neubbs.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 测试 IUserDAO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class IUserDAOTestCase {

    @Autowired
    IUserDAO userDAO;

    /**
     * 注册用户
     */
    @Test
    public void testSaveUser(){
        UserDO user = new UserDO();
            user.setName("hello112345kk");
            user.setPassword("12345");
            user.setEmail("liushuwei0925@gmail.com");

        try{
            int effectRow = userDAO.saveUser(user);
            System.out.println("受影响行数：" + effectRow);
            System.out.println("返回注册对象主键（id）："  + user.getId());
        }catch (DuplicateKeyException de){
            System.out.println("插入用户失败，用户名重复，fu_name 字段声明 UNIQUE KEY！");
        }
    }

    /**
     * 添加6个管理员
     */
    @Test
    public void addSixAdminUser(){

        String [] amdin = {"abonn","AnAndroidXiang","kayye","topLynch","Nancyshan","suvan"};
        UserDO user = new UserDO();
        for(String s: amdin){
            user.setName(s);
            user.setPassword("12345");
            user.setEmail(s + "@s.com");

            userDAO.saveUser(user);//注册用户，id会变化

            userDAO.updateUserRankByName("admin", user.getName());//修改用户权限
        }

        System.out.println("管理员添加完毕");
    }

    /**
     * 删除用户
     */
    @Test
    public void testRemoveUserById(){
        int effectRow = userDAO.removeUserById(1);
        System.out.println("受影响行数：" + effectRow);
    }

    /**
     * 根据 id 查询用户
     */
    @Test
    public void testGetUserById(){
        UserDO user = userDAO.getUserById(2);
        System.out.println("id查询到用户：" + JsonUtils.getJSONStringByObject(user));
    }

    /**
     * 根据 name 查询用户
     */
    @Test
    public void testGetUserByName(){
        UserDO user = userDAO.getUserByName("oo");
        System.out.println(user);
        System.out.println("name查询到用户：" + JsonUtils.getJSONStringByObject(user));
    }

    /**
     * 查询所有管理员
     */
    @Test
    public void testGetAllAdminUser(){
        List<UserDO> userList = userDAO.getAllAdminUser();
        System.out.println("查询所有管理员：");
        for(UserDO user : userList){
            System.out.println(JsonUtils.getJSONStringByObject(user));
        }
    }

    /**
     * 查询某年某月注册用户
     */
    @Test
    public void testGetAssignDateRegisterUserByYearMonth(){
        List<UserDO> userList = userDAO.getAssignDateRegisterUserByYearMonth(2017,10);
        System.out.println("查询2017年10月份注册的用户：");
        for(UserDO user : userList){
            System.out.println(JsonUtils.getJSONStringByObject(user));
        }
    }

    /**
     * 查询所有用户资料
     */
    @Test
    public void testGetAllUser(){
        List<UserDO> userList = userDAO.getAllUser();
        System.out.println("获取所有用户：");
        for(UserDO user : userList){
            System.out.println(JsonUtils.getJSONStringByObject(user));
        }
    }

    /**
     * 更新用户密码
     */
    @Test
    public void testUpdateUserPasswordById(){
        int effectRow = userDAO.updateUserPasswordByName("test", "88888");
        System.out.println("更新用户密码，影响行数：" + effectRow);
    }

    /**
     * 修改用户权限
     */
    @Test
    public void testUpdateUserRankById(){
        int effectRow = userDAO.updateUserRankByName("test", "user");
        System.out.println("更新用户权限，影响行数：" + effectRow);
    }
}
