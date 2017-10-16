package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.util.RequestParamsCheckUtils;

/**
 * 测试 reuqest 参数 检测工具类
 */
@RunWith(JUnit4.class)
public class RequestParamsCheckUtilsTestCast {
    /**
     * 测试 参数集合的合法性检测（非空，长度，正则规范）
     */
    @Test
    public void testRequestParamsMapCheckNoNull(){
        //链式调用
        String errorInfo = RequestParamsCheckUtils
                            .putParamKeys(new String[]{"username", "password", "email"})
                            .putParamValues(new String[]{"suvan","123456","liushuwei@gmail.com"})
                            .checkParamsNorm();

        System.out.println("输出错误信息：" + errorInfo);
    }
}
