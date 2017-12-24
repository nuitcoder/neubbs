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
     * 【/api/count】 test count totals success
     *      - user totals
     *      - topic totals
     *      - reply totals
     */
    @Test
    public void testCountTotalsSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/count")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.user").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.topic").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.reply").exists());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/count/visit】test count online count success
     *      - visit user
     *      - login user
     */
    @Test
    public void testCountOnlineCountSuccess() throws Exception {
       mockMvc.perform(
               MockMvcRequestBuilders.get("/api/count/online")
       ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
        .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.model." + ParamConst.VISIT_USER).value(21))
        .andExpect(MockMvcResultMatchers.jsonPath("$.model." + ParamConst.LOGIN_USER).value(50));

       printSuccessPassTestMehtodMessage();
    }
}
