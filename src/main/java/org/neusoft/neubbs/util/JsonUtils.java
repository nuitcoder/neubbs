package org.neusoft.neubbs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * JSON 工具类
 *
 * @author Suvan
 */
public class JsonUtils {
    /**
     * 将Object对象转换为JSON格式字符串
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public static String toJSONStringByObject(Object obj){
        ObjectMapper mapper = new ObjectMapper();

        String json = null;
        try{
            json = mapper.writeValueAsString(obj);
        }catch (JsonProcessingException jpe){}

        return json;
    }

    /**
     * 将JSON格式字符串，转为Map<String,String>
     * @param json
     * @return
     * @throws IOException
     */
    public static Map<String, Object> toMapByJSONString(String json){
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = null;
        try{
            map = mapper.readValue(json, Map.class);
        }catch (IOException ioe){}

        return map;
    }

    /**
     * 将 Object 对象转换成 Map<String, String> 格式
     * @param obj
     * @return
     */
    public static Map<String, Object> toMapByObject(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        Map<String, Object> map = null;

        try{
            json = mapper.writeValueAsString(obj);
            map = mapper.readValue(json, Map.class);
        }catch (IOException e){}

        return map;
    }
}
