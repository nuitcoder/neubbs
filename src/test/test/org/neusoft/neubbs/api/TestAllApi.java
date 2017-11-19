package test.org.neusoft.neubbs.api;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 测试所有 api
 *
 * @author Suvan
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountCollectorTest.class,
        CountControllerTest.class
})
public class TestAllApi { }
