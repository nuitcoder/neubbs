package test.org.neusoft.neubbs.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
            userAction.setUserid(user.getId());

        Assert.assertEquals(1, userActionDAO.saveUserAction(userAction));

        return userActionDAO.getUserAction(userAction.getUserid());
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
     * 测试更新用户喜欢话题 id 数组
     */
    @Test
    @Transactional
    public void testUpdateLikeTopicArrayJson() {
        UserActionDO userAction = this.saveTestUserActionDOToDatabase();

        int userId = userAction.getUserid();
        String likeTopicIdArrayJson = JsonUtil.toJSONStringByObject(new int[]{1, 2, 3, 4, 5});
        System.out.println("input[userid=" + userAction + ", likeTopicIdArrayJson=" + likeTopicIdArrayJson + "]");

        Assert.assertEquals(1, userActionDAO.updateLikeTopicIdJsonArray(userId, likeTopicIdArrayJson));

        System.out.println("update userid=" + userId
                + " user action likeTopicIdArrayJson=<" + likeTopicIdArrayJson + "> success!");
    }

    /**
     * （JSON 数组末尾，追加 topicid）测试更新用户喜欢话题 id 数组
     */
    @Test
    @Transactional
    public void testUpdateLikeTopicIdJsonArrayByOneTopicIdToAppendEnd() {
        UserActionDO userAction = this.saveTestUserActionDOToDatabase();

        int userId = userAction.getUserid();
        int topicId = 12;
        System.out.println("intput[userid=" + userId + ", topicid=" + topicId + "]");

        Assert.assertEquals(1, userActionDAO.updateLikeTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId));

        String afterLikeTopicJsonArray = userActionDAO.getUserAction(userId).getLikeTopicidJsonArray();
        JSONArray topicIdArray = JSON.parseArray(afterLikeTopicJsonArray);
        Assert.assertEquals(topicId, topicIdArray.get(topicIdArray.size() - 1));

        System.out.println("update userid=" + userId
                + " user action append end topicid=<" + topicId + "> success!");
    }

    /**
     * (根据索引，删除 JSON 数组中指定元素)测试更新用户喜欢的话题 id 数组
     */
    @Test
    @Transactional
    public void testUpdateLikeTopicIdJsonArrayRemoveOneTopicIdByIndex() {
        UserActionDO userAction = this.saveTestUserActionDOToDatabase();

        int userId = userAction.getUserid();
        int topicId = 1;
        Assert.assertEquals(1, userActionDAO.updateLikeTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId));

        int indexOfTopicId = 0;
        System.out.println("input[userid=" + userId + ", indexOfTopicId=" + topicId + "]");
        Assert.assertEquals(1, userActionDAO.updateLikeTopicIdJsonArrayRemoveOneTopicIdByIndex(userId, indexOfTopicId));

        String afterLikeTopicJsonArray = userActionDAO.getUserAction(userId).getLikeTopicidJsonArray();
        JSONArray topicIdArray = JSON.parseArray(afterLikeTopicJsonArray);
        Assert.assertEquals(0, topicIdArray.size());

        System.out.println("update userid=" + userId
                + "user action json array remove index=<" + indexOfTopicId + "> element success!");
    }
}
