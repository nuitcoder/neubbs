package org.neusoft.neubbs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 *  forum_user表 领域对象
 *  【Domain Object】
 *
 *  @author Suvan
 */
public class UserDO {
    private Integer id;

    private String name;

    private String password;
    private String email;

    private Integer sex;

    private String birthday;
    private String position;

    private String description;
    private String image;

    private String rank;
    private Integer state;

    private Date createtime;

    /**
     * Getter
     */
    public Integer getId() {
        return  id;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public Integer getSex() {
        return sex;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getPosition() {
        return position;
    }
    public String getDescription() {
        return description;
    }
    public String getImage() {
        return image;
    }
    public String getRank() {
        return rank;
    }
    public Integer getState() {
        return state;
    }
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * Setter
     */
    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "UserDO{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", password='" + password + '\''
                + ", email='" + email + '\''
                + ", sex='" + sex + '\''
                + ", birthday='" + birthday + '\''
                + ", position='" + position + '\''
                + ", description='" + description + '\''
                + ", image='" + image + '\''
                + ", rank='" + rank + '\''
                + ", state=" + state
                + ", createtime=" + createtime
                + '}';
    }
}
