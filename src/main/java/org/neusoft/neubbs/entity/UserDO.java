package org.neusoft.neubbs.entity;

import java.util.Date;

/**
 *  对应forum_user表 数据对象
 */
public class UserDO {

    private Integer id;

    private String name;
    private String password;
    private String sex;
    private String birthday;
    private String phone;
    private String email;
    private String address;
    private String rank;

    private Date createtime;

    /**
     * Getter
     */
    public Integer getId(){
        return  id;
    }
    public String getName(){
        return  name;
    }
    public String getPassword(){
        return password;
    }
    public String getSex(){
        return birthday;
    }
    public String getBirthday(){
        return birthday;
    }
    public String getPhone(){
        return  phone;
    }
    public String getEmail(){
        return email;
    }
    public String getAddress(){
        return address;
    }
    public String getRank(){
        return rank;
    }
    public Date getCreatetime(){
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
    public void setSex(String sex) {
        this.sex = sex;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setRank(String rank){
        this.rank = rank;
    }
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
