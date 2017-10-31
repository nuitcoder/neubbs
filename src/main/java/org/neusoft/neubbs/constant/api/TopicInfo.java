package org.neusoft.neubbs.constant.api;

/**
 * 主题信息
 *
 * @author Suvan
 */
public final class TopicInfo {
    private TopicInfo() { }

    public static final String ID = "id";
    public static final String CATEGORY = "category";
    public static final String TITLE = "title";
    public static final String COMMENT = "comment";
    public static final String LASTREPLYUSERID = "lastreplyuserid";
    public static final String LASTREPLYTIME = "lastreplytime";
    public static final String CREATETIME = "createtime";

    public static final String TOPICID = "topicid";
    public static final String CONTENT = "content";
    public static final String READ = "read";
    public static final String AGREE = "agree";

    public static final String USERID = "userid";
    public static final String OPPOSE = "oppose";
    public static final String REPLYID = "replyid";

    public static final String REPLY = "reply";


    /**
     * Topic api 警告信息
     */
    public static final String PARAM_ERROR  = "incorrect input parameter";
    public static final String NO_TOPIC = "no topic";
    public static final String NO_REPLY = "no reply";
    public static final String SAVE_TOPIC_SUCCESS = "save the topic success";
    public static final String SAVE_REPLY_SUCCESS = "save the reply success";
    public static final String REMOVE_TOPIC_SUCCESS = "remove the topic success";
    public static final String REMOVE_REPLY_SUCCESS = "remove the reply success";
    public static final String ALTER_TOPIC_CONTENT_SUCCESS = "alter topic content success";
    public static final String ALTER_REPLY_CONTENT_SUCCESS = "alter reply content success";
}
