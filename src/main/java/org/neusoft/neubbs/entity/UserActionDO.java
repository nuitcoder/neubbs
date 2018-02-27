package org.neusoft.neubbs.entity;

/**
 * forum_user_action 领域对象
 *
 * @author Suvan
 */
public class UserActionDO {

    private Integer id;
    private Integer userId;

    private String likeTopicIdJsonArray;
    private String collectTopicIdJsonArray;
    private String attentionTopicIdJsonArray;
    private String followingUserIdJsonArray;
    private String followedUserIdJsonArray;

    /**
     * Setter
     */
    public void setId(Integer id) {
        this.id = id;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setLikeTopicIdJsonArray(String likeTopicIdJsonArray) {
        this.likeTopicIdJsonArray = likeTopicIdJsonArray;
    }
    public void setCollectTopicIdJsonArray(String collectTopicIdJsonArray) {
        this.collectTopicIdJsonArray = collectTopicIdJsonArray;
    }
    public void setAttentionTopicIdJsonArray(String attentionTopicIdJsonArray) {
        this.attentionTopicIdJsonArray = attentionTopicIdJsonArray;
    }
    public void setFollowingUserIdJsonArray(String followingUserIdJsonArray) {
        this.followingUserIdJsonArray = followingUserIdJsonArray;
    }
    public void setFollowedUserIdJsonArray(String followedUserIdJsonArray) {
        this.followedUserIdJsonArray = followedUserIdJsonArray;
    }

    /**
     * Getter
     */
    public Integer getId() {
        return id;
    }
    public Integer getUserId() {
        return userId;
    }
    public String getLikeTopicIdJsonArray() {
        return likeTopicIdJsonArray;
    }
    public String getCollectTopicIdJsonArray() {
        return collectTopicIdJsonArray;
    }
    public String getAttentionTopicIdJsonArray() {
        return attentionTopicIdJsonArray;
    }
    public String getFollowingUserIdJsonArray() {
        return followingUserIdJsonArray;
    }
    public String getFollowedUserIdJsonArray() {
        return followedUserIdJsonArray;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"userId\":")
                .append(userId);
        sb.append(",\"likeTopicIdJsonArray\":\"")
                .append(likeTopicIdJsonArray).append('\"');
        sb.append(",\"collectTopicIdJsonArray\":\"")
                .append(collectTopicIdJsonArray).append('\"');
        sb.append(",\"attentionTopicIdJsonArray\":\"")
                .append(attentionTopicIdJsonArray).append('\"');
        sb.append(",\"followingUserIdJsonArray\":\"")
                .append(followingUserIdJsonArray).append('\"');
        sb.append(",\"followedUserIdJsonArray\":\"")
                .append(followedUserIdJsonArray).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
