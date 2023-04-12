package com.example.myapplication;

public class UserHelperClass {
    String name,username,email,phoneNo,password;

    public UserHelperClass(String name,String phoneNo, String username,String email, String password) {
        this.name = String.valueOf(name);
        this.phoneNo=String.valueOf(phoneNo);
        this.username = String.valueOf(username);
        this.email = String.valueOf(email);
        this.password = String.valueOf(password);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
