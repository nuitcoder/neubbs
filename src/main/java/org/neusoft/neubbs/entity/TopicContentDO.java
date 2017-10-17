package org.neusoft.neubbs.entity;

/**
 * forum_topic_content 表领域对象
 *
 * @author Suvan
 */
public class TopicContentDO {
    private Integer id;
    private Integer topicid;

    private String content;
    private String read;
    private String agree;

    /**
     * Getter
     */
    public Integer getId(){
        return id;
    }
    public Integer getTopicid(){
        return topicid;
    }
    public String getContent(){
        return content;
    }
    public String getRead(){
        return read;
    }
    public String getAgree() {
        return agree;
    }


    /**
     * Setter
     */
    public void setId(Integer id){
        this.id = id;
    }
    public void setTopicid(Integer topicid){
        this.topicid = topicid;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setRead(String read){
        this.read = read;
    }
    public void setAgree(String agree){
        this.agree = agree;
    }

    @Override
    public String toString() {
        return "TopicContentDO{" +
                "id=" + id +
                ", topicid=" + topicid +
                ", content='" + content + '\'' +
                ", read='" + read + '\'' +
                ", agree='" + agree + '\'' +
                '}';
    }
}
