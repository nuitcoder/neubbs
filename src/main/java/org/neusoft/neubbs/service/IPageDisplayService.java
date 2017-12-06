package org.neusoft.neubbs.service;

import java.util.Map;

/**
 * 页面显示业务接口
 *
 * @author Suvan
 */
public interface IPageDisplayService {
    /**
     * （Object 对象） 构建 Json Map
     *
     * @param object Object 对象
     * @return Map 获取对象相应的LinkHashMap
     */
    Map<String, Object> buildJsonMap(Object object);
}
