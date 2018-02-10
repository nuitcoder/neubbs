package test.org.neusoft.neubbs.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.utils.PatternUtil;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * PatternUtil 测试类
 *    - 测试 isPureNumber()
 *    - 测试 isPureEnglish()
 *    - 测试 matchUsername()
 *    - 测试 matchEmail()
 *    - 测试 matchUserAvatar()
 *    - 测试 matchTopicCategory()
 */
@RunWith(JUnit4.class)
public class PatternUtilTest {

   /**
    * 测试 isPureNumber()
    */
   @Test
   public void testIsPureNumber() {
      Assert.assertTrue(PatternUtil.isPureNumber("123"));
      Assert.assertTrue(PatternUtil.isPureNumber("99999"));

      Assert.assertFalse(PatternUtil.isPureNumber("_***"));
      Assert.assertFalse(PatternUtil.isPureNumber("123*"));
      Assert.assertFalse(PatternUtil.isPureNumber("k12"));
   }

   /**
    * 测试 isPureEnglish()
    */
   @Test
   public void testIsPureEnglish() {
      Assert.assertTrue(PatternUtil.isPureEnglish("hello"));

      Assert.assertFalse(PatternUtil.isPureEnglish("hello world"));
      Assert.assertFalse(PatternUtil.isPureEnglish("hello123"));
      Assert.assertFalse(PatternUtil.isPureEnglish("hello_"));
   }

   /**
    * 测试 matchUsername()
    */
   @Test
   public void testMatchUsername() {
      List<String> trueUsername = Arrays.asList("12345", "suvan", "ABCD", "1suvanA", "suvanLLLLLLLLLLLLLLL");
      for (String username: trueUsername) {
         Assert.assertTrue(PatternUtil.matchUsername(username));
      }

      List<String> falseUsername = Arrays.asList("a", "1a", "Aa", "suvansuvansuvansuvansuvan", "test@qq.com");
      Iterator iterator = falseUsername.iterator();
      while (iterator.hasNext()) {
         Assert.assertFalse(PatternUtil.matchUsername((String) iterator.next()));
      }
   }

   /**
    * 测试 matchEmail()
    */
   @Test
   public void testMatchEmail() {
      Assert.assertTrue(PatternUtil.matchEmail("liushuwei0925@gmail.com"));
      Assert.assertTrue(PatternUtil.matchEmail("test@email.com"));

      Assert.assertFalse(PatternUtil.matchEmail("123@.com"));
      Assert.assertFalse(PatternUtil.matchEmail("hello@word"));
   }

   /**
    * 测试 matchUserAvatarType()
    */
   @Test
   public void testUserAvatarType() {
      String[] truePictureType = {"image/jpg", "image/jpeg", "text/PNG"};
      for (String pictureType: truePictureType) {
         Assert.assertTrue("error param: " + pictureType, PatternUtil.matchUserAvatarType(pictureType));
      }

      String[] falsePictureType = {".jpg", "/.PNG", "image/", "image/txt"};
      for (String pictureType: falsePictureType) {
         Assert.assertFalse("error param: ", PatternUtil.matchUserAvatarType(pictureType));
      }
   }

   /**
    * 测试 matchTopicCategory()
    */
   @Test
   public void testMatchTopicCategory() {
      Assert.assertTrue(PatternUtil.matchTopicCategory("音乐"));
      Assert.assertTrue(PatternUtil.matchTopicCategory("music"));
      Assert.assertFalse(PatternUtil.matchTopicCategory("123music"));
      Assert.assertFalse(PatternUtil.matchTopicCategory("音乐music***"));
   }
}
