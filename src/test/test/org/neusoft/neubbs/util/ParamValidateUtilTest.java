package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.exception.ParamsErrorException;
import org.neusoft.neubbs.utils.ParamValidateUtil;

/**
 * ParamValidateUtil 测试类
 *      - 测试 check()
 *      - 测试 check() 异常
 */
@RunWith(JUnit4.class)
public class ParamValidateUtilTest {

    /**
     * 测试 check()
     */
    @Test
    public void testCheck() {
        ParamValidateUtil.check(ParamConst.USER_ID, String.valueOf(1));
        ParamValidateUtil.check(ParamConst.USERNAME, "suvan");
        ParamValidateUtil.check(ParamConst.EMAIL, "test@test.com");
        ParamValidateUtil.check(ParamConst.BIRTHDAY, null);
    }

    /**
     * 测试 check() 异常
     */
    @Test
    public void testCheckException() {
        String[][] params = {
                {ParamConst.USERNAME, null}, {ParamConst.PASSWORD, "1"},
                {ParamConst.EMAIL, "test……&……&……com"}
        };

        for (String[] param: params) {
            try {
                ParamValidateUtil.check(param[0], param[1]);
                throw new RuntimeException("not specified as 'ParamErrorException'");
            } catch (ParamsErrorException pee) {
                pee.printStackTrace();
            }
        }
    }
}
