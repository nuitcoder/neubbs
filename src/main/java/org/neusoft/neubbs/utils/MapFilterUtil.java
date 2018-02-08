package org.neusoft.neubbs.utils;


import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;

import java.util.HashMap;
import java.util.Map;

/**
 * Map Filter 工具类
 *       - 过滤用户信息 Map
 *       - 过滤话题基本信息 Map
 *       - 过滤话题基本内容信息 Map
 *       - 过滤话题分类信息 Map
 *       - 过滤话题用户信息 Map
 *       - 过滤话题回复信息 Map
 *       - 生成 Map
 *
 * @author Suvan
 */
public final class MapFilterUtil {

   private MapFilterUtil() { }

   /**
    * 过滤用户信息 Map
    *    - 仅用于 User Service,
    *    - 修改 id -> userId, name -> username
    *    - 拼接用户头像地址（key=avator)，包装成 HTTP 链接
    *    - 删除 id, name, password, rank
    *
    * @param userInfoMap 用户信息Map
    */
   public static void filterUserInfo(Map<String, Object> userInfoMap) {
      userInfoMap.put(ParamConst.USER_ID, userInfoMap.get(ParamConst.ID));
      userInfoMap.put(ParamConst.USERNAME, userInfoMap.get(ParamConst.NAME));

      userInfoMap.put(ParamConst.AVATOR, StringUtil.generateUserAvatarUrl(userInfoMap));

      removeKeys(userInfoMap, new String[] {ParamConst.ID, ParamConst.NAME,
              ParamConst.PASSWORD, ParamConst.RANK});
   }

   /**
    * 过滤话题基本信息 Map
    *    - 修改 id -> topicId
    *    - 删除 id, userId, categoryId, lastReplyUserId
    *
    * @param topicInfoMap 需过滤的话题基本信息 Map
    */
   public static void filterTopicInfo(Map<String, Object> topicInfoMap) {
      topicInfoMap.put(ParamConst.TOPIC_ID, topicInfoMap.get(ParamConst.ID));

      removeKeys(topicInfoMap, new String[] {ParamConst.ID, ParamConst.USER_ID,
              ParamConst.CATEGORY_ID, ParamConst.LAST_REPLY_USER_ID});
   }

   /**
    * 过滤话题内容信息 Map
    *    - 删除 id，topicId
    *
    * @param topicContentInfoMap 需过滤的话题内容信息Map
    */
   public static void filterTopicContentInfo(Map<String, Object> topicContentInfoMap) {
      removeKeys(topicContentInfoMap, new String[] {ParamConst.ID, ParamConst.TOPIC_ID});
   }

   /**
    * 过滤话题分类信息 Map
    *    - 修改 nick（英文昵称） -> id
    *    - 删除 id(原有)
    *
    * @param topicCategoryInfoMap 需过滤的话题分类信息Map
    */
   public static void filterTopicCategory(Map<String, Object> topicCategoryInfoMap) {
      topicCategoryInfoMap.put(ParamConst.ID, topicCategoryInfoMap.get(ParamConst.NICK));

      topicCategoryInfoMap.remove(ParamConst.NICK);
   }

   /**
    * 过滤话题用户信息 Map
    *    - 仅用于 Topic Service
    *    - 仅保留 name, image，avator
    *    - 修改 name -> username
    *    - 拼接用户头像地址（key=avator)，包装成 HTTP 链接
    *    - 删除 id， name
    *
    * @param userInfoMap 需过滤的话题用户信息Map
    */
   public static void filterTopicUserInfo(Map<String, Object> userInfoMap) {
      keepKeys(userInfoMap, new String[] {ParamConst.ID, ParamConst.NAME, ParamConst.AVATOR});

      userInfoMap.put(ParamConst.USERNAME, userInfoMap.get(ParamConst.NAME));
      userInfoMap.put(ParamConst.AVATOR, StringUtil.generateUserAvatarUrl(userInfoMap));

      userInfoMap.remove(ParamConst.ID);
      userInfoMap.remove(ParamConst.NAME);
   }

   /**
    * 过滤话题回复信息 Map
    *    - 修改 id -> replyId
    *    - 删除 id，userId
    *
    * @param topicReplyInfoMap 需过滤的话题回复信息Map
    */
   public static void filterTopicReply(Map<String, Object> topicReplyInfoMap) {
      topicReplyInfoMap.put(ParamConst.REPLY_ID, topicReplyInfoMap.get(ParamConst.ID));

      removeKeys(topicReplyInfoMap, new String[] {ParamConst.ID, ParamConst.USER_ID});
   }

   /**
    * 生成 Map
    *    - 构建新的 Map 对象（长度为 1 ）
    *    - 注入唯一的 key-value
    *
    * @param key 键
    * @param value 值
    * @return Map 生成键值对（HashMap）
    */
   public static HashMap<String, Object> generateMap(String key, Object value) {
      HashMap<String, Object> map = new HashMap<>(SetConst.SIZE_ONE);
         map.put(key, value);
      return map;
   }

   /*
    * ***********************************************
    * private method
    * ***********************************************
    */

   /**
    * 仅保留指定的多个 Key
    *
    * @param map 键值队
    * @param keys 要保留的 key
    */
   private static void keepKeys(Map<String, Object> map, String[] keys) {
      //new map, temporary storage fo data
      Map<String, Object> tmpMap = new HashMap<>(keys.length);
      for (String key: keys) {
         tmpMap.put(key, map.get(key));
      }

      //put in data again
      map.clear();
      for (String key: keys) {
         map.put(key, tmpMap.get(key));
      }
   }

   /**
    * 删除多个 Key
    *
    * @param map 传入键值对
    * @param keys 需删除键值对Key数组
    */
   private static void removeKeys(Map<String, Object> map, String[] keys) {
      for (String key : keys) {
         map.remove(key);
      }
   }
}
