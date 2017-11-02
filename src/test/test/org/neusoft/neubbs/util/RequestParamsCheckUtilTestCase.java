package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.utils.RequestParamCheckUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试 reuqest 参数 检测工具类
 */
@RunWith(JUnit4.class)
public class RequestParamsCheckUtilTestCase {
    /**
     * 测试 参数集合的合法性检测（非空，长度，正则规范）
     */
    @Test
    public void testRequestParamsMapCheckNoNull() throws Exception{
        RequestParamCheckUtil.check(ParamConst.PASSWORD, "1234");

        Map<String, String> typeParamMap = new HashMap<>(SetConst.SIZE_TWO);
            typeParamMap.put(ParamConst.USERNAME, "sadfasf");
            typeParamMap.put(ParamConst.EMAIL, "asdfasf");
        RequestParamCheckUtil.check(typeParamMap);
    }
}
