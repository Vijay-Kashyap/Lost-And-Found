package com.example.lostandfound;

public class User {
    private String username,reg,email,mobile,pass;

    public User(String username, String reg, String email, String mobile, String pass) {
        this.username = username;
        this.reg = reg;
        this.email = email;
        this.mobile = mobile;
        this.pass = pass;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
