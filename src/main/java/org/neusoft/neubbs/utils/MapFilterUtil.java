package org.neusoft.neubbs.utils;


import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
   public static void keepKesy(Map<String, Object> map, String [] keys) {
      Map<String, Object> tmpMap = new HashMap<>();

      for (String key: keys) {
         tmpMap.put(key, map.get(key));
      }

      map.clear();

      for (String key: keys) {
         map.put(key, tmpMap.get(key));
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

   /**
    * 过滤话题用户信息（只保留 username 和 image）
    *
    * @param userInfoMap
    */
   public static void filterTopicUserInfo(Map<String, Object> userInfoMap) {
      //只保留 name, image
      keepKesy(userInfoMap, new String[] {ParamConst.NAME, ParamConst.IMAGE});

      //name -> username（删除 name）
      userInfoMap.put(ParamConst.USERNAME, userInfoMap.get(ParamConst.NAME));
      userInfoMap.remove(ParamConst.NAME);
   }

   /**
    * 过滤话题信息（针对 TopicDO， forum_topic）
    *
    * @param topicInfoMap 话题信息 Map
    */
   public static void filterTopicInfo(Map<String, Object> topicInfoMap) {
      // id -> topicid (删除 id)
      topicInfoMap.put(ParamConst.TOPIC_ID, topicInfoMap.get(ParamConst.ID));

      //时间戳 -> xx天前 （传入：发表时间 + 当前时间）
      String separateDay = StringUtil.getSeparateDay((long) topicInfoMap.get(ParamConst.LAST_REPLY_TIME),
                                                         Calendar.getInstance().getTimeInMillis());

      topicInfoMap.put(ParamConst.LAST_REPLY_TIME, separateDay);

      //删除 userid,lastreplyuserid
      removeKeys(topicInfoMap, new String[] {ParamConst.ID, ParamConst.USER_ID, ParamConst.LAST_REPLY_USER_ID});
   }

   /**
    * 过滤话题内容信息（针对 TopicContentDO, forum_topic_content）
    *
    * @param topicContentInfoMap 话题内容信息Map
    */
   public static void filterTopicContentInfo(Map<String, Object> topicContentInfoMap) {
     //删除 id，topicid
      removeKeys(topicContentInfoMap, new String[] {ParamConst.ID, ParamConst.TOPIC_ID, ParamConst.CREATETIME});
   }

   /**
    * 过滤话题回复（针对 TopicReplyDO, forum_topic_reply）
    *
    * @param topicReplyInfoMap 话题回复信息Map
    */
   public static void filterTopicReply(Map<String, Object> topicReplyInfoMap) {
      // id -> replyId
      topicReplyInfoMap.put(ParamConst.REPLY_ID, topicReplyInfoMap.get(ParamConst.ID));

      //时间戳 -> xx天前
      String separateDay = StringUtil.getSeparateDay((long) topicReplyInfoMap.get(ParamConst.CREATETIME),
              Calendar.getInstance().getTimeInMillis());

      topicReplyInfoMap.put(ParamConst.CREATETIME, separateDay);

      //是删除（id，userid，topicid）
      removeKeys(topicReplyInfoMap, new String[] {ParamConst.ID, ParamConst.USER_ID, ParamConst.TOPIC_ID});
   }


}
