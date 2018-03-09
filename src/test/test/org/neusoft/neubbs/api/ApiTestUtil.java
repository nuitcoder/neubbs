package test.org.neusoft.neubbs.api;

import com.alibaba.fastjson.JSON;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.PermissionException;
import org.neusoft.neubbs.utils.SecretUtil;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

/**
 * Api 测试工具
 *      - 自定义工具函数
 *      - 仅提供给 api 包下的接口测试用例使用，函数访问修饰符 default(当前类，同包)
 *
 * @author Suvan
 */
public class ApiTestUtil {

    private MockMvc mockMvc;

    private static class ApiTestUtilHolder {
        private static final ApiTestUtil INSTANCE = new ApiTestUtil();
    }

    private ApiTestUtil(){ }

    static ApiTestUtil getInstance(MockMvc mockMvc) {
        ApiTestUtilHolder.INSTANCE.mockMvc = mockMvc;
        return ApiTestUtilHolder.INSTANCE;
    }

    /**
     * 打印成功通过 Test 函数消息
     */
    void printSuccessMessage() {
        String currentExecuteMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
        System.out.println("***************** 【" + currentExecuteMethod + "()】 " + " pass the test ****************");
    }

    /**
     * 获取已经登陆用户 Cookie
     *      - 设置 Suvan 账户
     *      - 具备已激活和管理员权限
     *
     * @return Cookie 已经登录用户Cookie
     */
    Cookie getAlreadyLoginUserCookie() {
        UserDO user = new UserDO();
            user.setId(6);
            user.setName("suvan");
            user.setRank(SetConst.RANK_ADMIN);
            user.setState(SetConst.ACCOUNT_ACTIVATED_STATE);

        return new Cookie(ParamConst.AUTHENTICATION, SecretUtil.generateUserInfoToken(user));
    }

    /**
     * 获取未激活的用户对象
     *
     * @return UserDO 未激活的用户对象
     */
    UserDO getNoActivatedUserDO() {
        UserDO user = new UserDO();
            user.setId(6);
            user.setName("suvan");
            user.setRank(SetConst.RANK_ADMIN);
            user.setState(SetConst.ACCOUNT_NO_ACTIVATED_STATE);

        return user;
    }

    /**
     * 获取非管理员的用户对象
     *
     * @return UserDO 非管理员的用户对象
     */
    UserDO getNoAdminRankUserDO() {
        UserDO user = new UserDO();
            user.setId(6);
            user.setName("suvan");
            user.setRank(SetConst.RANK_USER);
            user.setState(SetConst.ACCOUNT_ACTIVATED_STATE);

        return user;
    }

    /**
     * 获取其他已登陆用户的 Cookie
     *      - 默认是 suvan 用户，则该函数获取获取另外一个用户对象
     *
     * @return Cookie 其他已登陆用户的Cookie
     */
    Cookie getOtherAlreadyLoginUserCookie() {
        UserDO otherUser = new UserDO();
            otherUser.setId(123);
            otherUser.setName("noExistUser");
            otherUser.setRank(SetConst.RANK_USER);
            otherUser.setState(SetConst.ACCOUNT_ACTIVATED_STATE);

        return new Cookie(ParamConst.AUTHENTICATION, SecretUtil.generateUserInfoToken(otherUser));
    }

    /**
     * 生成 JSON 字段
     *      - 传入单个 key-value，生成 JSON 格式字符串
     *      - 字符串对象需加 ""， 其他 Object 对象不用
     *
     * @param key 键
     * @param value 值
     * @return String JSON字段
     */
    String getJsonField(String key, Object value) {
        if (value == null) {
            value = null;
        } else if (value instanceof String) {
            value = "\"" + value + "\"";
        }
        return "\"" + key + "\":" + value;
    }

    /**
     * 检查存在 Key 选项
     *      - 检测 MvcResult 的 mode 字段，存在指定 Key items
     *
     * @param result MvcResult 结果集
     * @param keyItems 多个 Key Item（可变参数）
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    void isExistKeyItems(MvcResult result, String... keyItems) throws UnsupportedEncodingException {
        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());
        Map resultModelMap = (Map) resultMap.get("model");

        Assert.assertEquals(keyItems.length, resultModelMap.size());
        Assert.assertThat((Set<String>) resultModelMap.keySet(), CoreMatchers.hasItems(keyItems));
    }

    /**
     * 确认结果集 Model Map 应该拥有以下 Key 选项
     *
     * @param map 需要判断的键值对
     * @param keyItems key选项
     * @throws Exception 所有异常
     */
    void confirmMapShouldHavaKeyItems(Map map, String... keyItems) throws Exception {
        //compare length and judge exist item
        Assert.assertEquals(keyItems.length, map.size());
        Assert.assertThat((Set<String>) map.keySet(), CoreMatchers.hasItems(keyItems));
    }

    /**
     * 访问 api，抛出用户无权限异常
     *      - 主要测试三个权限: @LoginAuthorization @AccountActivation @AdminRank
     *      - 若未抛出指定异常，则可能访问到空页面（Controller 的接口设定了访问限制，例如：consumes 和 参数列表）
     *
     * @param apiUrl api地址
     * @param requestMethod http请求方式
     * @param user 用户对象（用于构建Cookie）
     */
    void testApiThrowNoPermissionException(String apiUrl, RequestMethod requestMethod, UserDO user) {
        //set post | get
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get(apiUrl);
        if (RequestMethod.POST.equals(requestMethod)) {
            mockRequest = MockMvcRequestBuilders.post(apiUrl);
        }

        //set content type
        mockRequest.contentType(MediaType.APPLICATION_JSON);

        //upload file type, to change http request
        if (apiUrl.contains("/api/file/")) {
            mockRequest = MockMvcRequestBuilders
                    .fileUpload(apiUrl)
                    .file(new MockMultipartFile("avatarImageFile", "testAvatarFile.jpg", "image/jpg", new byte[0]));
            mockRequest.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
        }

        if (user != null) {
            mockRequest.cookie(new Cookie(ParamConst.AUTHENTICATION, SecretUtil.generateUserInfoToken(user)));
        }

        try {
            mockMvc.perform(
                    mockRequest
                        .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_PERMISSION))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));
        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof PermissionException);

            //the account no activated
            if (user != null && user.getState() == SetConst.ACCOUNT_NO_ACTIVATED_STATE) {
                Assert.assertEquals(ApiMessage.NO_ACTIVATE, ne.getRootCause().getMessage());
            } else {
                Assert.assertEquals(ApiMessage.NO_PERMISSION, ne.getRootCause().getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
           throw new RuntimeException("no throw expected exception");
        }
    }
}
