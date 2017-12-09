package org.neusoft.neubbs.entity;

/**
 * forum_topic_ccategory 领域模型
 *
 * @author Suvan
 */
public class TopicCategoryDO {

    private Integer id;
    private String nick;
    private String name;

    /**
     * Getter
     */
    public Integer getId() {
        return id;
    }
    public String getNick() {
        return nick;
    }
    public String getName() {
        return name;
    }

    /**
     * Setter
     */
    public void setId(Integer id) {
        this.id = id;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TopicCategoryDO{"
                + "+ id=" + id
                + ",+ nick='" + nick + '\''
                + ",+ name='" + name + '\''
                + '}';
    }
}
