package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.SecretUtil;
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
//@FixMethodOrder(MethodSorters.NAME_ASCENDING) //按方法名字（字典顺序），顺序执行
//@Transactional // 声明使用事务,,所有 Test 默认自动回滚
public class UserDAOTestCase {

    @Autowired
    IUserDAO userDAO;

    @BeforeClass
    public static void init() {
         //设置数据源云数据源（默认是使用云数据源） or 本地数据源
//        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.CLOUD_DATA_SOURCE_MYSQL);
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 添加测试用户
     */
    @Ignore
//  @Transactional //方法级别事务回滚（声明单个方法）
    public void testSaveUser() throws Exception {
        UserDO user = new UserDO();
            user.setName("testUser");
            user.setPassword(SecretUtil.encryptUserPassword("123456"));
            user.setEmail("testUser@neubbs.com");

            int effectRow = 0;
            try {
                effectRow = userDAO.saveUser(user);
            }catch (DuplicateKeyException de){
                throw new DuplicateKeyException("插入用户失败，fu_name 和 fu_email 字段声明 UNIQUE KEY，不能重复插入！");
            }

            Assert.assertNotEquals(effectRow, 0);

            System.out.println("插入测试用户信息：" + JsonUtil.toJSONStringByObject(userDAO.getUserById(user.getId())));
    }

    /**
     * 添加6个管理员用户
     */
    @Ignore
    public void testSaveSixAdminUser() {
        String [] adminArray = {"ahonn", "AnAndroidXiang", "kayye", "topLynch", "Nancyshan", "suvan"};
        String password = "123456";
        String [] emailArray = {"ahonn95@outlook.com", "2389351081@qq.com",
                                "584157012@qq.com", "805742416@qq.com",
                                "1169251031@qq.com", "liushuwei0925@gmail.com"};

        //插入用户
        int porinter = 0;
        UserDO user = new UserDO();
        for(String admin: adminArray){
            user.setName(admin);
            user.setPassword(SecretUtil.encryptUserPassword(password));
            user.setEmail(emailArray[porinter++]);

            //注册用户（插入行数不能为0）
            Assert.assertNotEquals(userDAO.saveUser(user), 0);

            //修改权限
            Assert.assertNotEquals(userDAO.updateUserRankByName(admin, "admin"), 0);

            //激活账户
            Assert.assertNotEquals(userDAO.updateUserStateForActivationByEmail(user.getEmail()), 0);

            //发送邮件
            //SendEmailUtil.sendEmail(emailArray[porinter++],
            //                        "Nebbbs 开发团队",
            //                        "欢迎您成为 Neubbs 项目管理员\n您的的管理员帐号：" + admin + " 密码：123456");//发送邮件

            System.out.println("管理员" + admin + "添加完毕");
        }
    }

    /**
     * 删除用户（新增一个用户后，执行删除）
     */
    @Ignore
    public void testRemoveUserById() {
        UserDO user = new UserDO();
            user.setName("testRemoveUser");
            user.setPassword("123456");
            user.setEmail("test@neubbs.com");

        Assert.assertNotEquals(userDAO.saveUser(user), 0);

        int effectRow = userDAO.removeUserById(user.getId());
        Assert.assertNotEquals(effectRow, 0);

        System.out.println("删除id = " + user.getId() + " 的用户，删除行数：" + effectRow);
    }

    /**
     * 查询用户最大 id（最新插入的 id）
     */
    @Ignore
    public void testGetUserMaxId() {
        int maxId = userDAO.getUserMaxId();

        Assert.assertTrue(maxId > 0);

        System.out.println("最新插入的id：" + userDAO.getUserMaxId());
    }

    /**
     * id 查询用户
     */
    @Test
    public void testGetUserById() {
        int selectId  = 1;

        UserDO user = userDAO.getUserById(selectId);
        Assert.assertNotEquals(user, null);

        System.out.println("成功查询到 id = " + selectId + " 用户，用户信息: " + JsonUtil.toJSONStringByObject(user));
    }

    /**
     * name 查询用户
     */
    @Test
    public void testGetUserByName() {
        String selectUsername = "testUser";

        UserDO user = userDAO.getUserByName(selectUsername);
        Assert.assertNotEquals(user, null);

        System.out.println("成功查询到 name = " + selectUsername + " 用户，用户信息：" + JsonUtil.toJSONStringByObject(user));
    }

    /**
     * email 查询用户
     */
    @Test
    public void testGetUserByEmail() {
        String selectUserEmail = "testUser@neubbs.com";

        UserDO user = userDAO.getUserByEmail(selectUserEmail);
        Assert.assertNotEquals(user, null);

        System.out.println("成功查询到 email = " + selectUserEmail + " 用户，用户信息：" + JsonUtil.toJSONStringByObject(user));
    }


    /**
     * 查询所有管理员
     */
    @Test
    public void testListAllAdminUser() {
        List<UserDO> userList = userDAO.listAllAdminUser();

        Assert.assertNotEquals(userList.size(), 0);

        System.out.println("打印所有管理员（权限为 admin）信息：");
        for(UserDO user : userList){
            System.out.println(JsonUtil.toJSONStringByObject(user));
        }
    }

    /**
     * 查询 2017年10月 范围内，所有注册用户
     */
    @Test
    public void testListAssignDateRegisterUserByYearMonth() {
        List<UserDO> userList = userDAO.listAssignDateRegisterUserByYearMonth(2017, 10);

        Assert.assertNotEquals(userList.size(), 0);

        System.out.println("打印 2017年10月份 注册的所有用户的用户信息：");
        for(UserDO user : userList){
            System.out.println(JsonUtil.toJSONStringByObject(user));
        }
    }

    /**
     * 查询所有用户
     */
    @Test
    public void testListAllUser() {
        List<UserDO> userList = userDAO.listAllUser();

        Assert.assertNotEquals(userList.size(), 0);

        System.out.println("打印所有用户的用户信息：");
        for(UserDO user : userList){
            System.out.println(JsonUtil.toJSONStringByObject(user));
        }
    }

    /**
     * 更新用户密码
     */
    @Ignore
    public void testUpdateUserPasswordByName() {
        String updateUserName = "testUser";
        String newPassword = SecretUtil.encryptUserPassword("8888881231");

        int effectRow = userDAO.updateUserPasswordByName(updateUserName, newPassword);
        Assert.assertNotEquals(effectRow, 0);

        System.out.println("更新 " + updateUserName + " 用户密码成功，更新行数：" + effectRow);
    }

    /**
     * 更新用户邮箱
     */
    @Ignore
    public void testUpdateUserEmailByName() {
        String updateUserName = "testUser";
        String newEmail = "testUser@neubbs.com.new.email";

        int effectRow = userDAO.updateUserEmailByName(updateUserName, newEmail);
        Assert.assertNotEquals(effectRow, 0);

        System.out.println("更新 " + updateUserName + " 用户邮箱成功，更新行数 :" + effectRow);
    }

    /**
     * 更新用户权限
     */
    @Ignore
    public void testUpdateUserRankByName() {
        String updateUserName = "testUser";
        String newRank = "admin";

        int effectRow = userDAO.updateUserRankByName(updateUserName, newRank);
        Assert.assertNotEquals(effectRow, 0);

        System.out.println("更新 " + updateUserName + " 用户权限（设为管理员）成功，更新行数：" + effectRow);
    }

    /**
     * 更新用户头像图片名
     */
    @Ignore
    public void testUpdateUserImageByName() {
        String updateUserName = "testUser";
        String newImage = "新的头像.jpg";

        int effectRow = userDAO.updateUserImageByName(updateUserName, newImage);

        System.out.println("更新 " + updateUserName + " 用户图片名成功，更新行数：" + effectRow);
    }

    /**
     * 更新用户激活状态（激活用户）
     */
    @Ignore
    public void testUpdateUserStateForActivationByEmail() {
        String updateUserEmail = "testUser@neubbs.com.new.email";

        int effectRow = userDAO.updateUserStateForActivationByEmail(updateUserEmail);
        Assert.assertNotEquals(effectRow, 0);

        System.out.println("更新 " + updateUserEmail + " 邮箱用户激活状态成功,更新行数：" + effectRow);
    }

    /**
     * 删减表
     */
    @Ignore
    public void tesetTruncateUserTable() throws Exception{
        userDAO.truncateUserTable();
    }
}
