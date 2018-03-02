package test.org.neusoft.neubbs.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.filter.ApiFilter;
import org.neusoft.neubbs.controller.handler.DynamicSwitchDataSourceHandler;
import org.neusoft.neubbs.dao.ITopicActionDAO;
import org.neusoft.neubbs.dao.ITopicCategoryDAO;
import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.dao.IUserActionDAO;
import org.neusoft.neubbs.entity.TopicCategoryDO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.exception.ParamsErrorException;
import org.neusoft.neubbs.exception.ServiceException;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

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

    private ApiTestUtil util;

    @Autowired
    private IUserActionDAO userActionDAO;

    @Autowired
    private ITopicActionDAO topicActionDAO;

    @Autowired
    private ITopicDAO topicDAO;

    @Autowired
    private ITopicContentDAO topicContentDAO;

    @Autowired
    private ITopicCategoryDAO topicCategoryDAO;

    @Autowired
    private ITopicReplyDAO topicReplyDAO;

    @Autowired
    private ITopicService topicService;

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
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .addFilter(new ApiFilter())
                .build();

        this.util = ApiTestUtil.getInstance(mockMvc);
    }

    /*
     * ***********************************************
     * api test method
     * ***********************************************
     */

    /**
     * 测试 /api/topic （GET）
     *      - 获取话题信息接口成功
     *          - 阅读数增加
     *          - 当前登陆 cookie 用户 'suvan' 并未点赞过 topicId = 1 的话题，所以期望 isliketopic = false
     */
    @Test
    @Transactional
    public void testGetTopicInfoSuccessOfAddRead() throws Exception {
        String topicId = "1";

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic")
                        .cookie(util.getAlreadyLoginUserCookie())
                        .param("topicid", topicId)
                        .param("hadread", "1")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.isliketopic").value(false))
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //judge $.model
        Map modelMap = (Map) resultMap.get("model");
        util.confirmMapShouldHavaKeyItems(modelMap,
                "title", "replies", "lastreplytime", "createtime", "topicid" ,"content",
                "read", "like", "category", "user", "lastreplyuser", "replylist", "isliketopic");

        //judge $.model.category
        util.confirmMapShouldHavaKeyItems((Map) modelMap.get("category"), "id", "name", "description");

        //judge $.model.user
        util.confirmMapShouldHavaKeyItems((Map) modelMap.get("user"), "avator", "username");

        //judge $.model.lastreplyuser
        util.confirmMapShouldHavaKeyItems((Map) modelMap.get("lastreplyuser"), "avator", "username");

        //judge $.model.replylist
        List modelReplyList = (List) modelMap.get("replylist");
        if (modelReplyList.size() > 0) {
            //get first reply map
            Map firstReplyMap = (Map) modelReplyList.get(0);
            util.confirmMapShouldHavaKeyItems(firstReplyMap,
                    "topicid", "content", "agree", "oppose", "createtime", "replyid", "user");

            //judge $.model.replys[0].user
            util.confirmMapShouldHavaKeyItems( (Map) firstReplyMap.get("user"), "avator", "username");
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic （GET）
     *      - 测试获取话题信息成功
     *          - 阅读数不增加
     *          - 访客用户默认不喜欢话题（若用户已登陆，则要判断用户是否喜欢话题）
     */
    @Test
    @Transactional
    public void testGetTopicInfoSuccessOfNotAddRead() throws Exception {
        String topicId = "1";
        int beforeTopicRead = topicContentDAO.getTopicContentByTopicId(Integer.parseInt(topicId)).getRead();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic")
                        .param("topicid", topicId)
                        .param("hadread", "0")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.isliketopic").value(false));

        //compare before and after read the same
        int afterTopicRead = topicContentDAO.getTopicContentByTopicId(Integer.parseInt(topicId)).getRead();
        Assert.assertEquals(beforeTopicRead, afterTopicRead);

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic （GET）
     *      - 获取话题信息异常
     *          - [✔] request param error, no norm
     *              - null
     *              - topicid error
     *          - [✔] service exception
     *              - no topic
     */
    @Test
    public void testGetTopicInfoException() throws Exception{
        //request param error
        String[] params = {null, "1111111111111111", "abc123*", "10000000"};

        for (String topicId: params) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topic")
                                .param("topicid", topicId)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic/reply （GET）
     *      - 获取回复信息成功
     */
    @Test
    public void getTopicReplyInfoSuccess() throws Exception {
        String replyId = "1";

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic/reply")
                        .param("replyid", replyId)
        ).andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //judge $.model
        Map modelMap = (Map) resultMap.get("model");
        util.confirmMapShouldHavaKeyItems(modelMap,
                "topicid", "content", "agree", "oppose", "createtime", "replyid", "user");

        //judge $.model.user
        util.confirmMapShouldHavaKeyItems((Map) modelMap.get("user"), "avator", "username");

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic/reply （GET）
     *      - 获取回复信息异常
     *          - [✔] request param error, no param
     *          - [✔] service exception
     *              - no reply
     */
    @Test
    public void testGetTopicReplyInfoException() throws Exception {
        String[] params = {null, "*123", "abc", "11111111111", "10000000"};

        for (String replyId: params) {
            System.out.println("input replyid = " + replyId);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topic/reply")
                                .param("replyid", replyId)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topics/hot
     *      - 获取热议话题列表成功
     */
    @Test
    public void testListTopicInfoSuccess() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics/hot")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()))
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());
        List modelList = (List) resultMap.get("model");
        Assert.assertEquals(10, modelList.size());

        //judge $.mode.[0]
        Map firstModelListMap = (Map) modelList.get(0);
        util.confirmMapShouldHavaKeyItems(firstModelListMap,
                "title", "replies", "lastreplytime", "createtime", "topicid", "category", "user", "lastreplyuser");

        //judge $.mode.category
        util.confirmMapShouldHavaKeyItems((Map) firstModelListMap.get("category"), "id", "name", "description");

        //judge $.model.user
        util.confirmMapShouldHavaKeyItems((Map) firstModelListMap.get("user"), "avator", "username");

        //$judge $.model.lastreplyuser
        util.confirmMapShouldHavaKeyItems((Map) firstModelListMap.get("lastreplyuser"), "avator", "username");

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topics
     *      - 获取热议话题成功
     *          - input param test
     *              - [ ] input page
     *              - [✔] input limit, page
     *              - [ ] input page, category
     *              - [ ] input limit, page category
     *              - [ ] input page, category
     *              - [ ] input limit, page, username
     *              - [ ] input page, username
     *              - [ ] input limit, page, category, username
     *              - [ ] input page, category, username
     *              - [ ] input limit, page, category, username
     */
    @Test
    public void testGetTopicListBasicInformationByFilterParamsSuccess() throws Exception {
        String apiUrl = "/api/topics";

        //input limit, page
        String limit = "1";
        String page = "1";
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl)
                        .param("limit", limit)
                        .param("page", page)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //judge $.model, because limit=1, so assert list.size()
        List modelList = (List) resultMap.get("model");
        Assert.assertEquals(1, modelList.size());

        Map firstModelListMap = (Map) modelList.get(0);

        util.confirmMapShouldHavaKeyItems(firstModelListMap,
                "title", "replies", "lastreplytime", "createtime", "topicid",
                "content", "read", "like", "category", "user", "lastreplyuser");

        //judge $.mode.category
        util.confirmMapShouldHavaKeyItems((Map) firstModelListMap.get("category"), "id", "name", "description");

        //judge $.model.user
        util.confirmMapShouldHavaKeyItems((Map) firstModelListMap.get("user"), "username", "avator");

        //$judge $.model.lastreplyuser
        util.confirmMapShouldHavaKeyItems((Map) firstModelListMap.get("lastreplyuser"), "username", "avator");

        util.printSuccessMessage();
    }

    /**
     * 【/api/topics】test get topic list information by page and limit throw exception
     *      - request param eroorn, no norm
     *      - database exception
     *          - [✔] input page exceed topic max page
     *          - [ ] no query topics
     *          - [ ] no category
     *          - [ ] no username
     */
    @Test
    public void testGetTopicListInformationByPageAndLimitThrowException() throws Exception {
        //page and limit, two param
        String[][] params = {
                {null, null}, {"1", null},
                {"1111111111111", "213"}, {"12", "1111111111111"}, {"11111111111111", "1111111111"},
                {"*-+", "1"}, {"1", "-=="}, {"-&&*%", "****("}, {"asdfasdf*123", "___jhjh123"},
                {"10000","1"}
        };

        for (String[] param: params) {
            String limit = param[0];
            String page = param[1];
            System.out.println("input param: limit=" + limit + ", page=" + page);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topics")
                                .param("limit", limit)
                                .param("page", page)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(CoreMatchers.is("false")))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CoreMatchers.notNullValue()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 【/api/topics/pages】test get topic total pages success
     *      - if no input limit param, neubbs.properties hava default value
     *
     *      - input param test
     *          - [✔] no input (default limit = 25)
     *          - [✔] input limit,
     *          - [✔] input category
     *          - [ ] input limit, category
     *          - [✔] input username
     *          - [ ] input limit, username
     *          - [✔] input category, username
     *          - [ ] input limit, category, username
     */
    @Test
    public void testGetTopicTotalPagesSuccess() throws Exception {
        String apiUrl = "/api/topics/pages";

        String limit;
        String category;
        String username;

        //no input, use default limit = 25
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());


        //input limit
        limit = "5";
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl).param("limit", limit)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input category
        category = "game";
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl).param("category", category)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input username
        username = "suvan";
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl).param("username", username)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input category,username
        category = "game";
        username = "suvan";
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl)
                        .param("category", category)
                        .param("username", username)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        util.printSuccessMessage();
    }

    /**
     * 【/api/topics/pages】test get topic total pages throw exception
     *      - prompt: if throw exception, will no to do .andExpect(...)
     *
     *  - [ ] reqeust param error, no norm
     *  - [ ] database exception
     *          - [✔] no category
     *          - [✔] no username
     */
    @Test
    public void testGetTopicTotalPagesThrowException() throws Exception {
        String apiUrl = "/api/topics/pages";

        //input category, but category no exist
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get(apiUrl).param("category", "noExistCategory")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_CATEGORY))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof ServiceException);
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_CATEGORY);
        }

        //input username, but username no exist
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get(apiUrl).param("username", "noExistUser")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("asd"))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof ServiceException);
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_USER);
        }

        util.printSuccessMessage();
    }

    /**
     * 【/api/topic/categorys】 test get topic category list success
     */
    @Test
    public void testGetTopicCategoryListSuccess() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics/categorys")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //judge $.model, is list
        List modelList = (List) resultMap.get("model");

        //judge $.model[0], is map
        Map firstModelListCategoryMap = (Map) modelList.get(1);
        util.confirmMapShouldHavaKeyItems(firstModelListCategoryMap, "id", "name", "description");

        util.printSuccessMessage();
    }

    /**
     * 【/api/topic (http post)】 test publish topic success
     *      - get user cookie
     *          - already login
     *          - account activated
     */
    @Test
    @Transactional
    public void testPublishTopicSuccess() throws Exception {
        String categoryNick = "game";
        String title = "new topic title";
        String content = "new topic Contentn";

        String requestBodyJson = "{\"category\":\"" + categoryNick + "\","
                + "\"title\":\"" + title + "\","
                + "\"content\":\"" + content + "\"}";
        System.out.println("input request-body: " + requestBodyJson);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic")
                        .cookie(util.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //jduge $.model.topicid
        Map modelMap = (Map) resultMap.get("model");
        int topicId = (Integer) modelMap.get("topicid");
        Assert.assertTrue(topicId >= 0);
        System.out.println("new insert test topic, topicid = " + topicId);

        //compare database topic information
        TopicDO topic = topicDAO.getTopicById(topicId);
        int categoryId = topic.getCategoryid();
        Assert.assertNotNull(topic);
        Assert.assertEquals(categoryNick, topicCategoryDAO.getTopicCategoryById(categoryId).getNick());
        Assert.assertEquals(title, topic.getTitle());
        Assert.assertEquals(content, topicContentDAO.getTopicContentByTopicId(topicId).getContent());

        util.printSuccessMessage();
    }

    /**
     * 【/api/topic (http post)】 test publish topic throw exception
     *      - permission exception
     *          - [✔] no login
     *          - [ ] account no activate
     *      - reqest param error, no norm
     *          - [✔] null check
     *          - [ ] param norm
     *              - [✔] topic category nick no is pure english
     *              - [ ] topic length length limit(5 <= lenth <= 50)
     *              - [✔] topic content length limit(50 <= lenth <= 100000)
     *      - database exception
     *          - [ ] no user (get user from cookie)
     *          - [ ] no category
     */
    @Test
    public void testPublishTopicThrowException() throws Exception {
        String apiUrl = "/api/topic";

        //no login
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post(apiUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage. NO_PERMISSION));

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof ServiceException);
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_PERMISSION);
        }

        //reqest param error, no norm
        String category = "testCategory";
        String title = "test title";
        String content = "test content";
        String[][] params = {
                {null, null, null}, {null, title, content}, {category, null, content}, {category, title, null},
                {"moive 123", title, content},
                {category, title, "testContentLength"}
        };

        for (String[] param: params) {
            if ("testContentLength".equals(param[2])) {
                //test 100001 topic content length
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= 100001; i++) {
                    sb.append("a");
                }
                param[2] = sb.toString();
            }

            String requestBody = "{" + util.getJsonField("category", param[0]) + ","
                    + util.getJsonField("title", param[1]) + ","
                    + util.getJsonField("content", param[2]) + "}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post(apiUrl)
                                .cookie(util.getAlreadyLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.PARAM_ERROR))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(), CoreMatchers.instanceOf(ParamsErrorException.class));
            }
        }


        util.printSuccessMessage();
    }

    /**
     * 【/api/topic/reply (http post)】 test publish topic reply success
     */
    @Test
    @Transactional
    public void testPublishTopicReplySuccess() throws Exception {
        int topicId = 1;
        String content = "new reply content";
        String requestBodyJson = "{" + util.getJsonField("topicid", topicId) + ","
                + util.getJsonField("content", content) + "}";
        System.out.println("input request-body: " + requestBodyJson);

        //before publish reply
        TopicDO beforeTopic = topicDAO.getTopicById(topicId);
        Assert.assertNotNull(beforeTopic);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/reply")
                    .cookie(util.getAlreadyLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //$.model.replyid
        Map modelMap = (Map) resultMap.get("model");
        int replyId = (Integer) modelMap.get("replyid");

        //compare database reply information
        TopicReplyDO reply = topicReplyDAO.getTopicReplyById(replyId);

        Assert.assertEquals(topicId, (int) reply.getTopicid());
        Assert.assertEquals(content, reply.getContent());

        TopicDO afterTopic = topicDAO.getTopicById(reply.getTopicid());
        Assert.assertNotNull(afterTopic);

        //compare insert reply before and after
        Assert.assertEquals(beforeTopic.getReplies() + 1, (int) afterTopic.getReplies());
        Assert.assertNotNull(afterTopic.getLastreplyuserid());
        Assert.assertNotEquals(beforeTopic.getLastreplytime(), afterTopic.getLastreplytime());

        Assert.assertEquals(reply.getCreatetime(), afterTopic.getLastreplytime());

        util.printSuccessMessage();
    }

    /**
     * 【undone】
     * 【/api/topic/reply (http post)】 test publish topic reply throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     *          - [ ] no user (get user from cookie)
     */

    /**
     * 【/api/topic-remove】test remove topic success
     */
    @Test
    @Transactional
    public void testRemoveTopicSuccess() throws Exception {
        int topicId = 1;
        String requestBodyJson = "{" + util.getJsonField("topicid", topicId) + "}";
        System.out.println("input reqeust-body: " + requestBodyJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic-remove")
                    .cookie(util.getAlreadyLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //confirm database already remove topic related (topic information, reply)
        Assert.assertNull(topicDAO.getTopicById(topicId));
        Assert.assertNull(topicContentDAO.getTopicContentByTopicId(topicId));
        List<TopicReplyDO> topicReplyList = topicReplyDAO.listTopicReplyByTopicId(topicId);
        Assert.assertEquals(topicReplyList.size(), 0);

        util.printSuccessMessage();
    }

    /**
     * 【undone】
     * 【/api/topic-remove】test remove topic throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *          - [ ] rank is 'admin'
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     */

    /**
     * 【/api/topic-remove】test remove topic reply success
     */
    @Test
    @Transactional
    public void testRemoveTopicReplySuccess() throws Exception {
        int replyId = 43;
        String requestBodyJson = "{" + util.getJsonField("replyid", replyId) + "}";
        System.out.println("input reqeust-body: " + requestBodyJson);

        TopicReplyDO beforeReply = topicReplyDAO.getTopicReplyById(replyId);
        Assert.assertNotNull(beforeReply);
        TopicDO beforeTopic = topicDAO.getTopicById(beforeReply.getTopicid());
        Integer beforeTopicReplies = beforeTopic.getReplies();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/reply-remove")
                        .cookie(util.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //confirm database already remove reply and related topic replies-1
        Assert.assertNull(topicReplyDAO.getTopicReplyById(replyId));

        int beforeTopicId = beforeTopic.getId();
        TopicDO afterTopic = topicDAO.getTopicById(beforeTopicId);
        Integer afterTopicReplies = afterTopic.getReplies();
        Assert.assertEquals(beforeTopicReplies - 1, (int) afterTopicReplies);

        util.printSuccessMessage();
    }

    /**
     * 【undone】
     * 【/api/topic/reply-remove】test remove topic reply throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *          - [ ] rank is 'admin'
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     */

    /**
     * 【/api/topic-update】test update topic success
     */
    @Test
    @Transactional
    public void testUpdateTopicSuccess() throws Exception {
        int topicId = 1;
        String newCategoryNick = "school";
        String newTitle = "new title";
        String newContent = "update new content";

        String requestBodyJson = "{" + util.getJsonField("topicid", topicId) + ","
                + util.getJsonField("category", newCategoryNick) + ","
                + util.getJsonField("title", newTitle) + ","
                + util.getJsonField("content", newContent) + "}";
        System.out.println("input request-body: " + requestBodyJson);

         mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic-update")
                    .cookie(util.getAlreadyLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //compare database data, confirm already alter
        TopicDO afterTopic = topicDAO.getTopicById(topicId);
        TopicContentDO afterTopicContent = topicContentDAO.getTopicContentByTopicId(topicId);
        TopicCategoryDO afterTopicCategory = topicCategoryDAO.getTopicCategoryById(afterTopic.getCategoryid());
        String afterCategory = afterTopicCategory.getNick();
        String afterTitle = afterTopic.getTitle();
        String afterContent = afterTopicContent.getContent();

        Assert.assertEquals(newCategoryNick, afterCategory);
        Assert.assertEquals(newTitle, afterTitle);
        Assert.assertEquals(newContent, afterContent);

        util.printSuccessMessage();
    }

    /**
     * 【undone】
     * 【/api/topic-update】test update topic throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *          - [ ] rank is 'admin'
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     */

    /**
     * 【/api/topic/reply-update】test update topic reply success
     */
    @Test
    @Transactional
    public void testUpdateTopicReplySuccess() throws Exception {
        int replyId = 43;
        String content = "new reply content";
        String requestBodyJson = "{" + util.getJsonField("replyid", replyId) + ","
                + util.getJsonField("content", content) + "}";
        System.out.println("input reqeust-body: " + requestBodyJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/reply-update")
                    .cookie(util.getAlreadyLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //compare database data, confirm already alter
        TopicReplyDO beforeReply = topicReplyDAO.getTopicReplyById(replyId);
        Assert.assertEquals(content, beforeReply.getContent());

        util.printSuccessMessage();
    }

    /**
     * 【undone】
     * 【/api/topic/reply-update】test update topic reply throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *          - [ ] rank is 'admin'
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     */

    /**
     * 【/api/topic/like】test update topic content like 'inc' success
     *      - need  @LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testUpdateTopicContentLikeIncSuccess() throws Exception {
        int topicId = 1;
        String command = "inc";
        String requestBody = "{" + util.getJsonField("topicid", topicId) + ", "
                + util.getJsonField("command", command) + "}";
        System.out.println("input request-body: "  + requestBody);

        Cookie cookie = util.getAlreadyLoginUserCookie();
        int userId = SecretUtil.decryptUserInfoToken(cookie.getValue()).getId();
        int beforeTopicContentLike = topicContentDAO.getTopicContentByTopicId(topicId).getLike();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/like")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model.like")
                        .value(CoreMatchers.is(beforeTopicContentLike + 1)));

        //compare database data
        int afterTopicContentLike = topicContentDAO.getTopicContentByTopicId(topicId).getLike();
        Assert.assertEquals(beforeTopicContentLike + 1, afterTopicContentLike);

        JSONArray jsonArray = JSON.parseArray(userActionDAO.getUserAction(userId).getLikeTopicIdJsonArray());
        Assert.assertEquals(topicId, (int) jsonArray.get(jsonArray.size() - 1));

        util.printSuccessMessage();
    }

    /**
     * 【/api/topic/like】test update topic content like 'dec' success
     */
    @Test
    @Transactional
    public void testUpdateTopicContentLikeDecSuccess() throws Exception {
        int topicId = 1;
        String command = "dec";
        String requestBody = "{" + util.getJsonField("topicid", topicId) + ", "
                + util.getJsonField("command", command) + "}";
        System.out.println("input request-body: "  + requestBody);

        //set user already like, get userId from cookie user
        Cookie cookie = util.getAlreadyLoginUserCookie();
        int userId = SecretUtil.decryptUserInfoToken(cookie.getValue()).getId();

        Assert.assertEquals(1, topicContentDAO.updateLikeAddOneByTopicId(topicId));
        Assert.assertEquals(1, userActionDAO.updateLikeTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId));

        int beforeTopicContentLike = topicContentDAO.getTopicContentByTopicId(topicId).getLike();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/like")
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.like").value(CoreMatchers.is(beforeTopicContentLike - 1)));

        //compare database data
        TopicContentDO afterTopicContent = topicContentDAO.getTopicContentByTopicId(topicId);
        int afterTopicContentLike = afterTopicContent.getLike();
        Assert.assertEquals(beforeTopicContentLike - 1, afterTopicContentLike);

        JSONArray jsonArray = JSON.parseArray(userActionDAO.getUserAction(userId).getLikeTopicIdJsonArray());
        Assert.assertTrue(jsonArray.indexOf(topicId) == -1);

        util.printSuccessMessage();
    }

    /**
     * 【/api/topic/like】test update topic content like throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] instruction error
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     *          - [ ] alter forum_topic_content 'like' field fail
     *          - [ ] alter forum_user_action 'fua_liketopic_ft_id_array' fail
     *          - [ ] user already like topic, input 'inc'
     *          - [ ] user no like topic, input 'dec'
     *          - [ ] repeat input 'inc' or 'dec'
     */

    /**
     * 【/api/topic/collect】 test user collect topic success
     *      - [✔] (forum_user_action)collect topic 'inc' and (forum_topic_action) collect user 'inc'
     *      - [ ] (forum_user_action) collect topic dec  abd (forum_topic_action) collect user 'dec'
     */
    @Test
    @Transactional
    public void testUserCollectTopicSuccess() throws Exception {
        // cookie userId = 6
        int topicId = 5;
        String requestBody = "{" + util.getJsonField("topicid", topicId) + "}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/collect")
                    .cookie(util.getAlreadyLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.userCollectTopicId").value(CoreMatchers.hasItem(topicId)));

        //database
        int cookieUserId = 6;
        String userCollectTopicIdJsonArrayString = userActionDAO.getUserActionCollectTopicIdJsonArray(cookieUserId);
        String topicCollectUserIdJsonArrayString = topicActionDAO.getTopicActionCollectUserIdJsonArray(topicId).getCollectUserIdJsonArray();

        Assert.assertNotEquals(-1 , JSON.parseArray(userCollectTopicIdJsonArrayString).indexOf(topicId));
        Assert.assertNotEquals(-1, JSON.parseArray(topicCollectUserIdJsonArrayString).indexOf(cookieUserId));

        util.printSuccessMessage();
    }

    /**
     * 【/api/topic/collect】test update topic content like throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     *          - [ ] alter forum_user_action  'fua_collect_ft_id_array' field fail
     *          - [ ] alter forum_topic_action 'fta_collect_fu_id_array' field fail
     */
}
