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
            user.setEmail("test@test.com");

        //密码加密
        String cipherText = SecretUtils.encryptUserPassword("123");
            user.setPassword(cipherText);

        try{
            int effectRow = userDAO.saveUser(user);
            System.out.println("受影响行数：" + effectRow + ",新用户 id ：" + user.getId());
            System.out.println("新用户信息：" + JsonUtils.toJSONStringByObject(userDAO.getUserById(user.getId())));
        }catch (DuplicateKeyException de){
            throw new DuplicateKeyException("插入用户失败，fu_name 和 fu_email 字段声明 UNIQUE KEY，不能重复！");
        }
    }

    /**
     * 添加6个管理员
     */
    @Ignore//忽略该用例
    public void addSixAdminUser(){
        String [] adminArray = {"ahonn", "AnAndroidXiang", "kayye", "topLynch", "Nancyshan", "suvan"};
        String [] emailArray = {"ahonn95@outlook.com", "2389351081@qq.com", "584157012@qq.com", "805742416@qq.com", "1169251031@qq.com", "liushuwei0925@gmail.com"};
        UserDO user = new UserDO();

        //插入用户
        int porinter = 0;
        for(String admin: adminArray){
            user.setName(admin);
            user.setPassword(SecretUtils.encryptUserPassword("123456"));
            user.setEmail(emailArray[porinter++]);

            userDAO.saveUser(user); //注册用户

            userDAO.updateUserRankByName(admin, "admin"); //修改权限

            userDAO.updateUserStateForActivationByEmail(user.getEmail());//激活账户

            //SendEmailUtils.sendEmail(emailArray[porinter++],
            //                        "Nebbbs 开发团队",
            //                        "欢迎您成为 Neubbs 项目管理员\n您的的管理员帐号：" + admin + " 密码：123456");//发送邮件

            System.out.println("管理员" + admin + "添加完毕");
        }
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
    public void test31_GetUserById(){
        UserDO user = userDAO.getUserById(userDAO.getUserMaxId());//查询最新插入id
        System.out.println("id 查询用户，查询结果：" + JsonUtils.toJSONStringByObject(user));
    }

    /**
     * name 查询用户
     */
    @Test
    public void test32_GetUserByName(){
        UserDO user = userDAO.getUserByName("testuser");
        System.out.println("name 查询用户，查询结果：" + JsonUtils.toJSONStringByObject(user));
    }

    /**
     * email 查询用户
     */
    @Test
    public void test323_GetUserByEmail(){
        UserDO user = userDAO.getUserByEmail("526097449@qq.com1");
        System.out.println("name 查询用户，查询结果：" + JsonUtils.toJSONStringByObject(user));
    }


    /**
     * 查询所有管理员
     */
    @Test
    public void test33_ListAllAdminUser(){
        List<UserDO> userList = userDAO.listAllAdminUser();
        System.out.println("查询所有管理员（权限为 admin）：");
        for(UserDO user : userList){
            System.out.println(JsonUtils.toJSONStringByObject(user));
        }
    }

    /**
     * 查询某年某月范围内，所有注册用户
     */
    @Test
    public void test34_ListAssignDateRegisterUserByYearMonth(){
        List<UserDO> userList = userDAO.listAssignDateRegisterUserByYearMonth(2017,10);
        System.out.println("查询2017年10月份注册的用户：");
        for(UserDO user : userList){
            System.out.println(JsonUtils.toJSONStringByObject(user));
        }
    }

    /**
     * 查询所有用户
     */
    @Test
    public void test35_ListAllUser(){
        List<UserDO> userList = userDAO.listAllUser();
        System.out.println("获取所有用户：");
        for(UserDO user : userList){
            System.out.println(JsonUtils.toJSONStringByObject(user));
        }
    }

    /**
     * 更新用户密码
     */
    @Test
    public void test41_UpdateUserPasswordByName(){
        String newPassword = SecretUtils.encryptUserPassword("888888");
        int effectRow = userDAO.updateUserPasswordByName("testuser", newPassword);

        System.out.println("更新 test 用户密码，更新行数：" + effectRow);
    }

    /**
     * 更新用户权限
     */
    @Test
    public void test42_UpdateUserRankByName(){
        int effectRow = userDAO.updateUserRankByName("testuser", "admin");
        System.out.println("更新 testuser 用户权限（设为管理员），更新行数：" + effectRow);
    }

    /**
     * 更新用户头像地址
     */
    @Test
    public void test43_UpdateUserImageByName(){
        int effectRow = userDAO.updateUserImageByName("testuser", "E://用户头像//suvan.png");
        System.out.println("更新 testuser 用户头像存放地址：" + effectRow);
    }

    /**
     * 更新用户激活状态（激活用户）
     */
    @Test
    public void test44_UpdateUserStateForActivationByEmail(){
        int effectRow = userDAO.updateUserStateForActivationByEmail("test@test.com");
        System.out.println("更新 testuser 用户激活状态：" + effectRow);
    }

    /**
     * 删减表
     */
    @Ignore
    public void tesetTruncateUserTable(){

    }
}
