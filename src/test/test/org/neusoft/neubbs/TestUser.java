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
 *  测试腾讯云服务器neubbs的数据库表,增删查改
 *
 * @Author Suvan
 * @Date 2017-9-21
 */
@RunWith(SpringJUnit4ClassRunner.class)		//表示继承了SpringJUnit4ClassRunner类[配置spring和junit整合，junit启动时加载springIOC容器 spring-test,junit]
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestUser {

    private static Logger logger = Logger.getLogger(TestUser.class); //将此类加入日志

    @Resource
    IUserService userService;



    @Test/*测试插入用户*/
    public void testInsertUser(){
        User user = new User();
            user.setName("论坛测试用户");
            user.setPassword("12345");
            user.setEmail("test@test.com");

        String result = userService.insertUser(user);
        logger.info(result + "----"+JSON.toJSONString(user));  //打印语句
    }



    @Test /*测试删除用户*/
    public void testDeleteUser(){
        String result = userService.deleteUserById(1);

        logger.info(result);
    }


    @Test  /*测试id查询用户*/
    public void testSelectUser(){
        User user = userService.selectUserById(1);
        logger.info("查询数据:"+JSON.toJSONString(user));
    }
    @Test  /*测试name查询用户*/
    public void testSelectUser2(){
        User user = userService.selectUserByName("liushuwei");
        logger.info("查询数据:"+JSON.toJSONString(user));
    }
    @Test  /*测试查询用户名唯一性*/
    public void testSelectOnlyUser(){
        String result = userService.selectByNameOnlyUser("SUVAN");
        logger.info("查询结果 - >" + result);
    }


    @Test /*测试更新用户*/
    public void testUpdateUser(){
        //1.id查询
        User newUser = userService.selectUserById(2);
            newUser.setName("瓜皮哦");
            newUser.setPassword("123123124");

        //2.进行更新
        String result = userService.updateUser(newUser);

        logger.info(result);
    }
}
