package org.neusoft.neubbs.utils;

import org.neusoft.neubbs.constant.api.SetConst;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应输出 工具类
 *      1. 需要 JsonUtil 工具类
 *
 * @author Suvan
 */
public final class ResponsePrintWriterUtil {

    private ResponsePrintWriterUtil() { }

    /**
     * 输出失败 JSON 信息
     *
     * @param response 用户响应
     * @param failMessage 失败信息
     */
    public static void outFailJSONMessage(HttpServletResponse response, String failMessage) {

        //构造 Map ,并指定初始大小
        Map<String, Object> map = new HashMap<>(SetConst.LENGTH_TWO);
            map.put("success", false);
            map.put("message", failMessage);

        //将 Map 转换为 JSON 格式字符串
        String json = JsonUtil.toJSONStringByObject(map);

        //设定相应类型
        response.setCharacterEncoding(SetConst.CHARACTER_ENCODING);
        response.setContentType(SetConst.CONTENT_TYPE);

        //输出与关闭资源
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
