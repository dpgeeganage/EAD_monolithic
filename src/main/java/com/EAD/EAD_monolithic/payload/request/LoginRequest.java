package com.EAD.EAD_monolithic.payload.request;

import javax.validation.constraints.NotBlank;

//LoginRequest comes when sign in has 2 fields 'username' and 'password'
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;


    //getters and setters to access and modify
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

