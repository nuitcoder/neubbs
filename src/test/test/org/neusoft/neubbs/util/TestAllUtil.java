package test.org.neusoft.neubbs.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 测试所有 Util
 *
 * @author Suvan
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        FtpUtilTest.class,
        JsonUtilTest.class,
        MapFilterUtilTest.class,
        ParamValidateUtilTest.class,
        PatternUtilTest.class,
        RandomUtilTest.class,
        SecretUtilTest.class,
        SendEmailUtilTest.class,
        StringUtilTest.class,
})
public class TestAllUtil { }
