package test.org.neusoft.neubbs.api;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.service.ITopicService;
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
                .andExpect(jsonPath(model + "user.image").value(CoreMatchers.nullValue()))
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
}
