package org.neusoft.neubbs.entity;

import java.util.Date;

/**
 * forum_topic表 领域对象
 *
 * @author Suvan
 */
public class TopicDO {

    private Integer id;
    private Integer userid;
    private Integer categoryid;

    private String title;
    private Integer replies;
    private Integer lastreplyuserid;
    private Date lastreplytime;
    private Date createtime;

    /**
     * Getter
     */
    public Integer getId() {
        return id;
    }
    public Integer getUserid() {
        return userid;
    }
    public Integer getCategoryid() {
        return categoryid;
    }
    public String getTitle() {
        return title;
    }
    public Integer getReplies() {
        return replies;
    }
    public Integer getLastreplyuserid() {
        return lastreplyuserid;
    }
    public Date getLastreplytime() {
        return lastreplytime;
    }
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * Setter
     */
    public void setId(Integer id) {
        this.id = id;
    }
    public void setUserid(Integer userid) {
        this.userid = userid;
    }
    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setReplies(Integer replies) {
        this.replies = replies;
    }
    public void setLastreplyuserid(Integer lastreplyuserid) {
        this.lastreplyuserid = lastreplyuserid;
    }
    public void setLastreplytime(Date lastreplytime) {
        this.lastreplytime = lastreplytime;
    }
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "TopicDO{"
                + "id=" + id
                + ", userid=" + userid
                + ", categoryid='" + categoryid + '\''
                + ", title='" + title + '\''
                + ", replies='" + replies + '\''
                + ", lastreplyuserid=" + lastreplyuserid
                + ", lastreplytime=" + lastreplytime
                + ", createtime=" + createtime
                + '}';
    }
}
