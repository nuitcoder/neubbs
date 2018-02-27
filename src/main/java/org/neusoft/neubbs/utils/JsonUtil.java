package org.neusoft.neubbs.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具类
 *      - 转换为 JSON 格式字符串
 *      - 将 JSON 字符串，转换为 Map
 *      - 将 Object 对象，转换成 Map
 *      - 将 JSON 数组字符串，转换成 List
 *      - 是否存在指定 int 元素
 *      - 获取 int 元素索引位置
 *      - 获取数组长度
 *
 * @author Suvan
 */
public final class JsonUtil {

    private JsonUtil() { }

    /**
     * 转换为 JSON 格式字符串
     *      - 将任意对象
     *
     * @param obj 任意 Object 对象
     * @return String JSON 格式字符串
     */
    public static String toJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException jpe) {
            return null;
        }
    }

    /**
     * 将 JSON 字符串，转换成 Map
     *      - String -> Map<String, Object>
     *
     * @param json JSON 格式字符串
     * @return Map 键值对
     */
    public static Map<String, Object> toMapByJsonString(String json) {
        try {
            return new ObjectMapper().readValue(json, LinkedHashMap.class);
        } catch (IOException ioe) {
            return null;
        }
    }

    /**
     * 将 Object 对象，转换成 Map
     *      - Object -> Map<String, Object>
     *
     * @param obj 任意 Object 对象
     * @return Map 键值对
     */
    public static Map<String, Object> toMapByObject(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(obj);
            return mapper.readValue(json, LinkedHashMap.class);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 将 JSON 数组字符串，转换成 List
     *      - String -> List<Integer>
     *
     * @param jsonArrayString JSON 数组字符串
     * @return List 列表
     */
    public static List<Integer> toListByJsonArrayString(String jsonArrayString) {
        return JSON.parseArray(jsonArrayString, Integer.class);
    }

    /**
     * 是否存在指定 int 元素
     *      - 需输入 JSON 数组字符串和目标元素（int 类型）
     *      - 用于从 forum_user_action（用户行为表）, forum_topic_action（话题行为表）去除相应 JSON 字符串
     *
     * @param jsonArrayString JSON 数组字符串
     * @param intElement 需判断存在性的 int 元素
     * @return boolean 判断结果（true - 存在，false - 不存在）
     */
    public static boolean isExistIntElement(String jsonArrayString,  int intElement) {
        JSONArray jsonArray = JSON.parseArray(jsonArrayString);

        boolean result = false;
        for (int i = 0, len = jsonArray.size(); i < len; i++) {
            if (jsonArray.getInteger(i) == intElement) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * 获取 int 元素索引位置
     *      - 需输入 JSON 数组字符串和目标元素（int 类型）
     *
     * @param jsonArrayString JSON 数组字符串
     * @param intElement 需获取索引值的 int 元素
     * @return int 元素所在位置索引值（若未找到则为 -1）
     */
    public static int getIntElementIndex(String jsonArrayString, int intElement) {
        return JSON.parseArray(jsonArrayString).indexOf(intElement);
    }

    /**
     * 获取数组长度
     *      - 传入 JSON 数组字符串
     *
     * @param jsonArrayString　JSON 数组字符串
     * @return int JSON 数组字符串长度
     */
    public static int getJsonArrayLength(String jsonArrayString) {
        return JSON.parseArray(jsonArrayString).size();
    }
}
