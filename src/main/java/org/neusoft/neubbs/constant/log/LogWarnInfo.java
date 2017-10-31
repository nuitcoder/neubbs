package org.neusoft.neubbs.constant.log;

/**
 * 日志警告信息
 *
 * @author Suvan
 */
public final class LogWarnInfo {
    private LogWarnInfo() { }

    /*****************************Account api**********************************/
    public static final String ACCOUNT_01 = " 数据库中不存在该用户 ";
    public static final String ACCOUNT_02 = " 数据库已经存该用户 ";

    public static final String ACCOUNT_03 = "账户未激活，无权调用 api";
    public static final String ACCOUNT_04 = " 激活口令已经过期";
    public static final String ACCOUNT_05 = "授权口令已经过期";
    public static final String ACCOUNT_06 = " 邮箱未注册，无法发送邮件";
    public static final String ACCOUNT_07 = " 邮箱用户已经激活，不重复发送邮件";
    public static final String ACCOUNT_08 = " 邮箱已被占用";

    public static final String ACCOUNT_09 = " 用户密码不正确";
    public static final String ACCOUNT_10 = "未生成验证码，无法验证";
    public static final String ACCOUNT_11 = " 验证码不正确";

    public static final String ACCOUNT_12 = "无权修改其余用户";

    public static final String ACCOUNT_13 = "找不到 username 或 email 参数（选其一即可），无法获取账户信息";


    /*****************************Topic api**********************************/
//    public static final String TOPIC_01 = "不存在指定主题，请检查输入（主题 id）";
//    public static final String TOPIC_02 = "不存在指定回复，请检查输入（回复 id）";


    /*****************************File api**********************************/
    public static final String FILE_01 = "用户没有选择上传文件 ";
    public static final String FILE_02 = " 文件类型不符合头像类型（jpg | png | jpeg）";
    public static final  String FILE_03 = "服务器不存在指定上传文件目录";


    /*****************************Api Interceptor**********************************/
    public static final String INTERCEPTRO_01 = "token 已经过期，需重新登录";
    public static final String INTERCEPTRO_02 = "无权访问 api，需登录";
    public static final String INTERCEPTRO_03 = "用户权限不足，非管理员用户无法调用此 api";


    /******************************User Service*********************************/
    public static final String SERVICE_01 = "用户保存失败";
    public static final String SERVICE_02 = "用户修改失败";


    /******************************Topic Service*********************************/
    public static final String TOPIC_01 = "主题保存失败";
    public static final String TOPIC_02 = "主题内容保存失败";
    public static final String TOPIC_03 = "主题回复保存失败";
    public static final String TOPIC_04 = "主题删除失败";
    public static final String TOPIC_05 = "主题内容删除失败";
    public static final String TOPIC_06 = "主题回复删除失败";
    public static final String TOPIC_07 = "主题修改失败";
    public static final String TOPIC_08 = "主题内容修改失败";
    public static final String TOPIC_09 = "主题回复修改失败";
    public static final String TOPIC_10 = "不存在主题";
    public static final String TOPIC_11 = "不存在回复";


    /******************************Redis Service*********************************/
}
