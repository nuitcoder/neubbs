package org.neusoft.neubbs.entity;

/**
 * forum_topic_action 表，领域对象
 *
 * @author Suvan
 */
public class TopicActionDO {

    private Integer id;
    private Integer topicId;

    private String replyUserIdJsonArray;
    private String likeUserIdJsonArray;
    private String collectUserIdJsonArray;
    private String attentionUserIdJsonArray;

    /**
     * Setter
     */
    public void setId(Integer id) {
        this.id = id;
    }
    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }
    public void setReplyUserIdJsonArray(String replyUserIdJsonArray) {
        this.replyUserIdJsonArray = replyUserIdJsonArray;
    }
    public void setLikeUserIdJsonArray(String likeUserIdJsonArray) {
        this.likeUserIdJsonArray = likeUserIdJsonArray;
    }
    public void setCollectUserIdJsonArray(String collectUserIdJsonArray) {
        this.collectUserIdJsonArray = collectUserIdJsonArray;
    }
    public void setAttentionUserIdJsonArray(String attentionUserIdJsonArray) {
        this.attentionUserIdJsonArray = attentionUserIdJsonArray;
    }

    /**
     * Getter
     */
    public Integer getId() {
        return id;
    }
    public Integer getTopicId() {
        return topicId;
    }
    public String getReplyUserIdJsonArray() {
        return replyUserIdJsonArray;
    }
    public String getLikeUserIdJsonArray() {
        return likeUserIdJsonArray;
    }
    public String getCollectUserIdJsonArray() {
        return collectUserIdJsonArray;
    }
    public String getAttentionUserIdJsonArray() {
        return attentionUserIdJsonArray;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"topicId\":")
                .append(topicId);
        sb.append(",\"replyUserIdJsonArray\":\"")
                .append(replyUserIdJsonArray).append('\"');
        sb.append(",\"likeUserIdJsonArray\":\"")
                .append(likeUserIdJsonArray).append('\"');
        sb.append(",\"collectUserIdJsonArray\":\"")
                .append(collectUserIdJsonArray).append('\"');
        sb.append(",\"attentionUserIdJsonArray\":\"")
                .append(attentionUserIdJsonArray).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
