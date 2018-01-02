package org.neusoft.neubbs.constant.log;

/**
 * 日志警告信息
 *
 * @author Suvan
 */
public final class LogWarn {

    private LogWarn() { }

    /*****************************File api**********************************/
    public static final String FILE_01 = "用户没有选择上传文件 ";
    public static final String FILE_02 = " 文件类型不符合头像类型（jpg | png | jpeg）";
    public static final String FILE_03 = "服务器不存在指定上传文件目录";
    public static final String FILE_04 = "IO异常，文件复制到服务器指定目录失败";
    public static final String FILE_05 = "用户上传头像过大（超过 5MB）";


    /*****************************Api Interceptor**********************************/
    public static final String INTERCEPTOR_01 = "token 已经过期，需重新登录";
    public static final String INTERCEPTOR_02 = "无权访问 api，需登录";
    public static final String INTERCEPTOR_03 = "用户权限不足，非管理员用户无法调用此 api";


    /******************************User Service*********************************/
    public static final String USER_01 = "用户保存失败";
    public static final String USER_02 = "用户修改失败";
    public static final String USER_04 = "用户行为修改失败";
    public static final String USER_05 = " 授权口令已经过期";
    public static final String USER_07 = " 用户已经激活，无需再次激活";
    public static final String USER_08 = "  邮箱已被占用";
    public static final String USER_09 = " 用户密码不正确";
    public static final String USER_10 = "未生成验证码，无法验证";
    public static final String USER_11 = " 验证码不正确";
    public static final String USER_12 = "无权修改其余用户";
    public static final String USER_14 = " 用户名已被占用";
    public static final String USER_15 = " 口令无效";
    public static final String USER_16 = " 创建 jwt 加密 token 失败！";
    public static final String USER_17 = " IO异常，生成 captcha 图片验证码失败";
    public static final String USER_18 = " （用户）行为失败";
    public static final String USER_19 = " 数据库中不存在该用户 ";
    public static final String USER_20 = "账户未激活，无权调用 api";

    /******************************Topic Service*********************************/
    public static final String TOPIC_01 = " 话题保存失败";
    public static final String TOPIC_02 = " 话题内容保存失败";
    public static final String TOPIC_03 = " 话题回复保存失败";
    public static final String TOPIC_04 = " 话题删除失败";
    public static final String TOPIC_05 = " 话题内容删除失败";
    public static final String TOPIC_06 = " 话题回复删除失败";
    public static final String TOPIC_07 = " 话题内容修改失败";
    public static final String TOPIC_08 = " 话题内容修改失败";
    public static final String TOPIC_09 = " 话题回复修改失败";
    public static final String TOPIC_10 = " 不存在话题";
    public static final String TOPIC_11 = " 不存在回复";
    public static final String TOPIC_12 = "page 与 limit 参数有误， 获取范围超过话题数量，";
    public static final String TOPIC_14 = " 不存在话题分类";
    public static final String TOPIC_15 = " 已经存在话题昵称（英文 id），不要重复插入";
    public static final String TOPIC_16 = " 已经存在话题名称（中文名），不要重复插入";
    public static final String TOPIC_17 = " 根据筛选条件（limit, page, category, username）"
            + "，话题数为 0，获取的话题列表长度为 0";
    public static final String TOPIC_18 = " 根据筛选条件（limit, category, username）, 话题数为 0， 话题总页数为 0";
    public static final String TOPIC_19 = " 修改话题分类的描述失败！";
    public static final String TOPIC_20 = " 当前话题请勿重复点赞（重复输入 inc 命令）";
    public static final String TOPIC_21 = " 当前话题请勿重复取消点赞（重复输入 dec 命令）";
    public static final String TOPIC_22 = " 用户，无法取消点赞，并未点击喜欢该文章 topicid= ";
    public static final String TOPIC_23 = " （用户）行为失败";
    public static final String TOPIC_24 = " （话题）记录行为失败";
    public static final String TOPIC_35 = " 话题行为保存失败";


    /******************************Ftp Service*********************************/
    public static final String FTP_01 = "注册用户，ftp 服务器新建 个人目录，抛出 IO 异常";
    public static final String FTP_02 = "上传用户头像图片，抛出 IO 异常";


    /******************************Param Check Service*********************************/
    public static final String PARAM_CHECK_01 = " 需按规定输入相应参数，不能输入空参数";
    public static final String PARAM_CHECK_02 = " 指令无效，请根据相应约定，输入运行范围内指令";
}
