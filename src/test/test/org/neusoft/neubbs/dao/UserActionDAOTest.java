package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.IUserActionDAO;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserActionDO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

/**
 * 测试 IUserAction 接口
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class UserActionDAOTest {

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IUserActionDAO userActionDAO;

    /**
     * 保存测试 UserActionDO 对象至数据库
     *      - 新建测试用户
     *      - 保存，且重新查询数据库获得对象（根据用户 id，进行查询）
     *
     * @return UserActionDO 用户行为对象
     */
    private UserActionDO saveTestUserActionDOToDatabase() {
        UserDO user = new UserDO();
            user.setName("testUser");
            user.setPassword(SecretUtil.encryptUserPassword("123456"));
            user.setEmail("testUser@neubbs.com");

        Assert.assertEquals(1, userDAO.saveUser(user));

        UserActionDO userAction = new UserActionDO();
            userAction.setUserId(user.getId());

        Assert.assertEquals(1, userActionDAO.saveUserAction(userAction));

        return userActionDAO.getUserAction(user.getId());
    }


    @BeforeClass
    public static void init() {
        //set local database source
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 测试保存用户行为
     */
    @Test
    @Transactional
    public void testSaveAction() {
        UserActionDO userAction = this.saveTestUserActionDOToDatabase();
        System.out.println("insert user action information: " + JsonUtil.toJSONStringByObject(userAction));
    }

    /**
     * 测试获取用户行为对象
     */
    @Test
    @Transactional
    public void testGetUserAction() {
        UserActionDO userAction = this.saveTestUserActionDOToDatabase();
        System.out.println("get userid=" + userAction.getId()
                + " user action information:" + JsonUtil.toJSONStringByObject(userAction));
    }

    /**
     * 测试获取用户喜欢话题 id 数组
     */
    @Test
    @Transactional
    public void testGetUserActionLikeTopicIdJsonArray() {
        UserActionDO userActionDO = this.saveTestUserActionDOToDatabase();

        int userId = userActionDO.getUserId();
        Assert.assertNotNull(userActionDAO.getUserActionLikeTopicIdJsonArray(userId).getLikeTopicIdJsonArray());

        System.out.println("get userId=" + userId + " like topic json array success!");
    }

    /**
     * 测试获取用户收藏话题 id 数组
     */
    @Test
    @Transactional
    public void testGetUserActionCollectTopicIdJsonArray() {
        UserActionDO userActionDO = this.saveTestUserActionDOToDatabase();

        int userId = userActionDO.getUserId();
        Assert.assertNotNull(userActionDAO.getUserActionCollectTopicIdJsonArray(userId).getCollectTopicIdJsonArray());

        System.out.println("get userId=" + userId + "collect topic json array success!");
    }


    /**
     * 测试获取用户关注话题 id 数组
     */
    @Test
    @Transactional
    public void testGetUserActionAttentionTopicIdJsonArray() {
        UserActionDO userActionDO = this.saveTestUserActionDOToDatabase();

        int userId = userActionDO.getUserId();
        Assert.assertNotNull(userActionDAO.getUserActionAttentionTopicIdJsonArray(userId).getAttentionTopicIdJsonArray());

        System.out.println("get userId=" + userId + " attention topic json array success!");
    }

    /**
     * 测试获取用户主动关注用户 id 数组
     */
    @Test
    @Transactional
    public void testGetUserActionFollowingUserIdJsonArray() {
        UserActionDO userActionDO = this.saveTestUserActionDOToDatabase();

        int userId = userActionDO.getUserId();
        Assert.assertNotNull(userActionDAO.getUserActionFollowingUserIdJsonArray(userId).getFollowingUserIdJsonArray());

        System.out.println("get userId=" + userId + " following user json array success!");
    }

    /**
     * 测试获取用户被关注用户 id 数组
     */
    @Test
    @Transactional
    public void testGetUserActionFollowedUserIdJsonArray() {
        UserActionDO userActionDO = this.saveTestUserActionDOToDatabase();

        int userId = userActionDO.getUserId();
        Assert.assertNotNull(userActionDAO.getUserActionFollowedUserIdJsonArray(userId).getFollowedUserIdJsonArray());

        System.out.println("get userId=" + userId + " followed user json array success!");
    }

    /**
     * 测试更新用户行为，JSON 数组末尾追加
     *      - 喜欢话题 id
     *      - 收藏话题 id
     *      - 关注话题 id
     *      - 主动关注用户 id
     *      - 被关注用户 id
     */
    @Test
    @Transactional
    public void testUpdateUserActionAddElementToAppendEnd() {
        UserActionDO userAction = this.saveTestUserActionDOToDatabase();

        int userId = userAction.getUserId();
        int topicId = 1;
        Assert.assertEquals(1, userActionDAO.updateLikeTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId));
        Assert.assertEquals(1, userActionDAO.updateCollectTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId));
        Assert.assertEquals(1, userActionDAO.updateAttentionTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId));
        System.out.println("success pass update to add like, collect, attention topic test!");

        int followingUserId = 2;
        int followedUserId = 3;
        Assert.assertEquals(1,
                userActionDAO.updateFollowingUserIdJsonArrayByOneUserIdToAppendEnd(userId, followingUserId));
        Assert.assertEquals(1,
                userActionDAO.updateFollowedUserIdJsonArrayByOneUserIdToAppendEnd(userId, followedUserId));
        System.out.println("success pass update to add following, followed user test!");
    }

    /**
     * 测试更新用户行为，JSON 数组根据索引，删除元素
     *      - 喜欢话题 id
     *      - 收藏话题 id
     *      - 关注话题 id
     *      - 主动关注用户 id
     *      - 被关注用户 id
     */
    @Test
    @Transactional
    public void testUpdateUserActionByIndexRemoveElement() {
        UserActionDO userAction = this.saveTestUserActionDOToDatabase();

        //add element
        int userId = userAction.getUserId();
        int topicId = 1;
        int followingUserId = 2;
        int followedUserId = 3;
        Assert.assertEquals(1, userActionDAO.updateLikeTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId));
        Assert.assertEquals(1, userActionDAO.updateCollectTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId));
        Assert.assertEquals(1, userActionDAO.updateAttentionTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId));
        Assert.assertEquals(1,
                userActionDAO.updateFollowingUserIdJsonArrayByOneUserIdToAppendEnd(userId, followingUserId));
        Assert.assertEquals(1,
                userActionDAO.updateFollowedUserIdJsonArrayByOneUserIdToAppendEnd(userId, followedUserId));

        System.out.println("before remove: " + userActionDAO.getUserAction(userId));

        //remove element
        int indexOfTopicId = 0;
        int indexOfFollowingUserId = 0;
        int indexOfFollowedUserId = 0;
        Assert.assertEquals(1, userActionDAO.updateLikeTopicIdJsonArrayByIndexToRemoveOneTopicId(userId, indexOfTopicId));
        Assert.assertEquals(1,
                userActionDAO.updateCollectTopicIdJsonArrayByIndexToRemoveOneTopicId(userId, indexOfTopicId));
        Assert.assertEquals(1,
                userActionDAO.updateAttentionTopicIdJsonArrayByIndexToRemoveOneTopicId(userId, indexOfTopicId));
        System.out.println("success pass update to remove like, collect, attention topic test!");

        Assert.assertEquals(1,
                userActionDAO.updateFollowingUserIdJsonArrayByIndexToRemoveOneUserId(userId, indexOfFollowingUserId));
        Assert.assertEquals(1,
                userActionDAO.updateFollowedUserIdJsonArrayByIndexToRemoveOneUserId(userId, indexOfFollowedUserId));
        System.out.println("success pass update to remove following, followed user test!");

        System.out.println("after remove: " + userActionDAO.getUserAction(userId));
    }

}
