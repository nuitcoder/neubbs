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
 *      - 连接 FTP 服务器
 *      - 退出连接
 *      - 移动到服务器路径
 *
 *  public
 *      - 创建目录
 *      - 获取服务器路径文件名列表
 *      - 上传文件
 *      - 删除（文件 or 目录）
 *      - 删除目录（及内部子文件夹，文件）
 *
 * 注意：
 *      每次使用连接都要断开，否则线程会一直保持 ftp 连接，其余用户无法使用
 *
 * @author Suvan
 */
public final class FtpUtil {

    private FtpUtil() { }

    private static String ftpIp;
    private static Integer ftpPort;
    private static String ftpUsername;
    private static String ftpPassword;

    private static volatile FTPClient ftpClient;

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
     *      - 连接 -> 登陆 -> 设置文件类型 -> 判断响应码
     *
     * @throws IOException IO异常
     */
    private static void connect() throws IOException {
        ftpClient.connect(ftpIp, ftpPort);
        ftpClient.login(ftpUsername, ftpPassword);

        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            throw new IOException("FTP 服务器连接失败!");
        }
    }

    /**
     * 退出连接
     *
     * @throws IOException IO异常
     */
    private static void destroy() throws IOException {
        if (ftpClient != null) {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    /**
     * 移动到服务器路径
     *      - 移动到指定目录，若不存在，则抛出异常
     *
     * @param serverDirectoryPath 服务器目录
     * @throws IOException IO异常
     */
    private static void moveServerPath(String serverDirectoryPath) throws IOException {
        //no exist directory, return false
        if (!ftpClient.changeWorkingDirectory(serverDirectoryPath)) {
            throw new IOException("FTP 服务器，不存在指定路径");
        }
    }


    /**
     * 创建目录
     *      -  /为分隔，支持创建层级目录（例：/user/userId-username/avatar）
     *
     * @param serverDirectoryPath 服务器目录路径（默认以根节点为开始）
     */
    public static void createDirectory(String serverDirectoryPath) throws IOException {
        connect();

        String[] paths = serverDirectoryPath.split("/");
        for (String path: paths) {
            if ("".equals(path)) {
                continue;
            }
            //already exist directory, return false,
            ftpClient.makeDirectory(path);
            ftpClient.changeWorkingDirectory(path);
        }

        //return root path
        ftpClient.changeWorkingDirectory("/");

        destroy();
    }

    /**
     * 获取服务器路径文件名列表
     *      - 指定目录下所有文件名
     *      - 不包含目录
     *
     * @param serverDirectoryPath 服务器路径
     * @return List<String> 文件名集合
     * @throws IOException IO异常
     */
    public static List<String> listServerPathFileName(String serverDirectoryPath) throws IOException {
        connect();
        moveServerPath(serverDirectoryPath);

        List<String> fileNameList = new ArrayList<>();
        FTPFile[] files = ftpClient.listFiles();
        for (FTPFile file: files) {
            if (file.isFile()) {
                fileNameList.add(file.getName());
            }
        }

        destroy();

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
    public static void uploadFile(String serverDirectoryPath, String serverFileName,
                                  InputStream uploadFileInputStream) throws IOException {
        connect();

        ftpClient.changeWorkingDirectory(serverDirectoryPath);
        ftpClient.storeFile(serverFileName, uploadFileInputStream);

        uploadFileInputStream.close();
        destroy();
    }

    /**
     * 删除
     *      - 删除文件
     *      - 删除目录（不包含目录）
     *
     * @param serverDirectoryPath 服务器目录路径
     * @param serverFileName 服务器文件名（若删除目录，此字符串可为 null）
     * @throws IOException IO异常
     */
    public static void delete(String serverDirectoryPath, String serverFileName) throws IOException {
        connect();
        moveServerPath(serverDirectoryPath);

        serverDirectoryPath = StringUtil.completeBeforeAfterSprit(serverDirectoryPath);
        if (serverFileName == null) {
            ftpClient.removeDirectory(serverDirectoryPath);
            return;
        }
        ftpClient.deleteFile(serverDirectoryPath + "/" + serverFileName);

        destroy();
    }

    /**
     * 删除目录
     *      - 及内部子文件夹，文件
     *
     * @param serverDirectoryPath 服务器目录路径
     * @throws IOException IO异常
     */
    public static void deleteDirectory(String serverDirectoryPath) throws IOException {
        connect();
        moveServerPath(serverDirectoryPath);

        //complete path(add '/')
        String path = StringUtil.completeBeforeAfterSprit(serverDirectoryPath);

        FTPFile[] files = ftpClient.listFiles(path);
        if (files != null) {
            for (FTPFile file: files) {
                if (file.isDirectory()) {
                    //recursive remove all directory
                    deleteDirectory(path + "/" + file.getName());

                    //must switch to father directory (otherwise can not delete)
                    ftpClient.changeWorkingDirectory(path.substring(0, path.lastIndexOf("/")));
                    ftpClient.removeDirectory(path);
                } else {
                    //remove file
                    ftpClient.deleteFile(path + "/" + file.getName());
                }
            }
        }

        ftpClient.changeWorkingDirectory(path.substring(0, path.lastIndexOf("/")));
        ftpClient.removeDirectory(path);

        destroy();
    }
}
