package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.utils.PatternUtil;

/**
 * 正则匹配 测试类
 */
@RunWith(JUnit4.class)
public class PatternUtilTest {
   /**
    * 测试 纯数字检测
    */
   @Test
   public void testIsPureNumber() {
      System.out.println(PatternUtil.isPureNumber("___213132"));
      System.out.println(PatternUtil.isPureNumber("222"));
      System.out.println(PatternUtil.isPureNumber("&^@#$sfgqwre1123"));
      System.out.println(PatternUtil.isPureNumber("12312!"));
   }

   /**
    * 测试 纯英文检测
    */
   @Test
   public void testIsPureEnglish() {
      String[] params = {
         "safde", "ADRTX", "sdfABVA", "asd...", "++_%saf", "---ABCD", "AS--..123asdf", "a-B",
         "=-=-=-", "123123123", "测试", "count1", "hello123", "no ooo", "cate"
      };

      for (String param: params) {
         System.out.println("input param=" + param + ": isPureEnglish() -> " + PatternUtil.isPureEnglish(param));
      }
   }

   /**
    * 测试 匹配用户名
    */
   @Test
   public void testMatchUsername() {
      String [] usrenameArray = {"1","a","*","kk","*as","asdfasf___","aaaaaaaaaaaaaaaaaaaaaaa","ABCk"};

      for(String name: usrenameArray){
         System.out.println(name + " 匹配结果：" + PatternUtil.matchUsername(name));
      }
   }

   /**
    * 测试 匹配邮箱
    */
   @Test
   public void testMatchEmail() {
      String [] emailArray = {"asdfasdf","ss@","@asdfa.com","asdfas@qq.com","fasadf@suvan.net.cn",
                              "asf_**@qq.com","1231%2312^31321@qq.com","hello@suvan.liushuwei.cn.net",
                              "liushuwei@gmail.com","313123123@qq.com","suvan@liushuwei.cn"};

      for(String email : emailArray){
         System.out.println(email + " 匹配结果：" + PatternUtil.matchEmail(email));
      }
   }

   /**
    * 匹配图片
    */
   @Test
   public void testMatchUserImage() {
      String [] imageType = {"image/jpg", "image/JPG" ,"image/png", "image/PNG", "image/asdffsda", "image/gif"};

      for (String type: imageType) {
         System.out.println(type + "匹配结果：" + PatternUtil.matchUserImage(type));
      }
   }

   /**
    * 匹配话题类型
    */
   @Test
   public void testMatchTopicCategory() {
      String [] categoryArray = {"java", "132aa","_qwerq","你好","你好study","学习!234_+"};

      for(String category: categoryArray){
         System.out.println(category + " 匹配结果：" + PatternUtil.matchTopicCategory(category));
      }
   }
}
