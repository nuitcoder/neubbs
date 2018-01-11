package org.neusoft.neubbs.constant.api;

/**
 * 参数信息
 *
 * @author Suvan
 */
public final class ParamConst {

    private ParamConst() { }

    /**
     * id
     */
    public static final String ID = "id";
    public static final String USER_ID = "userid";
    public static final String LAST_REPLY_USER_ID = "lastreplyuserid";
    public static final String FOLLOWING_USER_ID = "followingUserId";
    public static final String TOPIC_ID = "topicid";
    public static final String CATEGORY_ID = "categoryid";
    public static final String REPLY_ID = "replyid";
    public static final String USER_LIKE_TOPIC_ID = "userLikeTopicId";
    public static final String USER_COLLECT_TOPIC_ID = "userCollectTopicId";
    public static final String USER_ATTENTION_TOPIC_ID = "userAttentionTopicId";

    /**
     * 用户信息
     */
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String SEX = "sex";
    public static final String BIRTHDAY = "birthday";
    public static final String POSITION = "position";
    public static final String DESCRIPTION = "description";
    public static final String RANK = "rank";
    public static final String STATE = "state";
    public static final String AVATOR = "avator";
    public static final String USER = "user";
    public static final String LAST_REPLY_USER = "lastreplyuser";

    /**
     * 主动关注人与粉丝
     */
    public static final String FOLLOWING = "following";
    public static final String FOLLOWED = "followed";

    /**
     * 时间
     */
    public static final String CREATETIME = "createtime";
    public static final String LAST_REPLY_TIME = "lastreplytime";

    /**
     * 邮箱计时器
     */
    public static final String TIMER = "timer";

    /**
     * 验证码
     */
    public static final String CAPTCHA = "captcha";

    /**
     * 认证
     */
    public static final String TOKEN = "token";
    public static final String AUTHENTICATION = "authentication";

    /**
     * 默认头像
     */
    public static final String USER_DEFAULT_IMAGE = "default-avator-min.jpeg";

    /**
     * 话题信息
     */
    public static final String TOPIC = "topic";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String TOPIC_CONTENT = "topicContent";
    public static final String TOPIC_TITLE = "topicTitle";

    /**
     * 话题喜欢，收藏，关注，阅读
     */
    public static final String LIKE = "like";
    public static final String COLLECT = "collect";
    public static final String ATTENTION = "attention";
    public static final String READ = "read";
    public static final String IS_LIKE_TOPIC  = "isliketopic";
    public static final String COMMAND = "command";

    /**
     * 话题分类
     */
    public static final String CATEGORY = "category";
    public static final String NICK = "nick";
    public static final String TOPIC_CATEGORY_NICK = "topicCategoryNick";

    /**
     * 话题回复
     */
    public static final String REPLY = "reply";
    public static final String REPLY_CONTENT = "replyContent";
    public static final String REPLY_LIST = "replylist";

    /**
     * 数量
     */
    public static final String NUMBER = "number";

    /**
     * 分页
     */
    public static final String TOTAL_PAGES = "totalpages";

    /**
     * 统计
     *      - 在线登陆人数
     */
    public static final String LOGIN_USER = "loginUser";

    /**
     * TCP/IP 协议
     *      - http
     *      - ftp
     */
    public static final String HTTP = "http";
}
