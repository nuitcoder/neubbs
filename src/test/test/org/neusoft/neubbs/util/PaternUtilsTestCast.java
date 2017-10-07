package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.util.PatternUtils;

/**
 * 正则匹配 测试类
 */
@RunWith(JUnit4.class)
public class PaternUtilsTestCast {
   /**
    * 测试 纯数字检测
    */
   @Test
   public void testPureNumber(){
      System.out.println(PatternUtils.pureNumber("___213132"));
      System.out.println(PatternUtils.pureNumber("222"));
      System.out.println(PatternUtils.pureNumber("&^@#$sfgqwre1123"));
      System.out.println(PatternUtils.pureNumber("12312!"));
   }
}
