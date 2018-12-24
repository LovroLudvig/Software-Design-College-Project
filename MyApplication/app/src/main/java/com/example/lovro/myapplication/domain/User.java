package com.example.lovro.myapplication.domain;

import android.graphics.Bitmap;

public class User {

    private String email;
    private String password;
    private String username;
    private Bitmap profile_picture;

    public User(String email,String password,String username,Bitmap profile_picture){
        this.email = email;
        this.password = password;
        this.profile_picture = profile_picture;
        this.username = username;
    }
}
