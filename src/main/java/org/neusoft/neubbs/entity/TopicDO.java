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

    private String category;
    private String title;
    private String comment;

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
    public String getCategory() {
        return category;
    }
    public String getTitle() {
        return title;
    }
    public String getComment() {
        return comment;
    }
    public Integer getLastreplyuserid() {
        return lastreplyuserid;
    }
    public Date getLastreplytime() {
        return lastreplytime;
    }
    private Date getCreatetime() {
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
    public void setCategory(String category) {
        this.category = category;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setComment(String comment) {
        this.comment = comment;
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
                + ", category='" + category + '\''
                + ", title='" + title + '\''
                + ", comment='" + comment + '\''
                + ", lastreplyuserid=" + lastreplyuserid
                + ", lastreplytime=" + lastreplytime
                + ", createtime=" + createtime
                + '}';
    }
}
