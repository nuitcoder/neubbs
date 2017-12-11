package org.neusoft.neubbs.utils;

import org.neusoft.neubbs.constant.api.SetConst;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
     *      - 构造 Map ,并指定初始大小
     *      - 将 Map 转换为 JSON 格式字符串
     *      - 设定响应类型
     *      - 输出与关闭资源
     *
     * @param response 用户响应
     * @param failMessage 失败信息
     */
    public static void outFailJSONMessage(HttpServletResponse response, String failMessage) {
        Map<String, Object> map = new LinkedHashMap<>(SetConst.LENGTH_TWO);
            map.put("success", false);
            map.put("message", failMessage);
            map.put("model", new HashMap<>(SetConst.SIZE_ONE));

        String json = JsonUtil.toJSONStringByObject(map);

        response.setCharacterEncoding(SetConst.CHARACTER_ENCODING);
        response.setContentType(SetConst.CONTENT_TYPE);
        
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
