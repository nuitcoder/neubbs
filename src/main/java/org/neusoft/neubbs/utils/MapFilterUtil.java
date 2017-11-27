package org.neusoft.neubbs.utils;


import org.neusoft.neubbs.constant.api.ParamConst;

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
    * 保留指定 key
    *
    * @param map 键值队
    * @param keys 要保留的 key
    */
   public static void keepKesy(Map<String, Object> map, String[] keys) {
      Map<String, Object> tmpMap = new LinkedHashMap<>();

      for (String key: keys) {
         tmpMap.put(key, map.get(key));
      }

      map.clear();

      for (String key: keys) {
         map.put(key, tmpMap.get(key));
      }
   }


   /**
    * 过滤用户信息（account api 查询用户信息）
    *    - name -> username
    *    - 删除 id,name,password,image,rank,state,createtime
    *
    * @param userInfoMap 用户信息Map
    */
   public static void filterUserInfo(Map<String, Object> userInfoMap) {
      userInfoMap.put(ParamConst.USERNAME, userInfoMap.get(ParamConst.NAME));
      userInfoMap.put(ParamConst.AVATOR, userInfoMap.get(ParamConst.IMAGE));

      removeKeys(userInfoMap,
              new String[] {
                  ParamConst.ID, ParamConst.NAME,
                  ParamConst.PASSWORD, ParamConst.IMAGE, ParamConst.RANK,
                  ParamConst.STATE, ParamConst.CREATETIME
               });
   }

   /**
    * 过滤话题用户信息（只保留 username 和 image）
    *    - 只保留 name, image
    *    - name -> username（删除 name）
    *
    * @param userInfoMap 用户信息Map
    */
   public static void filterTopicUserInfo(Map<String, Object> userInfoMap) {
      keepKesy(userInfoMap, new String[] {ParamConst.NAME, ParamConst.IMAGE});

      userInfoMap.put(ParamConst.USERNAME, userInfoMap.get(ParamConst.NAME));
      userInfoMap.put(ParamConst.AVATOR, userInfoMap.get(ParamConst.IMAGE));

      userInfoMap.remove(ParamConst.NAME);
      userInfoMap.remove(ParamConst.IMAGE);
   }

   /**
    * 过滤话题信息（针对 TopicDO， forum_topic）
    *    - id -> topicid (删除 id)
    *    - 删除 id, userid, lastreplyuserid
    *
    * @param topicInfoMap 话题信息 Map
    */
   public static void filterTopicInfo(Map<String, Object> topicInfoMap) {
      topicInfoMap.put(ParamConst.TOPIC_ID, topicInfoMap.get(ParamConst.ID));

      removeKeys(topicInfoMap, new String[] {ParamConst.ID, ParamConst.USER_ID, ParamConst.LAST_REPLY_USER_ID});
   }

   /**
    * 过滤话题内容信息（针对 TopicContentDO, forum_topic_content）
    *    - 删除 id，topicid
    *
    * @param topicContentInfoMap 话题内容信息Map
    */
   public static void filterTopicContentInfo(Map<String, Object> topicContentInfoMap) {
      removeKeys(topicContentInfoMap, new String[] {ParamConst.ID, ParamConst.TOPIC_ID});
   }

   /**
    * 过滤话题回复（针对 TopicReplyDO, forum_topic_reply）
    *    - id -> replyId
    *    - 删除（id，userid，topicid）
    *
    * @param topicReplyInfoMap 话题回复信息Map
    */
   public static void filterTopicReply(Map<String, Object> topicReplyInfoMap) {
      topicReplyInfoMap.put(ParamConst.REPLY_ID, topicReplyInfoMap.get(ParamConst.ID));

      removeKeys(topicReplyInfoMap, new String[] {ParamConst.ID, ParamConst.USER_ID, ParamConst.TOPIC_ID});
   }


}
