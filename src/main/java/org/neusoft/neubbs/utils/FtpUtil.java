package org.neusoft.neubbs.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * FTP 工具类
 *  private
 *      - 连接服务器
 *      - 保持连接
 *      - 建行擦是否存在服务器目录
 *
 *  public
 *      - 获取 FTPClient 对象实例
 *      - 退出连接
 *      - 创建目录
 *      - 获取指定目录所有文件集合（不包含目录）
 *      - 上传文件
 *      - 删除文件
 *      - 删除目录（及内部子文件夹,文件）
 *
 * @author Suvan
 */
public final class FtpUtil {

    private FtpUtil() { }

    private static String ftpIp;
    private static Integer ftpPort;
    private static String ftpUsername;
    private static String ftpPassword;

    private static FTPClient ftpClient;

    static {
        ftpClient = new FTPClient();

        Resource resource = new ClassPathResource("/neubbs.properties");
        try {
            //read /resources/neubbs.properties
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
                ftpIp = props.getProperty("ftp.ip");
                ftpPort = Integer.parseInt(props.getProperty("ftp.port"));
                ftpUsername = props.getProperty("ftp.username");
                ftpPassword = props.getProperty("ftp.password");

            //connect ftp server
            connect();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 连接 FTP 服务器
     *
     * @throws IOException IO异常
     */
    private static void connect() throws IOException {
            ftpClient.connect(ftpIp, ftpPort);
            ftpClient.login(ftpUsername, ftpPassword);

            //二进制方式传输
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                throw new IOException("FTP 服务器连接失败!");
            }
    }

    /**
     * 保持连接
     *      - 连接已取消，则自动重连
     *
     * @throws IOException IO异常
     */
    private static void keepConnect() throws IOException {
        if (!ftpClient.isConnected()) {
            connect();
        }
    }

    /**
     * 检查是否存在服务器目录
     *
     * @param serverDirectoryPath 服务器目录
     * @throws IOException IO异常
     */
    private static void checkIsExistServerDirectory(String serverDirectoryPath) throws IOException {
        if (!ftpClient.changeWorkingDirectory(serverDirectoryPath)) {
            throw new IOException("FTP 服务器，不存在指定路径");
        }
    }

    /**
     * 获取 FTPClient 对象实例
     *
     * @return FTPClient 对象实例
     * @throws IOException IO异常
     */
    public static FTPClient getFTPClientInstance() throws IOException {
        if (ftpClient == null) {
            ftpClient = new FTPClient();
            connect();
        }

        return ftpClient;
    }

    /**
     * 退出连接
     *
     * @throws IOException IO异常
     */
    public static void destory() throws IOException {
        if (ftpClient != null) {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    /**
     * 创建目录
     *      -  /为分隔，创建层级目录 例：/user/5-suvan/avator
     *
     * @param serverDirectoryPath 待创建的服务器目录路径（举例：/user/5-suvan/avator）
     */
    public static void createDirectory(String serverDirectoryPath) throws IOException {
        keepConnect();

        String[] paths = serverDirectoryPath.split("/");
        for (String path: paths) {
            if ("".equals(path)) {
                continue;
            }

            //no exist directory, return false and create success, return true
            ftpClient.makeDirectory(path);
            ftpClient.changeWorkingDirectory(path);
        }

        //return root path
        ftpClient.changeWorkingDirectory("/");
    }

    /**
     * 获取指定路径下所有文件名（不包含目录）
     *
     * @param serverDirectoryPath 服务器目录路径
     * @return List<String> 文件名集合
     * @throws IOException IO异常
     */
    public static List<String> listDirectoryFileName(String serverDirectoryPath) throws IOException {
        keepConnect();
        checkIsExistServerDirectory(serverDirectoryPath);

        List<String> fileNameList = new ArrayList<>();
        FTPFile[] files = ftpClient.listFiles();
        for (FTPFile file: files) {
            if (file.isFile()) {
                fileNameList.add(file.getName());
            }
        }

        return fileNameList;
    }

    /**
     * 上传文件
     *      - 创建文件目录，并切换文件目录准备上传
     *      - 上传文件
     *
     * @param serverDirectoryPath 服务器目录路径（根路径为: /）
     * @param serverFileName 服务器文件名（上传后生成文件名）
     * @param uploadFileInputStream 待上传文件的输入流（可传入 new FileInputStream(File)）
     * @throws IOException IO异常
     */
    public static void uploadFile(String serverDirectoryPath, String serverFileName, InputStream uploadFileInputStream)
            throws IOException {
        keepConnect();

        ftpClient.changeWorkingDirectory(serverDirectoryPath);
        ftpClient.storeFile(serverFileName, uploadFileInputStream);

        uploadFileInputStream.close();
    }

    /**
     * 删除指定文件
     *
     * @param serverDirectoryPath 服务器目录路径
     * @param serverFileName 服务器文件名（若删除目录，此字符串可为 null）
     * @throws IOException IO异常
     */
    public static void delete(String serverDirectoryPath, String serverFileName) throws IOException {
        keepConnect();
        checkIsExistServerDirectory(serverDirectoryPath);

        serverDirectoryPath = StringUtil.completeBeforeAfterSprit(serverDirectoryPath);
        if (serverFileName == null) {
            ftpClient.removeDirectory(serverDirectoryPath);
            return;
        }
        ftpClient.deleteFile(serverDirectoryPath + "/" + serverFileName);
    }

    /**
     * 删除目录（及内部子文件夹,文件）
     *
     * @param serverDirectoryPath 服务器目录路径
     * @throws IOException IO异常
     */
    public static void deleteDirectory(String serverDirectoryPath) throws IOException {
        keepConnect();
        checkIsExistServerDirectory(serverDirectoryPath);

        //complete /
        String path = StringUtil.completeBeforeAfterSprit(serverDirectoryPath);

        FTPFile[] files = ftpClient.listFiles(path);
        if (files != null) {
            for (FTPFile file: files) {
                if (file.isDirectory()) {
                    //递归删除目录所有文件
                    deleteDirectory(path + "/" + file.getName());

                    //切换到父目录，删除文件夹（不切换无法删除目录）
                    ftpClient.changeWorkingDirectory(path.substring(0, path.lastIndexOf("/")));
                    ftpClient.removeDirectory(path);
                } else {
                    //删除特定文件
                    ftpClient.deleteFile(path + "/" + file.getName());
                }
            }
        }

        ftpClient.changeWorkingDirectory(path.substring(0, path.lastIndexOf("/")));
        ftpClient.removeDirectory(path);
    }
}
