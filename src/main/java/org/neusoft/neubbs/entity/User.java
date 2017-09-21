package org.neusoft.neubbs.entity;


import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * 【实体类】
 *
 * @Author Suvan
 * @Date 2017-09-21
 */
public class User {
    private Integer id;             //用户id(无setter)

    @NotNull
    private String name;            //名字
    private String password;        //密码
    private String sex;             //性别
    private String birthday;        //出生年月日
    private String address;         //地址
    private String phone;           //电话
    private String email;           //邮箱


    private Date registertime;      //注册时间(无setter)


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
    public Date getRegistertime(){
        return registertime;
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
    public  void setPhone(String phone){
        this.phone = phone;
    }
    public void setEmail(String email){
        this.email = email;
    }
    //public void setRegistertime(Date registertime){
    //    this.registertime = registertime;
    //}
}


/*
   数据表插入脚本
    CREATE TABLE user(
        u_id INT  AUTO_INCREMENT primary key,
        u_name VARCHAR(15) UNIQUE KEY,
        u_password VARCHAR(15) NOT NULL,
        u_sex  VARCHAR(2),
        u_birthday VARCHAR(20),
        u_address VARCHAR(15),
        u_phone VARCHAR(15),
        u_email  VARCHAR(50)  NOT NULL,
        u_registertime DATETIME DEFAULT NOW()
     )ENGINE = innoDB;
*/
