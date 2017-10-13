package test.org.neusoft.neubbs.db;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.util.JsonUtils;
import org.neusoft.neubbs.util.SecretUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 测试 IUserDAO 接口（对 forum_user 表的数据库操作）
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"}) //注入 Spring 配置文件
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //按方法名字（字典顺序），顺序执行
//@Transactional //测试类执行完后，事务回滚，所执行的数据操作全部回滚
public class UserDAOTestCase {

    @Autowired
    IUserDAO userDAO;

    /**
     * 注册用户
     */
    @Test
    //@Transactional //函数执行完后，事务回滚，将插入记录删除
    public void test1_SaveUser(){
        UserDO user = new UserDO();
            user.setName("testuser");
            user.setEmail("test@xxxx.com");

        String cipherText = SecretUtils.passwordMD5Encrypt("123");
            user.setPassword(cipherText);

        try{
            int effectRow = userDAO.saveUser(user);
            System.out.println("受影响行数：" + effectRow + ",新用户 id ：" + user.getId());
            System.out.println("新用户信息：" + JsonUtils.getJSONStringByObject(userDAO.getUserById(user.getId())));
        }catch (DuplicateKeyException de){
            System.out.println("插入用户失败，用户名重复，fu_name 字段声明 UNIQUE KEY！");
        }
    }

    /**
     * 添加6个管理员
     */
    @Ignore//忽略该用例
    public void addSixAdminUser(){
        String [] amdin = {"abonn","AnAndroidXiang","kayye","topLynch","Nancyshan","suvan"};
        UserDO user = new UserDO();

        for(String s: amdin){
            user.setName(s);
            user.setPassword(SecretUtils.passwordMD5Encrypt("12345"));
            user.setEmail(s + "@neubbs.com");


            userDAO.saveUser(user);//注册用户

            userDAO.updateUserRankByName("admin", user.getName());//修改权限
        }

        System.out.println("管理员添加完毕");
    }

    /**
     * 删除用户
     */
    @Test
    public void test2_RemoveUserById(){
        UserDO user = new UserDO();
            user.setName("testRemoveUser");
            user.setPassword("12345");
            user.setEmail("test@neubbs.com");

        userDAO.saveUser(user);

        int effectRow = userDAO.removeUserById(user.getId());
        System.out.println("删除id = " + user.getId() + " 的用户，删除行数：" + effectRow);
    }

    /**
     * 查询最新插入的 id
     */
    @Ignore //不执行
    public void testGetUserMaxId(){
        System.out.println("最新插入的id：" + userDAO.getUserMaxId());
    }
    /**
     * id 查询用户
     */
    @Test
    public void test3_GetUserById(){
        UserDO user = userDAO.getUserById(1);
        System.out.println("id 查询用户，查询结果：" + JsonUtils.getJSONStringByObject(user));
    }

    /**
     * name 查询用户
     */
    @Test
    public void test4_GetUserByName(){
        UserDO user = userDAO.getUserByName("testuser");
        System.out.println("name 查询用户，查询结果：" + JsonUtils.getJSONStringByObject(user));
    }

    /**
     * 查询所有管理员
     */
    @Test
    public void test5_ListAllAdminUser(){
        List<UserDO> userList = userDAO.listAllAdminUser();
        System.out.println("查询所有管理员（权限为 admin）：");
        for(UserDO user : userList){
            System.out.println(JsonUtils.getJSONStringByObject(user));
        }
    }

    /**
     * 查询某年某月范围内，所有注册用户
     */
    @Test
    public void test6_ListAssignDateRegisterUserByYearMonth(){
        List<UserDO> userList = userDAO.listAssignDateRegisterUserByYearMonth(2017,10);
        System.out.println("查询2017年10月份注册的用户：");
        for(UserDO user : userList){
            System.out.println(JsonUtils.getJSONStringByObject(user));
        }
    }

    /**
     * 查询所有用户
     */
    @Test
    public void test7_ListAllUser(){
        List<UserDO> userList = userDAO.listAllUser();
        System.out.println("获取所有用户：");
        for(UserDO user : userList){
            System.out.println(JsonUtils.getJSONStringByObject(user));
        }
    }

    /**
     * 更新用户密码
     */
    @Test
    public void test8_UpdateUserPasswordById(){
        int effectRow = userDAO.updateUserPasswordByName("testuser", "88888");
        System.out.println("更新 test 用户密码，更新行数：" + effectRow);
    }

    /**
     * 更新用户权限
     */
    @Test
    public void test9_UpdateUserRankById(){
        int effectRow = userDAO.updateUserRankByName("test", "count");
        System.out.println("更新 test 用户权限，更新行数：" + effectRow);
    }
}
