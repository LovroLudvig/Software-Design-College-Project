package com.craftery.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class UserForRegister {
    @Json(name = "username")
    private String username;
    @Json(name = "email")
    private String email;
    @Json(name = "password")
    private String password;

    public UserForRegister(String email,String password,String username){
        this.email=email;
        this.password=password;
        this.username=username;
    }
}
