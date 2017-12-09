package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

/**
 * 测试 IUserDAO 接口
 *      - inject the spring configuration file
 *      - by mehtod name（dictionary order） to do
 *      - class transaction statement, default execute finished rollback
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Transactional
public class UserDAOTest {

    @Autowired
    private IUserDAO userDAO;

    /**
     * 获取测试用户对象
     *
     * @return UserDO 获取测试用户对象
     */
    private UserDO getTestUserDO() {
        UserDO user = new UserDO();
            user.setName("testUser");
            user.setPassword(SecretUtil.encryptUserPassword("123456"));
            user.setEmail("test@neubbs.com");

        return user;
    }


    @BeforeClass
    public static void init() {
        // can set cloud data source (default cloud), this test case set local data source
//      SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.CLOUD_DATA_SOURCE_MYSQL);
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 测试保存用户
     */
    @Test
    @Transactional
    public void testSaveUser() throws Exception {
        UserDO user = this.getTestUserDO();
        Assert.assertEquals(1, userDAO.saveUser(user));
        Assert.assertTrue(user.getId() > 0);
        Assert.assertNotNull(userDAO.getUserById(user.getId()));

        System.out.println("insert user information: "
                + JsonUtil.toJSONStringByObject(userDAO.getUserById(user.getId())));
    }

    /**
     * 测试删除用户
     */
    @Test
    @Transactional
    public void testRemoveUserById() {
        UserDO user = this.getTestUserDO();
        Assert.assertEquals(1, userDAO.saveUser(user));
        Assert.assertEquals(1, userDAO.removeUserById(user.getId()));

        System.out.println("delete id=" + user.getId() + " user");
    }

    /**
     * 测试获取最大用户 id
     *      - 最新插入用户 id
     */
    @Test
    @Transactional
    public void testGetMaxUserId() {
        UserDO user = this.getTestUserDO();
        Assert.assertEquals(1, userDAO.saveUser(user));
        int maxUserId = userDAO.getMaxUserId();
        Assert.assertTrue(maxUserId > 0);

        System.out.println("get maxUserId = " + maxUserId);
    }

    /**
     * 测试 id 获取用户
     */
    @Test
    @Transactional
    public void testGetUserById() throws Exception {
        UserDO user = this.getTestUserDO();
        Assert.assertEquals(1, userDAO.saveUser(user));
        Assert.assertNotNull(userDAO.getUserById(user.getId()));

        System.out.println("get id=" + user.getId() + " user information: "
                + JsonUtil.toJSONStringByObject(user));
    }

    /**
     * 测试 name 查询用户
     */
    @Test
    @Transactional
    public void testGetUserByName() {
        UserDO user = this.getTestUserDO();
        Assert.assertEquals(1, userDAO.saveUser(user));
        Assert.assertNotNull(userDAO.getUserByName(user.getName()));

        System.out.println("get name=" + user.getName() + " user information:"
                + JsonUtil.toJSONStringByObject(user));
    }

    /**
     * 测试 email 查询用户
     */
    @Test
    @Transactional
    public void testGetUserByEmail() {
        UserDO user = this.getTestUserDO();
        Assert.assertEquals(1, userDAO.saveUser(user));
        Assert.assertNotNull(userDAO.getUserByEmail(user.getEmail()));

        System.out.println("get email=" + user.getEmail() + " user information: "
                + JsonUtil.toJSONStringByObject(user));
    }

    /**
     * 测试更新用户密码
     */
    @Test
    @Transactional
    public void testUpdateUserPasswordByName() {
        UserDO user = this.getTestUserDO();
        Assert.assertEquals(1, userDAO.saveUser(user));

        String username = "testUser";
        String newPassword = SecretUtil.encryptUserPassword("45678");
        Assert.assertEquals(1, userDAO.updateUserPasswordByName(username, newPassword));
        Assert.assertEquals(newPassword, userDAO.getUserByName(username).getPassword());

        System.out.println("update username=" + username +" password to <" + newPassword + "> success!");
    }

    /**
     * 测试更新用户邮箱
     */
    @Test
    @Transactional
    public void testUpdateUserEmailByName() {
        UserDO user = this.getTestUserDO();
        Assert.assertEquals(1, userDAO.saveUser(user));

        String username = "testUser";
        String newEmail = "newTest@neubbs.com";
        Assert.assertEquals(1, userDAO.updateUserEmailByName(username, newEmail));
        Assert.assertEquals(newEmail, userDAO.getUserByName(username).getEmail());

        System.out.println("update username=" + username + " email to <" + newEmail + "> success!");
    }

    /**
     * 测试更新用户权限
     */
    @Test
    @Transactional
    public void testUpdateUserRankByName() {
        UserDO user = this.getTestUserDO();
        Assert.assertEquals(1, userDAO.saveUser(user));

        String username = "testUser";
        String newRank = "admin";
        Assert.assertEquals(1, userDAO.updateUserRankByName(username, newRank));
        Assert.assertEquals(newRank, userDAO.getUserByName(username).getRank());

        System.out.println("update username=" + username + " rank to <" + newRank + "> success!");
    }

    /**
     * 测试更新用户头像图片名
     */
    @Test
    @Transactional
    public void testUpdateUserImageByName() {
        UserDO user = this.getTestUserDO();
        Assert.assertEquals(1, userDAO.saveUser(user));

        String username = "testUser";
        String newAvator = "newAvator.jpg";
        Assert.assertEquals(1, userDAO.updateUserAvatorByName(username, newAvator));
        Assert.assertEquals(newAvator, userDAO.getUserByName(username).getAvator());

        System.out.println("update username=" + username + " avator to <" + newAvator + "> success!");
    }

    /**
     * 测试更新用户激活状态
     *      - 输入邮箱，直接激活
     */
    @Test
    @Transactional
    public void testUpdateUserStateForActivationByEmail() {
        UserDO user = this.getTestUserDO();
        Assert.assertEquals(1, userDAO.saveUser(user));

        String email = "test@neubbs.com";
        Integer userActivatedState = 1;
        Assert.assertEquals(1, userDAO.updateUserStateToActivateByEmail(email));
        Assert.assertEquals(userActivatedState, userDAO.getUserByEmail(email).getState());

        System.out.println("update email=" + email + " state to <" + userActivatedState + "> success!");
    }
}
