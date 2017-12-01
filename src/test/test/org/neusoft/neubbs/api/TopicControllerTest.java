package test.org.neusoft.neubbs.api;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Topic api 测试
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-context.xml"}),
        @ContextConfiguration(locations = {"classpath:spring-mvc.xml"})
})
public class TopicControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ITopicService topicService;

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
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 【/api/topic】 test get topic information success
     */
    @Test
    public void testGetTopicInformationSuccess() throws Exception {
        String topicId = "1";
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic").param("topicid", topicId)
        ).andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.model").exists())
            .andExpect(jsonPath("$.model.topicid").value(Integer.parseInt(topicId)))
            .andExpect(jsonPath("$.model.user").exists())
            .andExpect(jsonPath("$.model.lastreplyuser").exists())
            .andExpect(jsonPath("$.model.replys").exists());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic】 test get topic information throw exception
     */
    @Test
    public void testGetTopicInformationThrowException() throws Exception{
        //check length, format, data exist
        String[] params = {
             null, "1111111111111111", "asdfa*1-", "10000"
        };

        for (String param: params) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topic").param("topicid", param)
                ).andExpect(jsonPath("$.success").value(false))
                 .andExpect(jsonPath("$.message").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(TopicErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class)));
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/reply】 test get topic reply information success
     */
    @Test
    public void testGetTopicReplyInformationSuccess() throws Exception {
        String replyId = "1";

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic/reply")
                    .param("replyid", replyId)
        ).andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.message").doesNotExist())
         .andExpect(jsonPath("$.model").exists());

        String model = "$.model.";
        result.andExpect(jsonPath(model + "content").exists())
                .andExpect(jsonPath(model + "agree").exists())
                .andExpect(jsonPath(model + "oppose").exists())
                .andExpect(jsonPath(model + "createtime").exists())
                .andExpect(jsonPath(model + "replyid").value(Integer.parseInt(replyId)))
                .andExpect(jsonPath(model + "user.avator").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath(model + "user.username").exists());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/reply】 test get topic reply information throw exception
     */
    @Test
    public void testGetTopicReplyInformationThrowException() throws Exception {
        String[] params = {null, "*12+", "abc", "11111111111", "11123k3k"};

        for (String param: params) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topic/reply")
                            .param("replyid", param)
                ).andExpect(jsonPath("$.success").value(false))
                 .andExpect(jsonPath("$.message").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class))
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topics/new】test get topic list information by page and limit success
     *      - the optional param limit, neubbs.properties hava default value
     *      - input limit,page
     *      - input limit,page,category
     */
    @Test
    public void testGetTopicListInformationByPageAndLimitSuccess() throws Exception {
        //only input limit and page
        int[][] params = {
                {20, 3}, {2, 20}, {43, 2}, {3}
        };

        for (int[] param: params) {
            String limit;
            String page;
            ResultActions result;
            if (param.length == 1) {
                page = String.valueOf(param[0]);
                System.out.print("input param[ page=" + page + " ]");

                result = mockMvc.perform(MockMvcRequestBuilders.get("/api/topics").param("page", page));
            } else {
                limit = String.valueOf(param[0]);
                page = String.valueOf(param[1]);
                System.out.print("input param[ limit=" + limit + "; page=" + page + " ]");

                result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topics")
                                .param("limit", limit)
                                .param("page", page)
                );
            }

            result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            //get model topic list
            Map<String, Object> rootMap = JsonUtil.toMapByJSONString(result.andReturn().getResponse().getContentAsString());
            ArrayList<Map<String, Object>> modelList = (ArrayList<Map<String, Object>>)rootMap.get("model");
            Map<String, Object> firstModelMap = modelList.get(0);

            //model topic map exist assign key item,$.model[0]
            Assert.assertThat(firstModelMap.keySet(),
                    CoreMatchers.hasItems("category","title","replies", "createtime","topicid",
                            "read", "agree","user", "lastreplyuser")
            );

            //$.model[0].user
            Map<String, Object> userMap = (Map<String, Object>) firstModelMap.get("user");
            Assert.assertThat(userMap.keySet(),
                    CoreMatchers.allOf(
                        CoreMatchers.hasItem("avator"),CoreMatchers.hasItem("username")
                    )
            );

            //$.model[0].lastreplyuser
            Map<String, Object> lastReplyUserMap = (Map<String, Object>) firstModelMap.get("lastreplyuser");
            Assert.assertArrayEquals(
                    lastReplyUserMap.keySet().toArray(), new String[]{"username", "avator"}
            );

            System.out.println("\t<test result: success!>");
        }


        //input param: limit,page,category
        String category = "分类 2";
        System.out.println("input param[ limit=1, page=1,category=" + category + "]");
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/topics")
                .param("limit", "1")
                .param("page", "1")
                .param("category", category)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model[0].category").doesNotExist());

        //input param: limit,page,username
        String username = "suvan";
        System.out.println("input param[ limit=1, page=1,username=" + username + "]");
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics")
                    .param("limit", "1")
                    .param("page", "1")
                    .param("username", "suvan")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model[0].user").doesNotExist());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topics/new】test get topic list information by page and limit throw exception
     *      - the optional param limit, neubbs.properties hava default value
     */
    @Test
    public void testGetTopicListInformationByPageAndLimitThrowException() throws Exception {
        //page and limit, two param
        String[][] params = {
                {null, null}, {"1", null},
                {"1111111111111", "213"}, {"12", "1111111111111"}, {"11111111111111", "1111111111"},
                {"*-+", "1"}, {"1", "-=="}, {"-&&*%", "****("}, {"asdfasdf*123", "___jhjh123"},
                {"10000","1", "23", "10000", "10000", "10000"}
        };

        for (String[] param: params) {
            String limit = param[0];
            String page = param[1];
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topics")
                            .param("limit", limit).param("page", page)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(CoreMatchers.is("false")))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CoreMatchers.notNullValue()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.nullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(TopicErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class)
                        )
                );
            }
        }

        //only page param
        String[] pages = {
                null, "***", "asdfasd", "*fasf", "asdfa123**(", "*123",
                "11111111111111111",
                "10000"
        };
        for (String page: pages) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topics").param("page", page)
                ).andExpect(jsonPath("$.success").value(false))
                 .andExpect(jsonPath("$.message").exists())
                 .andExpect(jsonPath("$.model").doesNotExist());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        anyOf(instanceOf(ParamsErrorException.class),
                                instanceOf(TopicErrorException.class),
                                instanceOf(AccountErrorException.class)
                        )
                );
            }
        }

        //the same input category and username
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/topics")
                            .param("page", "1")
                            .param("limit", "1")
                            .param("category", "fasdf")
                            .param("username", "asdf")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.PARAM_ERROR));
        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof TopicErrorException);
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/categorys】 test get topic category list success
     */
    @Test
    public void testGetTopicCategoryListSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic/categorys")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.categorys").exists());

        printSuccessPassTestMehtodMessage();
    }
}
