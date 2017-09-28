package test.org.neusoft.neubbs;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *  neubbs:forum_user   TestCase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class ForumUserTestCase {

    private static Logger logger = Logger.getLogger(ForumUserTestCase.class);

    @Autowired
    IUserService userService;


    /**
     * 测试插入用户
     */
    @Test
    public void testSaveUserByUser(){
        UserDO user = new UserDO();
        user.setName("oneuser");
        user.setPassword("12345");
        user.setEmail("test@test.com");

        String result = userService.saveUserByUser(user);
        logger.info(result + " => " + JSON.toJSONString(user));  //打印语句
    }

    /**
     * 测试id查询用户
     */
    @Test
    public void testgetUserById(){
        UserDO user = userService.getUserById(1);
        logger.info(JSON.toJSONString(user));
    }

    /**
     * 测试name查询用户
     */
    @Test
    public void testgetUserByName(){
        UserDO user = userService.getUserByName("oneuser");
        logger.info(JSON.toJSONString(user));
    }

    /**
     * 测试更新用户
     */
    @Test
    public void testUpdateUserByUser(){
        // ID 查询
        UserDO user = userService.getUserById(1);
        user.setName("TestUpdateUser");
        user.setPassword("Password");

        // 进行更新
        String result = userService.updateUserByUser(user);
        logger.info(result + " => " + JSON.toJSONString(user));
    }

    /**
     * 测试删除用户
     */
    @Test
    public void testRemoveUserById(){
        String result = userService.removeUserById(1);

        logger.info(result);
    }


    /**
     * 测试删减表
     */
    @Ignore
    public void truncateUserTable(){
        UserDO user1 = new UserDO();
        user1.setName("小明");
        user1.setPassword("12345");
        user1.setEmail("test@test.com");
        UserDO user2 = new UserDO();
        user2.setName("小红");
        user2.setPassword("12345");
        user2.setEmail("test@test.com");

        userService.saveUserByUser(user1);
        userService.truncateUserTable("forum_user");
        userService.saveUserByUser(user2);

        UserDO user = userService.getUserById(1);
        if("小红".equals(user.getName())){
            logger.info("删减成功,删减后id为1的用户,为添加后的小红！");
        }else{
            logger.info("删减失败！");
        }

    }
}
