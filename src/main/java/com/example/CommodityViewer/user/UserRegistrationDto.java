package com.example.CommodityViewer.user;

public class UserRegistrationDto {
    private String email;
    private String password;

    public UserRegistrationDto() {
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
}