package org.neusoft.neubbs.entity;

/**
 * forum_user_action 表，领域对象
 *
 * @author Suvan
 */
public class UserActionDO {

    private Integer id;
    private Integer userid;

    private String likeTopicidJsonArray;

    /**
     * Setter
     */
    public void setId(Integer id) {
        this.id = id;
    }
    public void setUserid(Integer userid) {
        this.userid = userid;
    }
    public void setLikeTopicidJsonArray(String likeTopicidJsonArray) {
        this.likeTopicidJsonArray = likeTopicidJsonArray;
    }

    /**
     * Getter
     */
    public Integer getId() {
        return id;
    }
    public Integer getUserid() {
        return userid;
    }
    public String getLikeTopicidJsonArray() {
        return likeTopicidJsonArray;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"userid\":")
                .append(userid);
        sb.append(",\"likeTopicidJsonArray\":\"")
                .append(likeTopicidJsonArray).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
