package org.neusoft.neubbs.utils;


import org.neusoft.neubbs.constant.api.ParamConst;

import java.util.Map;

/**
 * Map 过滤 工具类
 *
 * @author Suvan
 */
public final class MapFilterUtil {

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
   public static void filterUserInfo(Map<String, Object> userInfoMap) {

      //name -> username
      userInfoMap.put(ParamConst.USERNAME, userInfoMap.get(ParamConst.NAME));

      //删除 id,name,password,image,rank,state,createtime
      removeKeys(userInfoMap,
              new String[] {
                  ParamConst.ID, ParamConst.NAME,
                  ParamConst.PASSWORD, ParamConst.IMAGE, ParamConst.RANK,
                  ParamConst.STATE, ParamConst.CREATETIME
               });
   }
}
