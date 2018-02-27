package org.neusoft.neubbs.entity;

import org.neusoft.neubbs.constant.api.SetConst;

import java.util.Date;

/**
 *  forum_user 领域对象（Domain Object)
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
    private String avator;

    private String rank;
    private Integer state;

    private Date createtime;

    /**
     * Constructor
     */
    public UserDO() {
        this.sex = SetConst.SEX_NO;
        this.birthday = "";
        this.position = "";
        this.description = "";
    }

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
    public String getAvator() {
        return avator;
    }
    public String getRank() {
        return rank;
    }
    public Integer getState() {
        return state;
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
    public void setAvator(String avator) {
        this.avator = avator;
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
                + ", avator='" + avator + '\''
                + ", rank='" + rank + '\''
                + ", state=" + state
                + ", createtime=" + createtime
                + '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || !(o instanceof UserDO)) {
            return false;
        }

        UserDO u = (UserDO) o;
        return !(id != null ? !id.equals(u.id) : u.id != null
                || name != null ? !name.equals(u.name) : u.name != null
                || sex != null ? !sex.equals(u.sex) : u.sex != null
                || birthday != null ? !birthday.equals(u.birthday) : u.birthday != null
                || position != null ? !position.equals(u.position) : u.position != null
                || description != null ? !description.equals(u.description) : u.description != null
                || avator != null ? !avator.equals(u.avator) : u.avator != null
                || rank != null ? !rank.equals(u.rank) : u.rank != null
                || state != null ? !state.equals(u.state) : u.state != null
                || createtime != null ? !createtime.equals(u.createtime) : u.createtime != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        int hash = SetConst.USERDO_HASH_CONSTANT;

        result = hash * result + (name != null ? name.hashCode() : 0);
        result = hash * result + (password != null ? password.hashCode() : 0);
        result = hash * result + (email != null ? email.hashCode() : 0);
        result = hash * result + (sex != null ? sex.hashCode() : 0);
        result = hash * result + (birthday != null ? birthday.hashCode() : 0);
        result = hash * result + (position != null ? position.hashCode() : 0);
        result = hash * result + (description != null ? description.hashCode() : 0);
        result = hash * result + (avator != null ? avator.hashCode() : 0);
        result = hash * result + (rank != null ? rank.hashCode() : 0);
        result = hash * result + (state != null ? state.hashCode() : 0);
        result = hash * result + (createtime != null ? createtime.hashCode() : 0);

        return result;
    }
}
