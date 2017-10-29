package org.neusoft.neubbs.utils;


import java.util.Map;

/**
 * Map 过滤 工具类
 *
 * @author Suvan
 */
public final class MapFilterUtil {
   private static final String ID = "id";
   private static final String NAME = "name";
   private static final String USERNAME = "username";
   private static final String PASSWORD = "password";
   private static final String IMAGE = "image";
   private static final String RANK = "rank";
   private static final String STATE = "state";
   private static final String CREATETIME = "createtime";


   private MapFilterUtil() { }

   /**
    * 删除多个 Key
    *
    * @param map 传入键值对
    * @param keys 要删除 Key 的字符串数组
    */
   public static void removeKeys(Map map, String[] keys) {
      for (String key : keys) {
         map.remove(key);
      }
   }


   /**
    * 过滤用户信息（用于 api 页面显示 model）
    *
    * @param userInfoMap 用户信息Map
    */
   public static void filterUserInfo(Map userInfoMap) {

      //name -> username
      userInfoMap.put(USERNAME, userInfoMap.get(NAME));

      //删除 id,name,password,image,rank,state,createtime
      removeKeys(userInfoMap, new String[]{ID, NAME, PASSWORD, IMAGE, RANK, STATE, CREATETIME});
   }
}
