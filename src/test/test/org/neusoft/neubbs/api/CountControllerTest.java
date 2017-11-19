package test.org.neusoft.neubbs.api;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Count api 测试
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-context.xml"}),
        @ContextConfiguration(locations = {"classpath:spring-mvc.xml"})
})
public class CountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    /**
     * 打印成功通过 Test 函数消息
     */
    public void printSuccessPassTestMehtodMessage() {
        //当前正在执行函数（调用此函数的函数）
        String currentDoingMethod = Thread.currentThread().getStackTrace()[2].getMethodName();

        System.out.println("*************************** 【" + currentDoingMethod + "()】 "
                + "pass all the tests ****************************");
    }

    @BeforeClass
    public static void init() {
        //本地数据库
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    @Before
    public void setup() {
        //设置在线访问人数 + 在线登录人数
        ServletContext context = webApplicationContext.getServletContext();
            context.setAttribute(ParamConst.COUNT_VISIT_USER, 21);
            context.setAttribute(ParamConst.COUNT_LOGIN_USER, 50);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 【/api/count/visit】test count online visit users
     */
    @Test
    public void testCountOnlineVisitUsers() throws Exception {
       mockMvc.perform(
               MockMvcRequestBuilders.get("/api/count/visit")
       ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
        .andExpect(MockMvcResultMatchers.jsonPath("$.model." + ParamConst.COUNT_VISIT_USER).value(21));

       printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/count/login】 test count online login users
     */
    @Test
    public void testCountOnlineLoginUsers() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/count/login")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model." + ParamConst.COUNT_LOGIN_USER).value(50));

        printSuccessPassTestMehtodMessage();
    }
}
