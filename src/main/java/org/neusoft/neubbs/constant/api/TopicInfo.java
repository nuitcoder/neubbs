package org.neusoft.neubbs.constant.api;

/**
 * 主题信息
 *
 * @author Suvan
 */
public interface TopicInfo {
    String ID = "id";
    String CATEGORY = "category";
    String TITLE = "title";
    String COMMENT = "comment";
    String LASTREPLYUSERID = "lastreplyuserid";
    String LASTREPLYTIME = "lastreplytime";
    String CREATETIME = "createtime";

    String TOPICID = "topicid";
    String CONTENT = "content";
    String READ = "read";
    String AGREE = "agree";

    String USERID = "userid";
    String OPPOSE = "oppose";
    String REPLYID = "replyid";


    /**
     * Topic api 警告信息
     */
    String PARAM_ERROR  = "incorrect input parameter";
    String NO_TOPIC = "no topic";
    String NO_REPLY = "no reply";
    String SAVE_TOPIC_SUCCESS = "save the topic success";
    String SAVE_REPLY_SUCCESS = "save the reply success";
    String REMOVE_TOPIC_SUCCESS = "remove the topic success";
    String REMOVE_REPLY_SUCCESS = "remove the reply success";
    String ALTER_TOPIC_CONTENT_SUCCESS = "alter topic content success";
    String ALTER_REPLY_CONTENT_SUCCESS = "alter reply content success";
}
