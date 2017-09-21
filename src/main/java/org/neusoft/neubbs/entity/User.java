package org.neusoft.neubbs.entity;


import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * User 类
 */
public class User {
    private Integer id;

    @NotNull
    private String name;
    private String password;
    private String sex;
    private String birthday;
    private String address;
    private String phone;
    private String email;
    private Date registerTime;

    //Getter
    public Integer getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public String getSex(){
        return sex;
    }
    public String getBirthday(){
        return birthday;
    }
    public String getAddress(){
        return address;
    }
    public String getPhone(){
        return phone;
    }
    public String getEmail(){
        return email;
    }
    public Date getRegisterTime(){
        return registerTime;
    }

    //Setter
    //public void setId(Integer id){
    //    this.id = id;
    //}
    public void setName(String name){
        this.name = name;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setSex(String sex){
        this.sex = sex;
    }
    public void setBirthday(String birthday){
        this.birthday = birthday;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setEmail(String email){
        this.email = email;
    }
    //public void setRegistertime(Date registerTime){
    //    this.registerTime = registerTime;
    //}
}
