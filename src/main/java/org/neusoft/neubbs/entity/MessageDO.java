package org.neusoft.neubbs.entity;

import java.util.Date;

/**
 * forum_message 领域对象
 *
 * @author Suvan
 */
public class MessageDO {

    private Integer id;
    private String source;

    private Integer senderId;
    private Date sendTime;
    private String content;
    private Integer receiverId;
    private Date receiveTime;

    /**
     * Getter
     */
    public Integer getId() {
        return id;
    }
    public String getSource() {
        return source;
    }
    public Integer getSenderId() {
        return senderId;
    }
    public Date getSendTime() {
        return sendTime;
    }
    public String getContent() {
        return content;
    }
    public Integer getReceiverId() {
        return receiverId;
    }
    public Date getReceiveTime() {
        return receiveTime;
    }

    /**
     * Setter
     */
    public void setId(Integer id) {
        this.id = id;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"source\":\"")
                .append(source).append('\"');
        sb.append(",\"senderId\":")
                .append(senderId);
        sb.append(",\"sendTime\":\"")
                .append(sendTime).append('\"');
        sb.append(",\"content\":\"")
                .append(content).append('\"');
        sb.append(",\"receiverId\":")
                .append(receiverId);
        sb.append(",\"receiveTime\":\"")
                .append(receiveTime).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
