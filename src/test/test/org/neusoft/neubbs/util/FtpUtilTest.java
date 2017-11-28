package test.org.neusoft.neubbs.util;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.utils.FtpUtil;

import java.io.File;
import java.io.FileInputStream;

/**
 * FtpUtil 测试类
 *
 * @author Suvan
 */
@RunWith(JUnit4.class)
public class FtpUtilTest {
    /**
     * 测试创建指定目录
     */
    @Ignore
    public void testCreateDirectory() throws Exception {
        String[] paths = {
          "test","/test/test1", "test/test2/", "test/test3"
        };

        for (String serverFilePath: paths) {
            FtpUtil.createDirectory(serverFilePath);
        }
    }

    /**
     * 测试上传文件
     */
    @Ignore
    public void testUploadFileToFtpRemoteStorage() throws Exception {
        File file = new File("D:\\suvan.png");
        String serverFilePath = "/testUser/user";

        FtpUtil.createDirectory(serverFilePath);
        FtpUtil.uploadFile(serverFilePath, "avator.png", new FileInputStream(file));
    }

    /**
     * 测试删除指定文件
     */
    @Ignore
    public void testDeleteFile() throws Exception {
        testUploadFileToFtpRemoteStorage();

        FtpUtil.deleteFile("/testUser/user","avator.png");
    }

    /**
     * 测试删除目录内所有文件
     */
    @Ignore
    public void tesDeleteDirectoryAllContent() throws Exception {
        testCreateDirectory();
        testUploadFileToFtpRemoteStorage();

        FtpUtil.deleteDirectory("/test");
        FtpUtil.deleteDirectory("/testUser");
    }
}
