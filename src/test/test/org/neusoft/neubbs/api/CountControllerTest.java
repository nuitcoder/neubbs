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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
        //current executing mehtod
        String currentDoingMethod = Thread.currentThread().getStackTrace()[2].getMethodName();

        System.out.println("*************************** 【"
                + currentDoingMethod + "()】 "
                + " pass all the tests ****************************");
    }

    @BeforeClass
    public static void init() {
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    @Before
    public void setup() {
        //set servlet context online visit user and loin user
        ServletContext context = webApplicationContext.getServletContext();
            context.setAttribute(ParamConst.VISIT_USER, 21);
            context.setAttribute(ParamConst.LOGIN_USER, 50);

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
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
        .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.model." + ParamConst.VISIT_USER).value(21));

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
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.loginUser").value(50));

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/count/user】 test count user totals success
     */
    @Test
    public void testCountUserTotalsSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/count/user")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.userTotals").exists()).andDo(MockMvcResultHandlers.print());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/count/topic】 test count topic totals success
     */
    @Test
    public void testCountTopicTotalsSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/count/topic")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.topicTotals").exists());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/count/reply】 test count topic reply totals success
     */
    @Test
    public void testCountTopicReplyTotalsSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/count/reply")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.replyTotals").exists());

        printSuccessPassTestMehtodMessage();
    }
}
