package org.neusoft.neubbs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * JSON 工具类
 */
public class JsonUtils {
    /**
     * 将Object对象转换为JSON格式字符串
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public static String getObjectJSONString(Object obj) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * 将JSON格式字符串，转为Map<String,String>
     * @param json
     * @return
     * @throws IOException
     */
    public static Map<String, String> getMapByJSON(String json){
        Map<String, String> map = null;

        ObjectMapper mapper = new ObjectMapper();
        try{
            map = mapper.readValue(json, Map.class);
        }catch (IOException ioe){}

        return map;
    }
}
