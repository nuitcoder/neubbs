package test.org.neusoft.neubbs.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.dao.IUserDynamicDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.entity.UserDynamicDO;
import org.neusoft.neubbs.utils.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

/**
 * 测试 IUserDynamicDAO 接口
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class UserDynamicDAOTest {

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IUserDynamicDAO userDynamicDAO;

    /**
     * 保存测试 UserDynamicDO 对象至数据库
     *      - 新建测试用户动态
     *      - 保存，且重新查询数据库获得对象（根据用户 id，进行查询）
     *
     * @return UserDynamicDO 用户动态对象
     */
    private UserDynamicDO saveTestUserDynamicDOToDatabase() {
        UserDO user = new UserDO();
            user.setName("testUser");
            user.setPassword(SecretUtil.encryptUserPassword("123456"));
            user.setEmail("testUser@neubbs.com");

        Assert.assertEquals(1, userDAO.saveUser(user));

        UserDynamicDO userDynamic = new UserDynamicDO();
            userDynamic.setUserId(user.getId());
        Assert.assertEquals(1, userDynamicDAO.saveUserDynamic(userDynamic));

        return userDynamicDAO.getUserDynamic(user.getId());
    }


    @BeforeClass
    public static void init() {
        //set local database source
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 测试保存用户动态
     */
    @Test
    @Transactional
    public void testSaveUserDynamic() {
        UserDynamicDO userDynamic = this.saveTestUserDynamicDOToDatabase();
        System.out.println("success save userId=" + userDynamic.getUserId()
                + " user dynamic information: "+ userDynamic);
    }

    /**
     * 测试获取用户动态
     */
    @Test
    @Transactional
    public void testGetUserDynamic() {
        UserDynamicDO userDynamic = this.saveTestUserDynamicDOToDatabase();
        System.out.println("success get userId=" + userDynamic.getUserId()
                + " user dynamic information: " + userDynamic);
    }

    /**
     * 测试发布用户动态信息 JSON 数组（JSON 数组，末尾追加）
     */
    @Test
    @Transactional
    public void testUpdatePublicInfoJsonArrayByOneDynamicInforToAppendEnd() {
        UserDynamicDO userDynamic = this.saveTestUserDynamicDOToDatabase();
        int userId = userDynamic.getUserId();
        System.out.println("before append: " + userDynamic);

        //build user dynamic json information:
        JSONArray jsonUserDynamic = JSON.parseArray(userDynamic.getPublicInfoJsonArray());
        int currentJsonLength = jsonUserDynamic.size();

        JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", currentJsonLength + 1);
            jsonObject.put("createTime", System.currentTimeMillis());
            jsonObject.put("content", "today very happy");
            jsonObject.put("visibilityType", "myself");

        Assert.assertEquals(1,
                userDynamicDAO.updatePublicInfoJsonArrayByOneDynamicInfoToAppendEnd(
                        userId, jsonObject.toJSONString()
                )
        );

        UserDynamicDO afterUserDynamic = userDynamicDAO.getUserDynamic(userId);
        Assert.assertEquals(1, JSON.parseArray(afterUserDynamic.getPublicInfoJsonArray()).size());
        System.out.println("after append: " + afterUserDynamic);

        System.out.println("update user dynamic append on information success!");
    }

    /**
     * 测试删除用户动态信息（JSON 数组，根据索引，删除指定元素）
     */
    @Test
    @Transactional
    public void testUpdatePublicInfoJsonArrayByIndexToRemoveOneDynamicInfo() {
        UserDynamicDO userDynamic = this.saveTestUserDynamicDOToDatabase();
        int userId = userDynamic.getUserId();

        int id = 1;
        JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("createTime", System.currentTimeMillis());
            jsonObject.put("content", "today very happy");
            jsonObject.put("visibilityType", "myself");

        Assert.assertEquals(1,
                userDynamicDAO.updatePublicInfoJsonArrayByOneDynamicInfoToAppendEnd(
                        userId, jsonObject.toJSONString()
                )
        );

        System.out.println("before remove: " + userDynamicDAO.getUserDynamic(userId));
        int removeIndex = id - 1;
        Assert.assertEquals(1,
                userDynamicDAO.updatePublicInfoJsonArrayByIndexToRemoveOneDynamicInfo(userId, removeIndex)
        );

        UserDynamicDO afterUserDynamic = userDynamicDAO.getUserDynamic(userId);
        Assert.assertEquals(0, JSON.parseArray(afterUserDynamic.getPublicInfoJsonArray()).size());
        System.out.println("after remove: " + afterUserDynamic);

        System.out.println("update user dynamic remove index information success!");
    }
}

