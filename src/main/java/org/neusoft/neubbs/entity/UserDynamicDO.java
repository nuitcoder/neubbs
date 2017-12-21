package org.neusoft.neubbs.entity;

/**
 * forum_user_dynamic 表，领域对象
 *
 * @author Suvan
 */
public class UserDynamicDO {

    private Integer id;
    private Integer userId;

    private String publicInfoJsonArray;

    /**
     * Setter
     */
    public void setId(Integer id) {
        this.id = id;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setPublicInfoJsonArray(String publicInfoJsonArray) {
        this.publicInfoJsonArray = publicInfoJsonArray;
    }

    /**
     * Setter
     */
    public Integer getId() {
        return id;
    }
    public Integer getUserId() {
        return userId;
    }
    public String getPublicInfoJsonArray() {
        return publicInfoJsonArray;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"userId\":")
                .append(userId);
        sb.append(",\"publicInfoJsonArray\":\"")
                .append(publicInfoJsonArray).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
