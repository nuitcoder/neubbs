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
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.ParamsErrorException;
import org.neusoft.neubbs.exception.ServiceException;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.JsonUtil;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Topic api 测试
 *      - 测试 /api/topic (GET)
 *      - 测试 /api/topic/reply (GET)
 *      - 测试 /api/topic/hot
 *      - 测试 /api/topics
 *      - 测试 /api/topics/pages
 *      - 测试 /api/topics/categorys
 *      - 测试 /api/topic (POST)
 *      - 测试 /api/topic/reply (POST)
 *      - 测试 /api/topic-remove
 *      - 测试 /api/topic-update
 *      - 测试 /api/reply-update
 *      - 测试 /api/topic/like
 *      - 测试 /api/topic/newlike
 *      - 测试 /api/topic/collect
 *      - 测试 /api/topic/attention
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
        //prevent execute this api test class, appear unexpected exception
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);

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
     *          - request param error
     *              - [✔] null
     *              - [✔] format not norm
     *          - service exception
     *              - [✔] no topic
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
     *          - request param error
     *              - [✔] null
     *              - [✔] format not norm
     *          - service exception
     *              - [✔] no reply
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
     *      - 获取首页话题信息列表成功
     *          - input different param
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
    @Transactional
    public void testListHomeTopicSuccess() throws Exception {
        //input limit, page
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics")
                        .param("limit", "1")
                        .param("page", "1")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andDo(MockMvcResultHandlers.print())
                .andReturn();

        //get model.[0] element map
        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());
        List modelList = (List) resultMap.get("model");
        Assert.assertEquals(1, modelList.size());
        Map firstModelListMap = (Map) modelList.get(0);

        //judge $.model
        util.confirmMapShouldHavaKeyItems(firstModelListMap,
                "title", "replies", "lastreplytime", "createtime", "topicid",
                "content", "read", "like", "category", "user", "lastreplyuser");

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
     *      - 获取首页话题信息列表异常
     *          - request param error
     *              - [✔] null
     *              - [✔] format not norm
     *          - service exception
     *              - [✔] input page exceed topic max page
     *              - [ ] no query topics
     *              - [ ] no category
     *              - [ ] no username
     */
    @Test
    public void testListHomeTopicsException() throws Exception {
        String[][] params = {
                {null, null}, {"1", null},
                {"1111111111111", "123"}, {"123", "1111111111111"}, {"11111111111111", "1111111111"},
                {"*-+", "1"}, {"1", "-=="}, {"-&&*%", "****("}, {"abc*123", "___abc123"},
                {"10000","1"}
        };

        for (String[] param: params) {
            String limit = param[0];
            String page = param[1];
            System.out.println("input param: limit = " + limit + ", page = " + page);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topics")
                                .param("limit", limit)
                                .param("page", page)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(CoreMatchers.is("false")))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
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
     * 测试 /api/topics/pages
     *      - 获取话题总页数成功
     *          - input different param
     *              - [✔] no input (default limit = 25)
     *              - [✔] input limit,
     *              - [✔] input category
     *              - [ ] input limit, category
     *              - [✔] input username
     *              - [ ] input limit, username
     *              - [✔] input category, username
     *              - [ ] input limit, category, username
     */
    @Test
    @Transactional
    public void testGetTopicTotalPagesSuccess() throws Exception {
        String apiUrl = "/api/topics/pages";

        String limit;
        String category;
        String username;

        //no input any params, use default limit = 25
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

        //input category, username
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
     * 测试 /api/topics/pages
     *      - 获取话题总页数异常
     *          - request param error
     *              - [ ] null
     *              - [ ] type error
     *              - [ ] format not norm
     *          - service exception
     *              - [✔] no category
     *              - [✔] no username
     */
    @Test
    public void testCountTopicTotalPagesException() throws Exception {
        String apiUrl = "/api/topics/pages";

        //input category, but category no exist
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get(apiUrl)
                            .param("category", "noExistCategory")
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
                    MockMvcRequestBuilders.get(apiUrl)
                            .param("username", "noExistUser")
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
     * 测试 /api/topics/categorys
     *      - 获取话题分类信息成功
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

        //judge $.model.[0]
        List modelList = (List) resultMap.get("model");
        Map firstModelListCategoryMap = (Map) modelList.get(1);
        util.confirmMapShouldHavaKeyItems(firstModelListCategoryMap, "id", "name", "description");

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic （POST）
     *      - 测试发布话题成功
     *      - 需要权限：@LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testReleaseTopicSuccess() throws Exception {
        //build request-body
        String categoryNick = "game";
        String title = "new topic title";
        String content = "new topic content";
        String requestBody = "{\"category\":\"" + categoryNick + "\","
                + "\"title\":\"" + title + "\","
                + "\"content\":\"" + content + "\"}";
        System.out.println("input request-body: " + requestBody);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic")
                        .cookie(util.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        //judge $.model.topicid
        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());
        Map modelMap = (Map) resultMap.get("model");

        int topicId = (Integer) modelMap.get("topicid");

        //validate topic information
        TopicDO topic = topicDAO.getTopicById(topicId);
        Assert.assertNotNull(topic);
        Assert.assertEquals(title, topic.getTitle());
        Assert.assertEquals(content, topicContentDAO.getTopicContentByTopicId(topicId).getContent());

        //validate category
        int categoryId = topic.getCategoryid();
        Assert.assertEquals(categoryNick, topicCategoryDAO.getTopicCategoryById(categoryId).getNick());

        //validate topic action
        Assert.assertNotNull(topicActionDAO.getTopicAction(topicId));

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic （POST）
     *      - 测试发布话题异常
     *          - permission exception
     *              - [✔] no login
     *              - [✔] the account no activated
     *          - request param error
     *              - [✔] null
     *              - [ ] type error
     *              - format not norm
     *                  - [✔] topic category nick no is pure english
     *                  - [ ] topic title length limit(1 <= length <= 50)
     *                  - [✔] topic content length limit(1 <= length <= 100000)
     *          - server exception
     *              - [ ] no user (get user from cookie)
     *              - [ ] no category
     */
    @Test
    public void testReleaseTopicException() throws Exception {
        String apiUrl = "/api/topic";

        //no login
        util.testApiThrowNoPermissionException("/api/topic", RequestMethod.POST, null);

        //the account no activated
        util.testApiThrowNoPermissionException("/api/topic", RequestMethod.POST, util.getNoActivatedUserDO());

        //request param error
        String category = "testCategory";
        String title = "test title";
        String content = "test content";
        String[][] params = {
                {null, null, null}, {null, title, content}, {category, null, content}, {category, title, null},
                {"no category", title, content}, {category, "", content}, {category, title, ""}
        };

        for (String[] param: params) {
            if ("".equals(param[2])) {
                //test topic content length > 100000 error
                StringBuilder strBdr = new StringBuilder();
                for (int i = 1; i <= 100001; i++) {
                    strBdr.append("a");
                }
                param[2] = strBdr.toString();
            }

            //build request-body
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
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.PARAM_ERROR))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(), CoreMatchers.instanceOf(ParamsErrorException.class));
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic/reply （POST）
     *      - 发布话题回复成功
     *      - 需要权限: @LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testReleaseTopicReplySuccess() throws Exception {
        int topicId = 1;
        String content = "new reply content";
        String requestBody = "{" + util.getJsonField("topicid", topicId) + ","
                + util.getJsonField("content", content) + "}";
        System.out.println("input request-body: " + requestBody);

        //before publish reply
        TopicDO beforeTopic = topicDAO.getTopicById(topicId);
        Assert.assertNotNull(beforeTopic);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/reply")
                    .cookie(util.getAlreadyLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //judge $.model.replyid
        Map modelMap = (Map) resultMap.get("model");

        //validate reply information
        TopicReplyDO reply = topicReplyDAO.getTopicReplyById((Integer) modelMap.get("replyid"));
        Assert.assertEquals(topicId, (int) reply.getTopicid());
        Assert.assertEquals(content, reply.getContent());

        //validate topic information
        TopicDO afterTopic = topicDAO.getTopicById(reply.getTopicid());
        Assert.assertNotNull(afterTopic);
        Assert.assertEquals(beforeTopic.getReplies() + 1, (int) afterTopic.getReplies());
        Assert.assertNotEquals(beforeTopic.getLastreplytime(), afterTopic.getLastreplytime());
        Assert.assertEquals(reply.getCreatetime(), afterTopic.getLastreplytime());

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic/reply
     *      - 测试发布话题回复异常
     *          - no permission
     *              - [✔] no login
     *              - [✔] the account not activated
     *          - request param error
     *              - [✔] null
     *              - [✔] type error
     *              - [✔] format not norm
     *          - service exception
     *              - [ ] no topic
     *              - [ ] no user (get user from cookie)
     */
    @Test
    @Transactional
    public void testReleaseTopicTopicReplyException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/topic/reply", RequestMethod.POST, null);

        //the account not activated
        util.testApiThrowNoPermissionException("/api/topic/reply", RequestMethod.POST, util.getNoActivatedUserDO());

        //request param error
        String[][] params = {{null, null}, {null, "new topic content"}, {"1", null}};

        for (String[] param: params) {
            String requestBody = "{"
                 + util.getJsonField("topicid", (param[0] == null ? null : Integer.parseInt(param[0]))) + ","
                 + util.getJsonField("content", param[1]) + "}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/topic/reply")
                                .cookie(util.getAlreadyLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.PARAM_ERROR))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertTrue(ne.getRootCause() instanceof ParamsErrorException);
                Assert.assertEquals(ApiMessage.PARAM_ERROR, ne.getRootCause().getMessage());
            }
        }

        //request param 'topicId' no number type
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/topic/reply")
                    .cookie(util.getAlreadyLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{" + util.getJsonField("topicid", "1") + "," + util.getJsonField("content", "content") + "}")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.UNKNOWN_ERROR))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof ClassCastException);
        }

        //request param 'content' String length
        StringBuilder strBdr = new StringBuilder();
        for (int i = 1; i <= 151; i ++) {
            strBdr.append("a");
        }
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/topic/reply")
                            .cookie(util.getAlreadyLoginUserCookie())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" + util.getJsonField("topicid", 1) + "," + util.getJsonField("content", strBdr.toString()) + "}")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.PARAM_ERROR))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof ParamsErrorException);
            Assert.assertEquals(ApiMessage.PARAM_ERROR, ne.getRootCause().getMessage());
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic-remove
     *      - 测试删除话题成功
     *      - 需要权限：@LoginAuthorization @AccountActivation @AdminRank
     */
    @Test
    @Transactional
    public void testRemoveTopicSuccess() throws Exception {
        int topicId = 1;
        String requestBody = "{" + util.getJsonField("topicid", topicId) + "}";
        System.out.println("input request-body: " + requestBody);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic-remove")
                    .cookie(util.getAlreadyLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //confirm already remove topic information(forum_topic, forum_topic_action, forum_topic_content, forum_topic_reply)
        Assert.assertNull(topicDAO.getTopicById(topicId));
        Assert.assertNull(topicContentDAO.getTopicContentByTopicId(topicId));
        Assert.assertNull(topicActionDAO.getTopicAction(topicId));
        Assert.assertEquals(topicReplyDAO.listTopicReplyByTopicId(topicId).size(), 0);

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic-remove
     *      - 测试删除话题异常
     *          - no permission
     *              - [✔] no login
     *              - [✔] the account not activated
     *              - [✔] rank not 'admin'
     *          - request param error
     *              - [✔] null
     *              - [✔] type error
     *              - [✔] format not norm
     *          - service exception
     *              - [✔] no topic
     */
    @Test
    @Transactional
    public void testRemoveTopicException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/topic-remove", RequestMethod.POST, null);

        //the account the activated
        util.testApiThrowNoPermissionException("/api/topic-remove", RequestMethod.POST, util.getNoActivatedUserDO());
        
        //rank not 'admin'
        util.testApiThrowNoPermissionException("/api/topic-remove", RequestMethod.POST, util.getNoAdminRankUserDO());

        //request param error
        Object[] params = {null, "123", "kkk", "123**-", 100000000};

        for (Object topicId: params) {
            String requestBody = "{" + util.getJsonField("topicid", topicId) + "}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/topic-remove")
                                .cookie(util.getAlreadyLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.PARAM_ERROR))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));
            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(), CoreMatchers.anyOf(
                        CoreMatchers.instanceOf(ParamsErrorException.class),
                        CoreMatchers.instanceOf(ClassCastException.class),
                        CoreMatchers.instanceOf(ServiceException.class)
                ));
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic/reply-remove
     *      - 删除回复成功
     *      - 需要权限：@LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testRemoveTopicReply() throws Exception {
        int replyId = 1;
        String requestBody = "{" + util.getJsonField("replyid", replyId) + "}";
        System.out.println("input request-body: " + requestBody);

        //save reply before to remove
        TopicReplyDO reply = topicReplyDAO.getTopicReplyById(replyId);
        Assert.assertNotNull(reply);
        int replyTopicId = reply.getTopicid();
        TopicDO beforeTopic = topicDAO.getTopicById(replyTopicId);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/reply-remove")
                        .cookie(util.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        //confirm database already remove reply and related topic 'replies' - 1
        Assert.assertNull(topicReplyDAO.getTopicReplyById(replyId));
        TopicDO afterTopic = topicDAO.getTopicById(replyTopicId);
        Assert.assertEquals(beforeTopic.getReplies() - 1, (int) afterTopic.getReplies());

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic/reply-remove
     *      - 测试删除话题回复异常
     *          - no permission
     *              - [✔] no login
     *              - [✔] the account not activated
     *              - [✔] rank not 'admin'
     *          - request param error
     *              - [✔] null
     *              - [✔] type error
     *              - [✔] format not norm
     *          - service exception
     *              - [✔] no reply
     */
    @Test
    @Transactional
    public void testRemoveTopicReplyException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/topic/reply-remove", RequestMethod.POST, null);

        //the account not activated
        util.testApiThrowNoPermissionException("/api/topic/reply-remove", RequestMethod.POST, util.getNoActivatedUserDO());

        //the rank not 'admin'
        util.testApiThrowNoPermissionException("/api/topic/reply-remove", RequestMethod.POST, util.getNoAdminRankUserDO());

        //request param error
        Object[] params = {null, "123", "k", "1 + ", 100000000};

        for (Object replyId: params) {
            String requestBody = "{" + util.getJsonField("replyid", replyId) + "}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/topic/reply-remove")
                                .cookie(util.getAlreadyLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.PARAM_ERROR))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(), CoreMatchers.anyOf(
                        CoreMatchers.instanceOf(ParamsErrorException.class),
                        CoreMatchers.instanceOf(ClassCastException.class),
                        CoreMatchers.instanceOf(ServiceException.class)
                ));
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic-update
     *      - 编辑话题成功
     *      - 需要权限：@LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testUpdateTopicSuccess() throws Exception {
        int topicId = 1;
        String newCategoryNick = "school";
        String newTitle = "new title";
        String newContent = "update new content";

        String requestBody = "{" + util.getJsonField("topicid", topicId) + ","
                + util.getJsonField("category", newCategoryNick) + ","
                + util.getJsonField("title", newTitle) + ","
                + util.getJsonField("content", newContent) + "}";
        System.out.println("input request-body: " + requestBody);

         mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic-update")
                    .cookie(util.getAlreadyLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //compare database data, topic content, topic title and topic content
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
     * 测试 /api/topic-update
     *      - 话题更新异常
     *      - no permission
     *          - [✔] no login
     *          - [✔] the account no activated
     *      - request param error
     *          - [✔] null
     *          - [✔] type error
     *          - [✔] format not norm
     *      - service exception
     *          - [✔] no topic
     */
    @Test
    @Transactional
    public void testUpdateTopicException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/topic-update", RequestMethod.POST, null);

        //the account not activated
        util.testApiThrowNoPermissionException("/api/topic-update", RequestMethod.POST, util.getNoActivatedUserDO());

        //request param error
        int topicId = 1;
        String newCategoryNick = "school";
        String newTitle = "new title";
        String newContent = "update new content";
        Object[][] params = {
                {null, null, null, null}, {topicId, null, null, null}, {null, newCategoryNick, null, null},
                {null, null, newTitle, null}, {null, null ,null, newContent}, {topicId, newCategoryNick, null, null},
                {null, newCategoryNick, newTitle, newContent},
                {"kkk", newCategoryNick, newTitle, newContent}, {topicId, "123", newTitle, newContent},
                {topicId, newCategoryNick, "", newContent}, {topicId, newCategoryNick, newTitle, ""},
                {1000000000, newCategoryNick, newTitle, newContent}
        };

        for (Object[] param: params) {
                String requestBody = "{" + util.getJsonField("topicid", param[0]) + ","
                    + util.getJsonField("category", param[1]) + ","
                    + util.getJsonField("title", param[2]) + ","
                    + util.getJsonField("content", param[3]) + "}";
                System.out.println("input request-body: " + requestBody);

                try {
                    mockMvc.perform(
                           MockMvcRequestBuilders.post("/api/topic-update")
                                .cookie(util.getAlreadyLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                     .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                     .andExpect(MockMvcResultMatchers.jsonPath("$.mode").value(CoreMatchers.notNullValue()));

                } catch (NestedServletException ne) {
                    Assert.assertThat(ne.getRootCause(),
                            CoreMatchers.anyOf(
                                    CoreMatchers.instanceOf(ParamsErrorException.class),
                                    CoreMatchers.instanceOf(ClassCastException.class),
                                    CoreMatchers.instanceOf(ServiceException.class)
                            )
                    );
                }

        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /topic/reply-update
     *      - 编辑回复成功
     *      - 需要权限：@LoginAuthorization, @AccountActivation
     */
    @Test
    @Transactional
    public void testUpdateTopicReplySuccess() throws Exception {
        int replyId = 1;
        String newReplyContent = "new reply content";
        String requestBody = "{" + util.getJsonField("replyid", replyId) + ","
                + util.getJsonField("content", newReplyContent) + "}";
        System.out.println("input request-body: " + requestBody);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/reply-update")
                    .cookie(util.getAlreadyLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        //compare database data, before and after modification
        Assert.assertEquals(newReplyContent, topicReplyDAO.getTopicReplyById(replyId).getContent());

        util.printSuccessMessage();
    }

    /**
     * 测试 /topic/reply-update
     *      - 更新回复异常
     *      - no permission
     *          - [✔] no login
     *          - [✔] the account not activated
     *      - request param error
     *          - [✔] null
     *          - [✔] type error
     *          - [✔] format not norm
     *      - service exception
     *          - [✔] no topic
     */
    @Test
    @Transactional
    public void testUpdateTopicReplyException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/topic/reply-update", RequestMethod.POST, null);

        //the account not activated
        util.testApiThrowNoPermissionException("/api/topic/reply-update", RequestMethod.POST, util.getNoActivatedUserDO());

        //request param error
        int replyId = 1;
        String newReplyContent = "new reply content";
        Object[][] params = {
                {null, null}, {null, newReplyContent}, {replyId, null},
                {"kkkkk", newReplyContent}, {replyId, ""},
                {100000000, newReplyContent}
        };

        for (Object[] param: params) {
            String requestBody = "{" + util.getJsonField("replyid", param[0]) + ","
                    + util.getJsonField("content", param[1]) + "}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/topic/reply-update")
                                .cookie(util.getAlreadyLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ClassCastException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic/like
     *      - 话题点赞 + 1 成功 & 话题点赞 - 1 成功
     *      - 需要权限：@LoginAuthorization @AccountActivation
     *      - 旧接口，需要输入 command 命令参数（inc - 点赞 or dec - 取消）
     */
    @Test
    @Transactional
    public void testLikeTopicSuccess() throws Exception {
        int topicId = 1;

        //topic like + 1
        String command = "inc";
        String requestBody = "{" + util.getJsonField("topicid", topicId) + ", "
                + util.getJsonField("command", command) + "}";
        System.out.println("input dec request-body: "  + requestBody);

        Cookie cookie = util.getAlreadyLoginUserCookie();
        UserDO cookieUser = SecretUtil.decryptUserInfoToken(cookie.getValue());
        int cookieUserId = cookieUser.getId();
        int beforeTopicContentLike = topicContentDAO.getTopicContentByTopicId(topicId).getLike();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/like")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model.like")
                        .value(CoreMatchers.is(beforeTopicContentLike + 1)));

        //judge 'forum_user_action' new insert topicId
        JSONArray jsonArray = JSON.parseArray(userActionDAO.getUserActionLikeTopicIdJsonArray(cookieUserId));
        Assert.assertEquals(topicId, (int) jsonArray.get(jsonArray.size() - 1));

        //judge 'forum_topic_action' new insert userId
        jsonArray = JSON.parseArray(topicActionDAO.getTopicActionLikeUserIdJsonArray(topicId));
        Assert.assertEquals(cookieUserId, jsonArray.get(jsonArray.size() - 1));

        //topic like - 1
        command = "dec";
        requestBody = "{" + util.getJsonField("topicid", topicId) + ", "
                + util.getJsonField("command", command) + "}";
        System.out.println("input 'dec' request-body: "  + requestBody);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/like")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model.like")
                        .value(CoreMatchers.is(beforeTopicContentLike)));

        Assert.assertEquals(beforeTopicContentLike, (int) topicContentDAO.getTopicContentByTopicId(topicId).getLike());
        Assert.assertEquals(-1, JsonUtil.getIntElementIndex(userActionDAO.getUserActionLikeTopicIdJsonArray(cookieUserId), topicId));
        Assert.assertEquals(-1, JsonUtil.getIntElementIndex(topicActionDAO.getTopicActionLikeUserIdJsonArray(topicId), cookieUserId));

        util.printSuccessMessage();
    }


    /**
     * 测试 /topic/like
     *      - 旧接口，需要输入 command 命令参数（inc - 点赞 or dec - 取消）
     *      - 点赞接口异常
     *      - no permission
     *          - [✔] no login
     *          - [✔] the account not activated
     *      - request param error
     *          - [✔] null
     *          - [✔] type error
     *          - [✔] format not norm
     *          - [✔] command error
     *      - service exception
     *          - [✔] no topic
     */
    @Test
    @Transactional
    public void testLikeTopicException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/topic/like", RequestMethod.POST, null);

        //the account not activated
        util.testApiThrowNoPermissionException("/api/topic/like", RequestMethod.POST, util.getNoActivatedUserDO());

        //request param error
        int topicId = 1;
        String command = "inc";
        Object [][] params = {
                {null, null}, {null, command}, {topicId, null},
                {"kk", command}, {"100000000", command}, {"123-+-", command},
                {topicId, "ttt"}, {topicId, "---"}, {topicId, "123"},
                {1000000, command}
        };

        for (Object[] param: params) {
            String requestBody = "{" + util.getJsonField("topicid", param[0]) + ", "
                    + util.getJsonField("command", param[1]) + "}";
            System.out.println("input request-body: "  + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/topic/like")
                            .cookie(util.getAlreadyLoginUserCookie())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                            .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CoreMatchers.notNullValue()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ClassCastException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /topic/newlike
     *      - 点赞话题新接口成功 + 1 和 -1
     *      - 需要权限：@LoginAuthorization @AccountActivation
     *      - 新接口，不需要输入 command（inc | dec）,重复调用即自判断点暂（or 取消）
     */
    @Test
    @Transactional
    public void testNewLikeTopicSuccess() throws Exception {
        int topicId = 1;
        Cookie cookie = util.getAlreadyLoginUserCookie();
        UserDO cookieUser = SecretUtil.decryptUserInfoToken(cookie.getValue());
        int cookieUserId = cookieUser.getId();
        String requestBody = "{" + util.getJsonField("topicid", topicId) + "}";

        //like topic + 1
        TopicContentDO topicContent = topicContentDAO.getTopicContentByTopicId(topicId);
        int beforeTopicLike = topicContent.getLike();

        System.out.println("request 'inc' request-body: " + requestBody);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/newlike")
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.model.userLikeTopicId").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.model.like").value(beforeTopicLike + 1));

        //judge forum_user_action 'fua_like_ft_id_array'
        JSONArray jsonArray = JSON.parseArray(userActionDAO.getUserActionLikeTopicIdJsonArray(cookieUserId));
        Assert.assertEquals(topicId, jsonArray.get(jsonArray.size() - 1));

        //judge forum_topic_action 'fta_like_fu_id_array'
        jsonArray = JSON.parseArray(topicActionDAO.getTopicActionLikeUserIdJsonArray(topicId));
        Assert.assertEquals(cookieUserId, jsonArray.get(jsonArray.size() - 1));

        //like topic - 1
        System.out.println("request 'dec' request-body: " + requestBody);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/newlike")
                    .cookie(util.getAlreadyLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.model.userLikeTopicId").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.model.like").value(beforeTopicLike));

        Assert.assertEquals(-1, JsonUtil.getIntElementIndex(userActionDAO.getUserActionLikeTopicIdJsonArray(cookieUserId), topicId));
        Assert.assertEquals(-1, JsonUtil.getIntElementIndex(topicActionDAO.getTopicActionLikeUserIdJsonArray(topicId), cookieUserId));

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic/newlike
     *      - 新接口，不需要 command，自动处理话题用户喜欢话题状态，取反（已喜欢 -> 未喜欢， 未喜欢 -> 已喜欢）
     *      - no permission
     *          - [✔] no login
     *          - [✔] the account not activated
     *      - request param error
     *          - [✔] null
     *          - [✔] type error
     *          - [✔] format not norm
     *      - service exception
     *          - [✔] no topic
     */
    @Test
    @Transactional
    public void testNewLikeTopicException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/topic/newlike", RequestMethod.POST, null);

        //the account not activated
        util.testApiThrowNoPermissionException("/api/topic/newlike", RequestMethod.POST, util.getNoActivatedUserDO());

        //request param error
        Object[] params = {null, "123", "kk", "123-+-", 1000000};

        for (Object topicId: params) {
            String requestBody = "{" + util.getJsonField("topicid", topicId) + "}";
            System.out.println("input request-body: "  + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/topic/newlike")
                            .cookie(util.getAlreadyLoginUserCookie())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                            .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CoreMatchers.notNullValue()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ClassCastException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        util.printSuccessMessage();
    }


    /**
     * 测试 /api/topic/collect
     *      - 收藏话题成功 + 1 和 -1
     *      - 需要权限: @LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testCollectTopicSuccess() throws Exception {
        int topicId = 1;
        Cookie cookie = util.getAlreadyLoginUserCookie();
        UserDO cookieUser = SecretUtil.decryptUserInfoToken(cookie.getValue());
        int cookieUserId = cookieUser.getId();
        String requestBody = "{" + util.getJsonField("topicid", topicId) + "}";

        //collect topic + 1
        System.out.println("input 'inc' request-body: " + requestBody);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/collect")
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.userCollectTopicId").value(CoreMatchers.hasItem(topicId)));

        //compare forum_topic_action 'fta_collect_fu_id_array'
        JSONArray jsonArray = JSON.parseArray(topicActionDAO.getTopicActionCollectUserIdJsonArray(topicId));
        Assert.assertEquals(cookieUserId, jsonArray.get(jsonArray.size() - 1));

        //compare forum_user_action 'fua_following_fu_id_array'
        jsonArray = JSON.parseArray(userActionDAO.getUserActionCollectTopicIdJsonArray(cookieUserId));
        Assert.assertEquals(topicId, jsonArray.get(jsonArray.size() - 1));

        //collect topic - 1
        System.out.println("input 'dec' request-body: " + requestBody);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/collect")
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.userCollectTopicId").isArray());

        Assert.assertEquals(-1, JsonUtil.getIntElementIndex(topicActionDAO.getTopicActionCollectUserIdJsonArray(topicId), cookieUserId));
        Assert.assertEquals(-1, JsonUtil.getIntElementIndex(userActionDAO.getUserActionCollectTopicIdJsonArray(cookieUserId), topicId));

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic/collect
     *      - 收藏话题异常
     *      - no permission
     *          - [✔] no login
     *          - [✔] the account not activated
     *      - request param error
     *          - [✔] null
     *          - [✔] type error
     *          - [✔] format not norm
     *      - service exception
     *          - [✔] no topic
     */
    @Test
    @Transactional
    public void testCollectTopicException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/topic/attention", RequestMethod.POST, null);

        //the account not activated
        util.testApiThrowNoPermissionException("/api/topic/attention", RequestMethod.POST, util.getNoActivatedUserDO());

        //request param error & service exception
        Object [] params = {null, "kk", "123-+-", "123", 10000000};
        for (Object topicId: params) {
            String requestBody = "{" + util.getJsonField("topicid", topicId) + "}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/topic/collect")
                            .cookie(util.getAlreadyLoginUserCookie())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CoreMatchers.notNullValue()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.model.userCollectTopicId").isArray());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ClassCastException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic/attention
     *      - 关注话题成功 + 1 和 -1
     *      - 需要权限: @LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testAttentionTopicSuccess() throws Exception {
        int topicId = 1;
        Cookie cookie = util.getAlreadyLoginUserCookie();
        UserDO cookieUser = SecretUtil.decryptUserInfoToken(cookie.getValue());
        int cookieUserId = cookieUser.getId();
        String requestBody = "{" + util.getJsonField("topicid", topicId) + "}";

        //attention topic + 1
        System.out.println("input 'inc' request-body: " + requestBody);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/attention")
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.userAttentionTopicId").value(CoreMatchers.hasItem(topicId)));

        //compare forum_topic_action 'fta_attention_fu_id_array'
        JSONArray jsonArray = JSON.parseArray(topicActionDAO.getTopicActionAttentionUserIdJsonArray(topicId));
        Assert.assertEquals(cookieUserId, jsonArray.get(jsonArray.size() - 1));

        //compare forum_user_action 'fua_following_fu_id_array'
        jsonArray = JSON.parseArray(userActionDAO.getUserActionAttentionTopicIdJsonArray(cookieUserId));
        Assert.assertEquals(topicId, jsonArray.get(jsonArray.size() - 1));

        //collect topic - 1
        System.out.println("input 'dec' request-body: " + requestBody);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/attention")
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.userAttentionTopicId").isArray());

        Assert.assertEquals(-1, JsonUtil.getIntElementIndex(topicActionDAO.getTopicActionAttentionUserIdJsonArray(topicId), cookieUserId));
        Assert.assertEquals(-1, JsonUtil.getIntElementIndex(userActionDAO.getUserActionAttentionTopicIdJsonArray(cookieUserId), topicId));

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/topic/attention
     *      - 关注话题异常
     *      - no permission
     *          - [✔] no login
     *          - [✔] the account not activated
     *      - request param error
     *          - [✔] null
     *          - [✔] type error
     *          - [✔] format not norm
     *      - service exception
     *          - [✔] no topic
     */
    @Test
    @Transactional
    public void testAttentionTopicException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/topic/attention", RequestMethod.POST, null);

        //the account not activated
        util.testApiThrowNoPermissionException("/api/topic/attention", RequestMethod.POST, util.getNoActivatedUserDO());

        //request param error & service exception
        Object [] params = {null, "kk", "123-+-", "123", 10000000};
        for (Object topicId: params) {
            String requestBody = "{" + util.getJsonField("topicid", topicId) + "}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/topic/attention")
                            .cookie(util.getAlreadyLoginUserCookie())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CoreMatchers.notNullValue()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.model.userAttentionTopicId").isArray());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ClassCastException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        util.printSuccessMessage();
    }
}
