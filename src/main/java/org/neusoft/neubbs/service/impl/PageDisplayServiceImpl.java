package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.service.IPageDisplayService;
import org.neusoft.neubbs.utils.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * IPageDisplayService 实现类
 *
 * @author Suvan
 */
@Service("pageDisplayServiceImpl")
public class PageDisplayServiceImpl implements IPageDisplayService {

    @Override
    public Map<String, Object> buildJsonMap(Object object) {
        //if object == null, return empty object map
        if (object == null) {
            return new LinkedHashMap<>();
        }

       return JsonUtil.toMapByObject(object);
    }
}
