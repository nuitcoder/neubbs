package org.neusoft.neubbs.utils;


import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;

import java.util.LinkedHashMap;
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
   public static void removeKeys(Map<String, Object> map, String[] keys) {
      for (String key : keys) {
         map.remove(key);
      }
   }

   /**
    * 过滤用户信息
    *    - 仅用于 user service,
    *    - 修改 name -> username
    *    - 删除 id, name, password, rank
    *
    * @param userInfoMap 用户信息Map
    */
   public static void filterUserInfo(Map<String, Object> userInfoMap) {
      //re-build username and avator field
      userInfoMap.put(ParamConst.USERNAME, userInfoMap.get(ParamConst.NAME));
      userInfoMap.put(ParamConst.AVATOR, StringUtil.spliceUserAvatorUrl(userInfoMap, SetConst.HTTP));

      removeKeys(userInfoMap,
              new String[] {ParamConst.ID, ParamConst.NAME, ParamConst.PASSWORD, ParamConst.RANK});
   }

   /**
    * 过滤话题基本信息 Map
    *    - 修改 id -> topicid
    *    - 删除 id, userid , categoryid, lastreplyuserid
    *
    * @param topicInfoMap 话题基本信息 Map
    */
   public static void filterTopicInfo(Map<String, Object> topicInfoMap) {
      topicInfoMap.put(ParamConst.TOPIC_ID, topicInfoMap.get(ParamConst.ID));
      Object topicLastReplyTime = topicInfoMap.get(ParamConst.LAST_REPLY_TIME);

      if (topicLastReplyTime == null) {
         topicInfoMap.put(ParamConst.LAST_REPLY_TIME, 0);
      }

      removeKeys(topicInfoMap, new String[] {ParamConst.ID, ParamConst.USER_ID,
              ParamConst.CATEGORY_ID, ParamConst.LAST_REPLY_USER_ID});
   }

   /**
    * 过滤话题内容信息 Map
    *    - 删除 id，topicid
    *
    * @param topicContentInfoMap 话题内容信息Map
    */
   public static void filterTopicContentInfo(Map<String, Object> topicContentInfoMap) {
      removeKeys(topicContentInfoMap, new String[] {ParamConst.ID, ParamConst.TOPIC_ID});
   }

   /**
    * 过滤话题分类信息 Map
    *    - 修改 nick -> id
    *    - 删除 id(原有)
    *
    * @param topicCategoryInfoMap 话题分类信息Map
    */
   public static void filterTopicCategory(Map<String, Object> topicCategoryInfoMap) {
      topicCategoryInfoMap.put(ParamConst.ID, topicCategoryInfoMap.get(ParamConst.NICK));

      removeKeys(topicCategoryInfoMap, new String[] {ParamConst.NICK});
   }

   /**
    * 过滤话题用户信息 Map
    *    - 只保留 name, image
    *    - 修改 name -> username
    *    - 修改 image -> avator
    *    - 删除 name, image
    *
    * @param userInfoMap 用户信息Map
    */
   public static void filterTopicUserInfo(Map<String, Object> userInfoMap) {
      keepKesy(userInfoMap, new String[] {ParamConst.ID, ParamConst.NAME, ParamConst.AVATOR});

      userInfoMap.put(ParamConst.USERNAME, userInfoMap.get(ParamConst.NAME));
      userInfoMap.put(ParamConst.AVATOR, StringUtil.spliceUserAvatorUrl(userInfoMap, SetConst.HTTP));

      userInfoMap.remove(ParamConst.ID);
      userInfoMap.remove(ParamConst.NAME);
   }


   /**
    * 过滤话题回复信息 Map
    *    - 修改 id -> replyid
    *    - 删除 id，userid
    *
    * @param topicReplyInfoMap 话题回复信息Map
    */
   public static void filterTopicReply(Map<String, Object> topicReplyInfoMap) {
      topicReplyInfoMap.put(ParamConst.REPLY_ID, topicReplyInfoMap.get(ParamConst.ID));

      removeKeys(topicReplyInfoMap, new String[] {ParamConst.ID, ParamConst.USER_ID});
   }

   /*
    * ***********************************************
    * private method
    * ***********************************************
    */

   /**
    * 保留指定 key
    *
    * @param map 键值队
    * @param keys 要保留的 key
    */
   private static void keepKesy(Map<String, Object> map, String[] keys) {
      Map<String, Object> tmpMap = new LinkedHashMap<>();

      for (String key: keys) {
         tmpMap.put(key, map.get(key));
      }

      map.clear();

      for (String key: keys) {
         map.put(key, tmpMap.get(key));
      }
   }
}
