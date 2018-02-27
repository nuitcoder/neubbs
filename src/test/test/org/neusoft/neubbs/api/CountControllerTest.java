package test.org.neusoft.neubbs.api;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.filter.ApiFilter;
import org.neusoft.neubbs.controller.handler.DynamicSwitchDataSourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Count api 测试
 *      - 测试 /api/count
 *      - 测试 /api/count/online
 *      - 测试 /api/count/user
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

    private ApiTestUtil util;

    /*
     * ***********************************************
     * init method
     * ***********************************************
     */

    @BeforeClass
    public static void init() {
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    @Before
    public void setup() {
        //set web application attribute loginUser = 50
        this.webApplicationContext.getServletContext().setAttribute(ParamConst.LOGIN_USER, 50);

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .addFilter(new ApiFilter())
                .build();

        this.util = ApiTestUtil.getInstance(this.mockMvc);
    }

    /*
     * ***********************************************
     * api test method
     * ***********************************************
     */

    /**
     * 测试 /api/count
     *      - 测试论坛基数统计成功
     *          - user totals
     *          - topic totals
     *          - reply totals
     */
    @Test
    public void testCountForumBasicDataSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/count")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.user").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.topic").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.reply").exists());

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/count/online
     *      - 测试在线统计成功
     *          - login user
     */
    @Test
    public void testCountOnlineDataSuccess() throws Exception {
        //@Before already set application attribute loginUser = 50
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/count/online")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model." + ParamConst.LOGIN_USER).value(50))
        .andDo(MockMvcResultHandlers.print());

       util.printSuccessMessage();
    }

    /**
     * 测试 /api/count/user
     *      - 测试用户统计成功
     *          - following
     *          - followed
     *          - like
     *          - collect
     *          - attention
     *          - topic
     *          - reply
     */
    @Test
    public void testCountUserData() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/count/user")
                        .param("userid", "6")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        util.isExistKeyItems(mvcResult, "following", "followed", "like", "collect", "attention", "topic", "reply");

        util.printSuccessMessage();
    }
}
