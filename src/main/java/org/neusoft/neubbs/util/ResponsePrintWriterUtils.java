package org.neusoft.neubbs.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应输出 工具类
 *      1. 需要 JsonUtils 工具类
 *
 * @author
 */
public class ResponsePrintWriterUtils {

    private static final String  SUCCESS_STATES = "success";
    private static final Boolean SUCCESS_STATES_FAIL= false;
    private static final String MESSAGE = "message";

    private static final String CHARACTER_ENCODING = "utf-8";
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
     * 输出失败 JSON 信息
     *
     * @param response 用户响应
     * @param failMessage 失败信息
     * @throws IOException
     */
    public static void outFailJSONMessage(HttpServletResponse response, String failMessage){

        //构造 Map ,并指定初始大小
        Map<String, Object> map = new HashMap<String, Object>(2);
            map.put(SUCCESS_STATES, SUCCESS_STATES_FAIL);
            map.put(MESSAGE, failMessage);

        //将 Map 转换为 JSON 格式字符串
        String json = JsonUtils.toJSONStringByObject(map);

        //设定相应类型
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType( CONTENT_TYPE);

        //输出与关闭资源
        PrintWriter writer = null;
        try{
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
