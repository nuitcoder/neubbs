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
import org.neusoft.neubbs.controller.handler.DynamicSwitchDataSourceHandler;
import org.neusoft.neubbs.exception.AccountErrorException;
import org.neusoft.neubbs.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.exception.FileUploadErrorException;
import org.neusoft.neubbs.exception.FtpServiceErrorException;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.utils.FtpUtil;
import org.neusoft.neubbs.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.util.List;

/**
 * File api 测试
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-context.xml"}),
        @ContextConfiguration(locations = {"classpath:spring-mvc.xml"})
})
public class FileControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private IUserService userService;


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
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 【/api/file/image】 test upload user avator image success
     */
    @Test
    @Transactional
    public void testUploadUserAvatorImageSuccess() throws Exception {
        //build MultipartFile type(input name, 文件名，文件类型，内容字节)
        String htmlInputName = "avatorImage";
        String uploadFileName = "testUploadImage";
        byte [] fileBytes = new byte[1024 * 1];
        MockMultipartFile[] files = {
                new MockMultipartFile(htmlInputName, uploadFileName + ".jpg", "image/jpg", fileBytes),
                new MockMultipartFile(htmlInputName, uploadFileName + ".png", "image/png", fileBytes),
                new MockMultipartFile(htmlInputName, uploadFileName + ".jpeg", "image/jpeg", fileBytes),
        };

        //build login token
        String authentication = JwtTokenUtil.createToken(userService.getUserInfoByName("suvan"));

        //upload user image file
        for (MockMultipartFile file: files) {
            mockMvc.perform(
                    MockMvcRequestBuilders.fileUpload("/api/file/avator")
                            .file(file)
                            .cookie(new Cookie(ParamConst.AUTHENTICATION, authentication))
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.UPLOAD_SUCCESS))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

           System.out.println("upload " + file.getOriginalFilename() + " success!");
        }

        /*
         * delete already upload user image file
         */
        //【local file】(path: /webapp/WEB-INF/file/user/image/....)
//        String serverUploadFilePath = webApplicationContext.getServletContext()
//                .getRealPath(neubbsConfig.getUserImageUploadPath());
//        File directory = new File(serverUploadFilePath);
//        File[] allFiles = directory.listFiles();
//        for (File file: allFiles) {
//            if (file.getName().contains("testUploadImage")) {
//                file.deleteOnExit();
//                System.out.println("delete " +file.getName() + " success!");
//            }
//        }

        //【ftp server file】(path: ftp-url/user/userid-username/avator/)
        UserDO user = userService.getUserInfoByName("suvan");
        String ftpAvatorDirectoryPath = "/user/" + user.getId() + "-" + user.getName() + "/avator/";
        List<String> listFileName = FtpUtil.listDirectoryFileName(ftpAvatorDirectoryPath);
        for (String fName: listFileName) {
            if (fName.contains(uploadFileName)) {
                FtpUtil.delete(ftpAvatorDirectoryPath, fName);
                System.out.println("success delete " + fName + " !");
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/file/image】 test upload user avator image throw exception
     */
    @Test
    @Transactional
    public void testUploadUserAvatorImageThrowException() throws Exception {
        String apiUrl = "/api/file/avator";

        //no login
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.fileUpload(apiUrl)
            ).andExpect(MockMvcResultMatchers.status().is(200))
             .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_PERMISSION))
             .andExpect(MockMvcResultMatchers.jsonPath("$.success").exists());

        }catch (NestedServletException ne) {
            Assert.assertThat(ne.getRootCause(), CoreMatchers.instanceOf(AccountErrorException.class));
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_PERMISSION);
        }

        //alread login contain cookie, upload exception picture
        String htmlInputName = "avatorImage";
        String fileName = "exceptionImage.jpg";
        String fileType = "image/jpg";
        byte[] fileBytes = new byte[1024 * 1024 * 1];
        MockMultipartFile[] files = {
                new MockMultipartFile(htmlInputName, fileName, fileType, new byte[0]),
                new MockMultipartFile(htmlInputName, "exceptionImage.ps", "image/ps" , fileBytes),
                new MockMultipartFile(htmlInputName, "exceptionImage.jpg", "jpg" , fileBytes),
                new MockMultipartFile(htmlInputName, "exceptionImage.avi", "avi" , fileBytes),
                new MockMultipartFile(htmlInputName, fileName, fileType ,new byte[1024 * 1024 * 6])
        };

        String authentication = JwtTokenUtil.createToken(userService.getUserInfoByName("suvan"));

        for (MockMultipartFile file: files) {
            try {
                mockMvc.perform(
                       MockMvcRequestBuilders.fileUpload("/api/file/avator")
                            .file(file)
                            .cookie(new Cookie(ParamConst.AUTHENTICATION, authentication))
                ).andExpect(MockMvcResultMatchers.status().is(200))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(FileUploadErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class),
                                CoreMatchers.instanceOf(DatabaseOperationFailException.class),
                                CoreMatchers.instanceOf(FtpServiceErrorException.class)
                        )
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }
}
