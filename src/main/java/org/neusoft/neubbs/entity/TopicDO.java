package org.neusoft.neubbs.entity;

import java.util.Date;

/**
 * forum_topic表 领域对象
 */
public class TopicDO {
    private Integer id;
    private Integer userid;

    private String category;
    private String title;
    private String comment;

    private Date lastreplaytime;
    private Date createtime;

    /**
     * Getter
     */
    public Integer getId(){
        return id;
    }
    public Integer getUserid(){
        return userid;
    }
    public String getCategory(){
        return category;
    }
    public String getTitle(){
        return title;
    }
    public String getComment(){
        return comment;
    }
    public Date getLastreplaytime(){
        return lastreplaytime;
    }
    private Date getCreatetime(){
        return createtime;
    }

    /**
     * Setter
     */
    public void setId(Integer id){
        this.id = id;
    }
    public void setUserid(Integer userid){
        this.userid = userid;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public void setLastreplaytime(Date lastreplaytime){
        this.lastreplaytime = lastreplaytime;
    }
    public void setCreatetime(Date createtime){
        this.createtime = createtime;
    }
}
