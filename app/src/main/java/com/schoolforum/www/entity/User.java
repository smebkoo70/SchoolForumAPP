package com.schoolforum.www.entity;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String loginnname;
    private String password;
    private String username;
    private String studentno;
    private String major;
    private String telephone;
    private String sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginnname() {
        return loginnname;
    }

    public void setLoginnname(String loginnname) {
        this.loginnname = loginnname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStudentno() {
        return studentno;
    }

    public void setStudentno(String studentno) {
        this.studentno = studentno;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public User() {
    }

    public User(String id,String loginnname, String password, String username, String studentno, String major, String telephone, String sex) {
        this.id = id;
        this.loginnname = loginnname;
        this.password = password;
        this.username = username;
        this.studentno = studentno;
        this.major = major;
        this.telephone = telephone;
        this.sex = sex;
    }
}
