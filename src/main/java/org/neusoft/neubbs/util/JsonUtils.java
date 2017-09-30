package org.neusoft.neubbs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
