package org.neusoft.neubbs.entity;

/**
 * forum_topic_content 领域对象
 *
 * @author Suvan
 */
public class TopicContentDO {
    private Integer id;
    private Integer topicid;

    private String content;

    private Integer read;
    private Integer like;

    /**
     * Getter
     */
    public Integer getId() {
        return id;
    }
    public Integer getTopicid() {
        return topicid;
    }
    public String getContent() {
        return content;
    }
    public Integer getRead() {
        return read;
    }
    public Integer getLike() {
        return like;
    }

    /**
     * Setter
     */
    public void setId(Integer id) {
        this.id = id;
    }
    public void setTopicid(Integer topicid) {
        this.topicid = topicid;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setRead(Integer read) {
        this.read = read;
    }
    public void setLike(Integer like) {
        this.like = like;
    }

    @Override
    public String toString() {
        return "TopicContentDO{"
                + "id=" + id
                + ", topicid=" + topicid
                + ", content='" + content + '\''
                + ", read='" + read + '\''
                + ", like='" + like + '\''
                + '}';
    }
}
