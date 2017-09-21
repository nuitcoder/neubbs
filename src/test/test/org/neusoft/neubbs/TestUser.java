package test.org.neusoft.neubbs;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.entity.User;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 *  测试腾讯云服务器 neubbs 的数据库表
 */
@RunWith(SpringJUnit4ClassRunner.class)		//表示继承了SpringJUnit4ClassRunner类[配置spring和junit整合，junit启动时加载springIOC容器 spring-test,junit]
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestUser {

    private static Logger logger = Logger.getLogger(TestUser.class);

    @Resource
    IUserService userService;


    /**
     * 测试插入用户
     */
    @Test
    public void testInsertUser(){
        User user = new User();
            user.setName("论坛测试用户");
            user.setPassword("12345");
            user.setEmail("test@test.com");

        String result = userService.insertUser(user);
        logger.info(result + "----" + JSON.toJSONString(user));  //打印语句
    }

    /**
     * 测试删除用户
     */
    @Test
    public void testDeleteUser(){
        String result = userService.deleteUserById(1);

        logger.info(result);
    }

    /**
     * 测试id查询用户
     */
    @Test
    public void testSelectUser(){
        User user = userService.selectUserById(1);
        logger.info("查询数据:"+JSON.toJSONString(user));
    }

    /**
     * 测试name查询用户
     */
    @Test
    public void testSelectUser2(){
        User user = userService.selectUserByName("liushuwei");
        logger.info("查询数据:"+JSON.toJSONString(user));
    }

    /**
     * 测试查询用户名唯一性
     */
    @Test
    public void testSelectOneUsernameByName(){
        String result = userService.selectOneUsernameByName("SUVAN");
        logger.info("查询结果 - >" + result);
    }

    /**
     * 测试更新用户
     */
    @Test
    public void testUpdateUser(){
        // ID 查询
        User newUser = userService.selectUserById(2);
        newUser.setName("Username");
        newUser.setPassword("Password");

        // 进行更新
        String result = userService.updateUser(newUser);
        logger.info(result);
    }
}
