package org.neusoft.neubbs.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.exception.FtpException;
import org.neusoft.neubbs.exception.UtilClassException;
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
 *      - 创建目录
 *      - 获取指定路径文件名列表
 *      - 上传文件
 *      - 删除
 *      - 完全删除目录
 *
 * @author Suvan
 */
public final class FtpUtil {

    private FtpUtil() { }

    private static volatile FTPClient ftpClient;

    private static String ftpIp;
    private static Integer ftpPort;
    private static String ftpUsername;
    private static String ftpPassword;

    /*
     * ***********************************************
     * 静态代码块
     *     - 读取 src/main/resources/neubbs.properties
     * ***********************************************
     */
    static {
        ftpClient = new FTPClient();

        Resource resource = new ClassPathResource("/neubbs.properties");
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);

            ftpIp = props.getProperty("ftp.ip");
            ftpPort = Integer.parseInt(props.getProperty("ftp.port"));
            ftpUsername = props.getProperty("ftp.username");
            ftpPassword = SecretUtil.decodeBase64(props.getProperty("ftp.password"));
        } catch (IOException ioe) {
            throw new UtilClassException(ApiMessage.UNKNOWN_ERROR).log(LogWarnEnum.UC3);
        }
    }

    /**
     * 创建目录
     *      - /为分隔，支持创建层级目录（例：/user/userId-username/avatar）
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
     * 获取获取指定路径文件名列表
     *      - 指定目录下所有文件名
     *      - 不包含目录
     *
     * @param serverDirectoryPath 服务器目录路径
     * @return List 文件名集合
     * @throws IOException IO异常
     */
    public static List<String> listServerPathFileName(String serverDirectoryPath) throws IOException {
        connect();
        moveServerPath(serverDirectoryPath);

        FTPFile[] files = ftpClient.listFiles(serverDirectoryPath);
        List<String> fileNameList = new ArrayList<>(files.length);
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
     *      - 若不存在，则创建文件目录，
     *      - 切换指定目录，进行文件上传
     *      - 另起线程，从 20 端口进行传输，函数会先一步执行完毕
     *
     * @param serverDirectoryPath 服务器目录路径（默认根路径为: /）
     * @param serverFileName 服务器文件名（上传后的文件名）
     * @param uploadFileInputStream 待上传文件的输入流（可传入 new FileInputStream(File)）
     * @throws IOException IO异常
     */
    public static void uploadFile(String serverDirectoryPath, String serverFileName,
                                  InputStream uploadFileInputStream) throws IOException {
        connect();

        ftpClient.changeWorkingDirectory(serverDirectoryPath);
        if (!ftpClient.storeFile(serverFileName, uploadFileInputStream)) {
            throw new FtpException(ApiMessage.FTP_SERVER_ERROR).log(LogWarnEnum.UC4);
        }

        uploadFileInputStream.close();
        destroy();
    }

    /**
     * 删除
     *      - 删除文件
     *      - 删除目录（第二参数 serverFileName 为 null，则删除目录）
     *
     * @param serverDirectoryPath 服务器目录路径
     * @param serverFileName 服务器文件名
     * @throws IOException IO异常
     */
    public static void delete(String serverDirectoryPath, String serverFileName) throws IOException {
        connect();
        moveServerPath(serverDirectoryPath);

        //complete around(before-after) sprit
        serverDirectoryPath = StringUtil.completeAroundSprit(serverDirectoryPath);

        boolean deleteResult = serverFileName == null
                ? ftpClient.removeDirectory(serverDirectoryPath)
                : ftpClient.deleteFile(serverDirectoryPath + "/" + serverFileName);

        if (!deleteResult) {
            throw new FtpException(ApiMessage.FTP_SERVER_ERROR).log(LogWarnEnum.UC5);
        }

        destroy();
    }

    /**
     * 完全删除目录
     *      - 包括其内部子文件,子目录
     *      - / 默认是根目录
     *      - 不存在目录（会自动补全前后 “/”）则会抛出异常
     *
     * @param serverDirectoryPath 服务器目录路径
     * @throws IOException IO异常
     */
    public static void deleteDirectory(String serverDirectoryPath) throws IOException {
        connect();

        remove(StringUtil.completeAroundSprit(serverDirectoryPath));

        destroy();
    }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /**
     * 连接 FTP 服务器
     *      - 流程
     *          1. 连接
     *          2. 登陆
     *          3. 设置文件类型
     *          4. 开通端口传输数据
     *          5. 判断响应码
     *
     * @throws IOException IO异常
     */
    private static void connect() throws IOException {
        ftpClient.connect(ftpIp, ftpPort);
        ftpClient.login(ftpUsername, ftpPassword);

        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

        ftpClient.enterLocalPassiveMode();

        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            throw new FtpException(ApiMessage.FTP_SERVER_ERROR).log(LogWarnEnum.UC6);
        }
    }

    /**
     * 退出连接
     *      - 【注意】每个工具方法，重新连接后，使用完毕，都需要断开连接，否则其余用户无法使用
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
     *      - （定位）移动到指定目录，若不存在，则抛出异常
     *
     * @param serverDirectoryPath 服务器目录
     * @throws IOException IO异常
     */
    private static void moveServerPath(String serverDirectoryPath) throws IOException {
        //no exist directory, return false
        if (!ftpClient.changeWorkingDirectory(serverDirectoryPath)) {
            throw new FtpException(ApiMessage.FTP_SERVER_ERROR).log(LogWarnEnum.UC7);
        }
    }

    /**
     * 完全删除
     *      - 递归操作
     *      - 删除指定路径的子文件，及子目录
     *
     * @param path 删除路径
     * @throws IOException IO异常
     */
    private static void remove(String path) throws IOException {
        moveServerPath(path);

        FTPFile[] files = ftpClient.listFiles();
        if (files.length == 0) {
            //there are no sub-files, to remove directory
            ftpClient.removeDirectory(path);
            return;
        }

        for (FTPFile ftpFile: ftpClient.listFiles(path)) {
            if (ftpFile.isFile()) {
                ftpClient.deleteFile(path + "/" + ftpFile.getName());
            } else if (ftpFile.isDirectory()) {
                //recursion
                remove(path + "/" + ftpFile.getName() + "/");
            }
        }

        //finally delete the initial directory
        ftpClient.removeDirectory(path);
    }
}
