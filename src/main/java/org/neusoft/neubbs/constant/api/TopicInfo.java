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


    /**
     * Topic api 警告信息
     */
    String PARAM_ERROR  = "incorrect input parameter";
    String PUBLISH_TOPIC_SUCCESS = "publish the topic successfully";
    String PUBLISH_REPLY_SUCCESS = "publish the reply successfully";
}
