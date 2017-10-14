package org.neusoft.neubbs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 *  forum_user表 领域对象
 *  【Domain Object】
 */
public class UserDO {

    private Integer id;

    private String name;

    //@JsonIgnore //生成JSON 忽略该属性
    private String password;
    private String email;
    private String sex;
    private String birthday;
    private String address;

    private String description;          //一句话描述
    private String personalprofile;      //个人简介
    private String image;                //头像地址

    private String rank;                 //级别
    private Integer state;               //激活状态

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
    public String getEmail(){
        return email;
    }
    public String getSex(){
        return birthday;
    }
    public String getBirthday(){
        return birthday;
    }
    public String getAddress(){
        return address;
    }
    public String getDescription(){
        return description;
    }
    public String getPersonalprofile(){
        return personalprofile;
    }
    public String getImage(){
        return image;
    }
    public String getRank(){
        return rank;
    }
    public Integer getState(){
        return state;
    }
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
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
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setPersonalprofile(String personalprofile){
        this.personalprofile = personalprofile;
    }
    public void setImage(String image){
        this.image = image;
    }
    public void setRank(String rank){
        this.rank = rank;
    }
    public void setState(Integer state){
        this.state = state;
    }
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
