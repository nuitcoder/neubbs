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
      System.out.println(PatternUtils.isPureNumber("___213132"));
      System.out.println(PatternUtils.isPureNumber("222"));
      System.out.println(PatternUtils.isPureNumber("&^@#$sfgqwre1123"));
      System.out.println(PatternUtils.isPureNumber("12312!"));
   }

   /**
    * 测试 匹配用户名
    */
   @Test
   public void testMatchUsername(){
      String [] usrenameArray = {"1","a","*","kk","*as","asdfasf___","aaaaaaaaaaaaaaaaaaaaaaa","ABCk"};

      for(String name: usrenameArray){
         System.out.println(name + " 匹配结果：" + PatternUtils.matchUsername(name));
      }
   }

   /**
    * 测试 匹配邮箱
    */
   @Test
   public void testMatchEmail(){
      String [] emailArray = {"asdfasdf","ss@","@asdfa.com","asdfas@qq.com","fasadf@suvan.net.cn",
                              "asf_**@qq.com","1231%2312^31321@qq.com","hello@suvan.liushuwei.cn.net",
                              "liushuwei@gmail.com","313123123@qq.com","suvan@liushuwei.cn"};

      for(String email : emailArray){
         System.out.println(email + " 匹配结果：" + PatternUtils.matchEmail(email));
      }
   }
}
