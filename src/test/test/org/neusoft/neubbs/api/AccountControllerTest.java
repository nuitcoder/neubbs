package test.org.neusoft.neubbs.api;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.filter.ApiFilter;
import org.neusoft.neubbs.controller.handler.DynamicSwitchDataSourceHandler;
import org.neusoft.neubbs.dao.IUserActionDAO;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.ParamsErrorException;
import org.neusoft.neubbs.exception.PermissionException;
import org.neusoft.neubbs.exception.ServiceException;
import org.neusoft.neubbs.service.ICacheService;
import org.neusoft.neubbs.service.ICaptchaService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.utils.FtpUtil;
import org.neusoft.neubbs.utils.SecretUtil;
import org.neusoft.neubbs.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.transaction.Transactional;
import java.util.ArrayList;

/**
 * Account api 测试
 *      - 测试 /api/account
 *      - 测试 /api/account/state
 *      - 测试 /api/account/login
 *      - 测速 /api/account/logout
 *      - 测试 /api/account/register
 *      - 测试 /api/account/update-profile
 *      - 测试 /api/account/update-password
 *      - 测试 /api/account/update-email
 *      - 测试 /api/account/activate
 *      - 测试 /api/account/captcha
 *      - 测试 /api/account/validate-captcha
 *      - 测试 /api/account/forget-password
 *      - 测试 /api/account/following
 *
 * 【注意】 设置配置文件(需设置 ApplicationContext.xml 和 mvc.xm,否则会报错)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-context.xml"}),
        @ContextConfiguration(locations = {"classpath:spring-mvc.xml"}),
})
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ApiTestUtil util;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IUserActionDAO userActionDAO;

    static class Param {
        String key;
        Object value;

        Param (String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    /*
     * ***********************************************
     * init method
     * ***********************************************
     */

    @BeforeClass
    public static void init() {
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * api 测试执行前构建
     * - 方式1：依赖 Spring 上下文，需加入 @ContextConfiguration 类注解，加载配置文件
     * - 方式2：自主构建控制器，可加入拦截器，过滤器之类
     */
    @Before
    public void setup() {
        //prevent execute this api test class, appear unexpected exception
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);

        //method 1
        this.webApplicationContext.getServletContext().setAttribute(ParamConst.LOGIN_USER, 0);
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .addFilter(new ApiFilter())
                .build();

        this.util = ApiTestUtil.getInstance(mockMvc);

        //method 2, custom default environment
        //AccountController accountController = (AccountController) webApplicationContext.getBean("accountController");
        //StandaloneMockMvcBuilder standBuild = MockMvcBuilders.standaloneSetup(accountController);
        //    standBuild.addFilter(new ApiFilter());
        //    standBuild.addInterceptors(new ApiInterceptor());
        //this.mockMvc = standBuild.build();
    }

    /*
     * ***********************************************
     * api test method
     * ***********************************************
     */

    /**
     * 测试 /api/account
     *      - 获取用户信息成功
     */
    @Test
    public void testGetUserInfoSuccess() throws Exception {
        ArrayList<Param> paramList = new ArrayList<>();
            paramList.add(new Param("username", "suvan"));
            paramList.add(new Param("username", "liushuwei0925@gmail.com"));
            paramList.add(new Param("email", "liushuwei0925@gmail.com"));

        for (Param param : paramList) {
            System.out.println("input: key=" + param.key + ", value=" + param.value);

            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account")
                            .param(param.key, (String) param.value)
            ).andExpect(MockMvcResultMatchers.status().is(200))
             .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
             .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                    .andReturn();

            util.isExistKeyItems(result,
                    "email", "sex", "birthday", "position", "description",
                    "avator", "state", "createtime", "userid", "username",
                    "following", "followed", "like", "collect", "attention", "topic", "reply");
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account
     *      - [✔] 获取用户信息，常同时输入两个不同参数，其中之一出现异常
     */
    @Test
    public void testGetUserInfoSameInputTwoParamException() throws Exception {
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account")
                            .param("username", "noUser")
                            .param("email", "liushuwei0925@gmail.com")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        } catch (NestedServletException ne) {
            Throwable throwable = ne.getRootCause();
            Assert.assertThat(throwable, CoreMatchers.instanceOf(ServiceException.class));
            Assert.assertEquals(throwable.getMessage(), ApiMessage.NO_USER);
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account
     *      - 获取用户信息异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    public void testGetUserInfoException () throws Exception {
        ArrayList<Param> alist = new ArrayList<>();
            alist.add(new Param("username", null));
            alist.add(new Param("username", "a"));
            alist.add(new Param("username", "*asdf-="));
            alist.add(new Param("username", "12312311111111111111111"));
            alist.add(new Param("username", "suvanaa"));
            alist.add(new Param("email", null));
            alist.add(new Param("email", "asdfasfsfaasf@"));
            alist.add(new Param("email", "suvan@liushuwei.cn"));

        for (Param param : alist) {
            System.out.println("params: " + param.key + " = " + param.value);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account")
                                .param(param.key, (String) param.value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(ServiceException.class))
                        )
                );
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/state
     *      - 获取用户状态成功
     */
    @Test
    public void testGetActivateState() throws Exception {
        //already exist user
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/state")
                        .param("username", "suvan")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/state
     *      - 获取用户激活状态异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    public void testGetUserActivateStateException() throws Exception {
        String key = "username";
        String[] values = {null, "h", "123", "hello"};

        for (String value : values) {
            System.out.println("input param: " + key + " = " + value);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account/state")
                                .param(key, value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
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
     * 测试 /api/account/following
     *      - 获取用户所有主动关注人信息列表成功
     */
    @Test
    public void testListUserFollowingUserInfoSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/following")
                    .param("userid", "6")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/following
     *      - 获取用户所有主动关注人信息列表异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    public void testListUserFollowingUserInfoException() throws Exception {
        String key = "userid";
        String[] values = {null, "abc", "123*", "100000000000", "88888"};

        for (String value: values) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account/following")
                            .param(key, value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
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
     * 测试 /api/account/followed
     *      - 获取用户所有被关注信息列表成功
     *      - 列表字段
     *          - email, sex, birthday, position, description,
     *          - avator, state, create, userid, username
     */
    @Test
    public void testListUserFollowedUserInfoSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/followed")
                    .param("userid", "6")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/followed
     *      - 获取所有用户所有被关注人信息列表异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    public void testListUserFollowedUserInfoException() throws Exception {
        String key = "userid";
        String[] values = {null, "abc", "123*", "100000000000", "88888"};

        for (String value: values) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account/followed")
                            .param(key, value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"))
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
     * 测试 /api/account/login
     *      - 账户登陆成功
     *      - 列表字段
     *          - email, sex, birthday, position, description,
     *          - avator, state, create, userid, username
     */
    @Test
    public void testLoginAccountSuccess() throws Exception {
        //build request-body
        String username = "suvan";
        String password = "123456";
        String requestBody = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
        System.out.println("input request-body: " + requestBody);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.cookie().exists(ParamConst.AUTHENTICATION))
         .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
            .andReturn();

        util.isExistKeyItems(result, "state", "authentication");
        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/login
     *      - 账户登陆异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    public void testLoginAccountException() throws Exception {
        String[][] params = {
                {null, null}, {"suvan", null}, {null, "123456"},
                {"s", "123456"}, {"suvan*-=", "123456"}, {"noUser", "123456"},
                {"test@gmail.com", "123456"}, {"liushuwei0925@gmail.com", "xxxxxxx"},
                {"suvan", "123"}, {"suvan", "xxxxx"}};

        for (String[] param : params) {
            //build request-body
            String username = param[0];
            String password = param[1];
            String requestBody = "{\"username\":\"" + username + "\"," + "\"password\":\"" + password + "\"}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(ServiceException.class))
                        )
                );
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/logout
     *      - 注销账户成功
     *      - 需要权限：@LoginAuthorization
     */
    @Test
    public void testLogoutAccountSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/logout")
                        .cookie(util.getAlreadyLoginUserCookie())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/logout
     *      - 注销账户异常
     *          - [✔] no login
     */
    @Test
    public void testLogoutThrowException() throws Exception {
        try {
            //no Cookie, to do ApiInterceptor validate
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account/logout")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof PermissionException);
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_PERMISSION);
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/register
     *      - 注册账户成功
     */
    @Test
    @Transactional
    public void testRegisterAccountSuccess() throws Exception {
        //build params -> request-body
        String username = "apiTestUser";
        String password = "apiTestPassword";
        String email = "apiTestEmail@neubBs.com";
        String requestBody = "{\"username\":\"" + username + "\","
                + "\"password\":\"" + password + "\","
                + "\"email\":\"" + email + "\"}";
        System.out.println("input request-body: " + requestBody);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
            .andReturn();

        util.isExistKeyItems(result,
                "email", "sex", "birthday", "position", "description",
                "avator", "state", "createtime", "userid", "username"
        );

        //validate 'forum_user_action'
        UserDO user = userDAO.getUserByName(username);
        Assert.assertNotNull(userActionDAO.getUserAction(user.getId()));

        //so need to remove user personal directory in ftp server, because register account automatic create
        FtpUtil.deleteDirectory("/user/" + user.getId() + "-" + username);

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/register
     *      - 注册账户异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    @Transactional
    public void testRegisterAccountException() throws Exception {
        String[][] params = {
                {null, null, null},
                {null, "123456", "only@neubbs.com"}, {"only", null, "only@neubbs.com"}, {"only", "123456", null},
                {null, null, "onlu@neubbs.com"}, {null, "123456", null}, {"only", null, null},
                {"o", "123456", "only@neubbs.com"}, {"only$*=", "123456", "only@neubbs.com"},
                {"only", "1", "only@neubbs.com"},
                {"only", "123456", "only@"},
                {"suvan", "123456", "only@neubbs.com"}, {"only", "123456", "liushuwei0925@gmail.com"}
        };

        for (String[] arr : params) {
            String requestBody = "{\"username\":\"" + arr[0] + "\","
                    + "\"password\":\"" + arr[1] + "\","
                    + "\"email\":\"" + arr[2] + "\"}";
            System.out.println("request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
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
     * 测试 /api/account/update-profile
     *      - 修改用户基本信息成功
     *      - 需要权限：@LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testUpdateUserInfoSuccess() throws Exception {
        //build request-body
        Integer newSex = 1;
        String newBirthday = "1996-09-25";
        String newPosition = "Neusoft School";
        String newDescription = "hello neubbs";
        String requestBody = "{" + util.getJsonField("sex", newSex) + ", "
                + util.getJsonField("birthday", newBirthday) + ", "
                + util.getJsonField("position", newPosition) + ", "
                + util.getJsonField("description", newDescription) + "}";
        System.out.println("input request-body information: " + requestBody);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/update-profile")
                        .cookie(util.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.sex").value(newSex))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.birthday").value(newBirthday))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.position").value(newPosition))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.description").value(newDescription))
                .andReturn();

        util.isExistKeyItems(result,
                "email", "sex", "birthday", "position", "description",
                "avator", "state", "createtime", "userid", "username");

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/update-profile
     *      - 修改用户基本信息异常
     *          - [✔] no permission（@LoginAuthorization, @AccountActivation）
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    @Transactional
    public void testUpdateUserInfoException() throws Exception {
        String apiUrl = "/api/account/update-profile";

        //no login
        util.testApiThrowNoPermissionException(apiUrl, RequestMethod.POST, null);

        //account no activated
        util.testApiThrowNoPermissionException(apiUrl, RequestMethod.POST, util.getNoActivatedUserDO());

        //request
        String[][] params = {
                {null, null, null, null}, {null, "1996-09-25", "深圳", "描述"},
                {"12", "1996-09-25", "深圳", "描述"}, {"1", "1996-02-25 错误的时间", "深圳", "描述"}
        };

        for (String[] param: params) {
            Integer newSex = param[0] == null ? null : Integer.parseInt(param[0]);
            String newBirthday = param[1];
            String newPosition = param[2];
            String newDescription = param[3];
            String requestBody = "{" + util.getJsonField("sex", newSex) + ", "
                    + util.getJsonField("birthday", newBirthday) + ", "
                    + util.getJsonField("position", newPosition) + ", "
                    + util.getJsonField("description", newDescription) + "}";
            System.out.println("input request-body information: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/update-profile")
                                .cookie(util.getAlreadyLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CoreMatchers.notNullValue()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Throwable throwable = ne.getRootCause();
                Assert.assertThat(throwable,
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }
    }


    /**
     * 测试 /api/account/update-password
     *      - 账户修改密码成功
     *      - 需要权限：@LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testUpdateUserPassword() throws Exception {
        String username = "suvan";
        String newPassword = "new123456";
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + newPassword + "\"}";
        System.out.println("input request-body:" + requestBody);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/update-password")
                        .cookie(util.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //again validate database user
        UserDO user = userService.getUserInfoByName(username);
        String secretPassword = SecretUtil.encryptMd5(SecretUtil.encryptMd5(newPassword) + newPassword);
        Assert.assertEquals(secretPassword, user.getPassword());

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/update-password
     *      - 修改账户密码异常
     *          - [✔] no permission (@LoginAuthorization, @AccountActivation)
     *              - no login
     *              - account no activated
     *              - input user not match cookie user
     *          - [✔] request param error, no norm
     */
    @Test
    @Transactional
    public void testUserUpdatePasswordThrowException() throws Exception {
        // no login
        util.testApiThrowNoPermissionException("/api/account/update-password", RequestMethod.POST, null);

        //account no activated
        util.testApiThrowNoPermissionException("/api/account/update-password", RequestMethod.POST, util.getNoActivatedUserDO());

        //input username no match cookie user
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/account/update-password")
                        .cookie(util.getOtherAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"suvan\",\"password\":\"123456\"}")
                        .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_PERMISSION))
             .andExpect(MockMvcResultMatchers.jsonPath("$.mode").value(CoreMatchers.notNullValue()));

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof PermissionException);
            Assert.assertEquals(ApiMessage.NO_PERMISSION, ne.getRootCause().getMessage());
        }

        //request params error, no norm
        String[][] params = {
                {null, null}, {null, "123456"}, {"suvan", null},
                {"s", "123456"}, {"suvan*--0", "123456"},
                {"suvan", "123"}, {"suvan", "k"}
        };

        for (String[] param : params) {
            String username = param[0];
            String newPassword = param[1];
            String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + newPassword + "\"}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/update-password")
                                .cookie(util.getAlreadyLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.PARAM_ERROR))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertTrue(ne.getRootCause() instanceof ParamsErrorException);
                Assert.assertEquals(ApiMessage.PARAM_ERROR, ne.getRootCause().getMessage());
            }
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/update-email
     *      - 测试修改邮箱成功
     *      - 需要权限：@LoginAuthorization
     */
    @Test
    @Transactional
    public void testUpdateUserEmailSuccess() throws Exception {
        String username = "suvan";
        String newEmail = "newEmail@email.com";
        String requestBody = "{\"username\":\"" + username + "\",\"email\":\"" + newEmail + "\"}";
        System.out.println("input request-body: " + requestBody);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/update-email")
                        .cookie(util.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //again validate database user
        UserDO user = userService.getUserInfoByName(username);
        Assert.assertNotNull(user);
        Assert.assertEquals(newEmail, user.getEmail());

        util.printSuccessMessage();
    }

    /**
     *  测试 /api/account/update-email
     *      - 修改用户邮箱异常
     *          - [✔] no permission
     *              - no login
     *              - input user not match cookie user
     *          - [✔] request param error, no norm
     */
    @Test
    @Transactional
    public void testUpdateUserEmailException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/account/update-email", RequestMethod.POST, null);

        //request param error, no norm
        String[][] params = {
                {null, null}, {null, "test@test.com"}, {"test", null},
                {"t", "test@test.com"}, {"test*~", "test@test.com"}, {"test", "test@"}
        };

        for (String[] param: params) {
            String username = param[0];
            String newEmail = param[1];
            String requestBody = "{\"username\":\"" + username + "\",\"email\":\"" + newEmail + "\"}";

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/update-email")
                            .cookie(util.getAlreadyLoginUserCookie())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                            .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("message").value(ApiMessage.PARAM_ERROR))
                 .andExpect(MockMvcResultMatchers.jsonPath("model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(), CoreMatchers.instanceOf(ParamsErrorException.class));
                Assert.assertEquals(ApiMessage.PARAM_ERROR, ne.getRootCause().getMessage());
            }
        }

        //input user not match cookie user
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/account/update-email")
                        .cookie(util.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"otherUser\",\"email\":\"newUser@neubbs.com\"}")
                        .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_PERMISSION))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof PermissionException);
            Assert.assertEquals(ApiMessage.NO_PERMISSION, ne.getRootCause().getMessage());
        }

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/activate
     *      - 测试激活账户成功
     *          - 主线程结束，守护线程也会放弃执行
     *              - 所以需要阻塞主线程，当发送邮件线程执行完毕后，再继续执行
     *              - taskExecutor.execute() 激活的都是守护线程
     */
    @Test
    @Transactional
    public void testActivateAccountSuccess() throws Exception {
        String email = "suvan-liu@qq.com";
        String requestBody = "{\"email\":\"" + email + "\"}";
        System.out.println("input request-body: " + requestBody);

        //register new user, to test
        userService.registerUser("testUser", "123456", email);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/activate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

       /*
        * need to blocking main thread, otherwise not send mail
        *   - loop to cyclic, active thread count > 0,
        *   - most to block main thread 60s
        */
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) webApplicationContext.getBean("taskExecutor");
        int timer = 1;
        while (taskExecutor.getActiveCount() > 0 && timer < 60) {
            Thread.sleep(1000);
            System.out.println("already wait " + (timer++) + "s");
        }
        System.out.println("send activate mail to '" + email + "'success !");

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/activate
     *      - 测试激活账户异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     *          - [✔] 60s send mail interval time for the same account
     */
    @Test
    @Transactional
    public void testActivateAccountException() throws Exception{
        //request param error, no norm
       String [] params = {null, "123", "test@", "liushuwei0925@gmail.com"};

        for (String email: params) {
            String requestBody = "{\"email\":\"" + email + "\"}";
            System.out.println("input post param request-body: " +requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/activate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                            .accept(MediaType.APPLICATION_JSON)
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

       //test again send mail, exist interval time（not sent repeatedly）
       String email = "testActivate@test.com";
       userService.registerUser(email.substring(0, email.indexOf("@")), "123456", email);

       //cache server set 60s send mail interval time for the same account
       ICacheService redisService = (ICacheService) webApplicationContext.getBean("redisServiceImpl");
       redisService.saveUserEmailKey(email);

       mockMvc.perform(
               MockMvcRequestBuilders.post("/api/account/activate")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"email\":\"" + email + "\"}")
                       .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.WAIT_TIMER))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.timer").value(CoreMatchers.notNullValue()));

        System.out.println("send email exist interval time limit , test success!");

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/validate
     *      - 验证 Token（来自激活邮件），激活用户成功
     */
    @Test
    @Transactional
    public void testValidateActivateTokenSuccess() throws Exception {
        //register new user
        UserDO testUser = userService.registerUser("testUser", "123456", "testUser@test.com");

        //build token
        //String token = ((ISecretService) webApplicationContext.getBean("secretServiceImpl")).generateValidateEmailToken(testUser.getEmail());
        String token = SecretUtil.encodeBase64(testUser.getEmail() + "-" + StringUtil.getTodayTwentyFourClockTimestamp());

        //validate token and activate user
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/validate")
                        .param("token", token)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //again validate database user activated state
        Assert.assertTrue(userService.getUserInfoByEmail(testUser.getEmail()).getState() == 1);

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/validate
     *      - 验证 Token（来自激活邮件），激活用户出现异常
     *          - [✔] util class exception
     *              - encode Base64 failure
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    @Transactional
    public void testValidateActivateTokenException() throws Exception {
        //build request param token
        String [] parmas = {
                "1234", "awsfasdf", "asdfasdf-qweqwe", "13412-ASDFAF-qqqq",
                "abcdefg-" + StringUtil.getTodayTwentyFourClockTimestamp(),
                "test@neubs.com-" + System.currentTimeMillis(),
                "test@nuebbs.com-" + StringUtil.getTodayTwentyFourClockTimestamp(),
                "liushuwei0925@gmail.com-" + StringUtil.getTodayTwentyFourClockTimestamp(),
        };

        for (String param: parmas) {
            try {
                String token = SecretUtil.encodeBase64(param);
                System.out.println("input token = " + token);

                mockMvc.perform(
                           MockMvcRequestBuilders.get("/api/account/validate")
                                   .param("token", token)
                   ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
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
     * 测试 /api/account/captcha
     *      - 自动生成图片验证码成功
     */
    @Test
    public void generateCaptchaPictureSuccess() throws Exception {
        //generate web page captcha picture and validate
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/captcha")
                        .accept(MediaType.IMAGE_JPEG_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.content().contentType("image/jpeg"))
         .andReturn();

        //verify session attribute
        Assert.assertNotNull(result.getRequest().getSession().getAttribute(SetConst.SESSION_CAPTCHA));

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/check-captcha
     *      - 检查用户输入验证码匹配成功
     *          - 设置 session 属性，再传入用户输入验证码
     */
    @Test
    public void testValidateCaptchaSuccess() throws Exception {
        //get captcha service bean object
        ICaptchaService captchaService = (ICaptchaService) this.webApplicationContext.getBean("captchaServiceImpl");
        String captcha = captchaService.getCaptchaText();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/validate-captcha")
                    .sessionAttr(SetConst.SESSION_CAPTCHA, captcha)
                    .param(ParamConst.CAPTCHA, captcha)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/check-captcha
     *      - 检查用户输入验证码匹配异常
     *          - [✔] request param error, no norm
     *          - [✔] not pass '/api/account/captcha' api to generate captcha picture, session not exist captcha text
     */
    @Test
    public void testValidateCaptchaException() throws Exception {
        //build captcha
        ICaptchaService captchaService = (ICaptchaService) webApplicationContext.getBean("captchaServiceImpl");
        String captcha = captchaService.getCaptchaText();

        //1. request param error and 2. input captcha error
        String[] params = {null, "123", "abc", "ABC", "123abcABC", "0*=123", "55555"};
        for (String param: params) {
            System.out.println("input captcha = " + param);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account/validate-captcha")
                                .sessionAttr(SetConst.SESSION_CAPTCHA, captcha)
                                .param(ParamConst.CAPTCHA, param)
                   ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.CAPTCHA_INCORRECT))
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

       //not pass '/api/account/validate' api to generate captcha picture, session not exist captcha text
       try {
           mockMvc.perform(
                   MockMvcRequestBuilders.get("/api/account/validate-captcha")
                        .param(ParamConst.CAPTCHA, captcha)
           ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(ApiMessage.CAPTCHA_INCORRECT))
            .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

       } catch (NestedServletException ne) {
           Assert.assertSame(ServiceException.class, ne.getRootCause().getClass());
           Assert.assertEquals(ApiMessage.NO_GENERATE_CAPTCHA, ne.getRootCause().getMessage());
       }

       util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/forget-password
     *      - 忘记密码成功
     *          - 重新设置临时密码，并发送邮件提示
     *          - 主线程等待，邮件线程发送完毕再继续执行
     */
    @Test
    @Transactional
    public void testForgetPasswordSuccess() throws Exception {
        String email = "liushuwei0925@gmail.com";
        String requestBody = "{\"email\":\"" + email + "\"}";
        System.out.println("input request-body:" + requestBody);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/forget-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //wait send mail thread send success
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) webApplicationContext.getBean("taskExecutor");
        int timer = 1;
        while (taskExecutor.getActiveCount() > 0 && timer < 60) {
            Thread.sleep(1000);
            System.out.println("already wait " + (timer++) + "s");
        }
        System.out.println("send alter temporary password mail to '" + email + "' success!");

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/forget-password
     *      - 测试忘记密码异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    @Transactional
    public void testForgetPasswordException() throws Exception {
        String[] params = {null, "test@", "test@neubbs.com"};

        for (String email: params) {
            String requestBody = "{\"email\":\"" + email + "\"}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/forget-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
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
     * 测试 /api/account/following
     *      - 测试关注用户成功
     *          - 【自动变化】未关注 -> 关注，关注 -> 未关注
     *      - 需要权限：@LoginAuthorization，@AccountActivation
     */
    @Test
    @Transactional
    public void testFollowingUserSuccess() throws Exception {
        int userId = 1;
        String requestBody = "{" + util.getJsonField("userid", userId) + "}";
        System.out.println("input request-body = " + requestBody);

        //get IUserActionDAO Object bean
        IUserActionDAO userActionDAO = (IUserActionDAO) webApplicationContext.getBean("IUserActionDAO");

        mockMvc.perform(
               MockMvcRequestBuilders.post("/api/account/following")
                       .cookie(util.getAlreadyLoginUserCookie())
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(requestBody)
                       .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.followingUserId").isArray());

        //validate database(id = 6 -> following -> id = 1)
        Assert.assertEquals("[1]", userActionDAO.getUserActionFollowingUserIdJsonArray(6));
        Assert.assertEquals("[6]", userActionDAO.getUserActionFollowedUserIdJsonArray(1));

         mockMvc.perform(
               MockMvcRequestBuilders.post("/api/account/following")
                       .cookie(util.getAlreadyLoginUserCookie())
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(requestBody)
                       .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.followingUserId").isArray());

        //validate database(id = 6 -> cancel following -> id = 1)
        Assert.assertEquals("[]", userActionDAO.getUserActionFollowingUserIdJsonArray(6));
        Assert.assertEquals("[]", userActionDAO.getUserActionFollowedUserIdJsonArray(1));

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/account/following
     *      - 测试关注用户异常
     *          - [✔] no permission
     *              - no login
     *              - the account not activated
     *          - [✔] request param error, no norm
     *          - [✔] server exception
     *              - following user no exist
     */
    @Test
    @Transactional
    public void testFollowingUserException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/account/following", RequestMethod.POST, null);

        //the account not activate
        util.testApiThrowNoPermissionException("/api/account/following", RequestMethod.POST, util.getNoActivatedUserDO());

        //request param error, no norm
        String[] params = {null, "kkk", "****", "123test", "888888888888888888"};
        for (String userId: params) {
            String requestBody = "{" + util.getJsonField("userid", userId) + "}";
            System.out.println("input request-body = " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/following")
                                .cookie(util.getAlreadyLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.PARAM_ERROR))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ClassCastException.class)
                        )
                );
            }
        }

        //service exception
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/account/following")
                            .cookie(util.getAlreadyLoginUserCookie())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"userid\":111111}")
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_USER))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));
        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof ServiceException);
            Assert.assertEquals(ApiMessage.NO_USER, ne.getRootCause().getMessage());
        }

        util.printSuccessMessage();
    }
}
