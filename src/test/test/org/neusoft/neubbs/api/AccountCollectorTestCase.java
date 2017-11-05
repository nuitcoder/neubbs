package test.org.neusoft.neubbs.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Account api 测试
 *      - 声明 Spirng
 *      - 开启 web 服务
 *      - 设置配置文件(需设置 applicatonContext.xml 和 mvc.xm,否则会报错)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-context.xml"}),
        @ContextConfiguration(locations = {"classpath:spring-mvc.xml"})
})
public class AccountCollectorTestCase {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    static class Param {
        String key;
        Object value;

        Param (String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 测试 Accout api（获取用户信息）
     */
    @Test
    public void testAccount() {
        ArrayList<Param> alist = new ArrayList<>();
            alist.add(new Param(null, null));
            alist.add(new Param(null, ""));
            alist.add(new Param("username", null));
            alist.add(new Param("username", 1));
            alist.add(new Param("username", ""));
            alist.add(new Param("username", "asdfsf"));
            alist.add(new Param("username", "suvan"));
            alist.add(new Param("username", "safasf@asdf.casdf"));
            alist.add(new Param("username", "liushuwei0925@gmail.com"));
            alist.add(new Param("email", null));
            alist.add(new Param("email", ""));
            alist.add(new Param("email", "asdfas@asdf"));
            alist.add(new Param("email", "liushuwei0925@gmail.com"));


        for(Param param: alist) {
            System.out.println("*************************** Start ****************************");
            System.out.println("[输入参数] "+ param.key + " = " + param.value);
            try{
                MvcResult result = mockMvc.perform(
                        get("/api/account").param(param.key, (String)param.value)
                ).andReturn();
                System.out.println("[结果] SUCCESS ---> " + result.getResponse().getContentAsString());

            } catch (Exception e){
                String errorMessage= e.getMessage().substring(e.getMessage().lastIndexOf(":") + 1);
                ResponseJsonDTO responseJson = new ResponseJsonDTO(AjaxRequestStatus.FAIL, errorMessage);
                System.out.println("[结果] FAIL ---> " + JsonUtil.toJSONStringByObject(responseJson));

            }
        }
    }
}
