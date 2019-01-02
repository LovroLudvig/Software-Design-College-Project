package com.example.lovro.myapplication.domain;

import android.graphics.Bitmap;

import com.squareup.moshi.Json;

public class User {

    @Json(name = "email")
    private String email;

    @Json(name = "passwordHash")
    private String password;

    @Json(name = "username")
    private String username;

    @Json(name = "name")
    private String name;

    @Json(name = "cardNumber")
    private String cardNumber;

    @Json(name = "address")
    private String address;

    @Json(name = "roles")
    private String roles;

    @Json(name = "userStatus")
    private String userStatus;

    @Json(name = "town")
    private Town town;

    public User(String email,String password,String username,String name,String cardNumber,String address,String roles,String userStatus,Town town){
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.cardNumber = cardNumber;
        this.name = name;
        this.roles = roles;
        this.town = town;
        this.userStatus = userStatus;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRoles() {
        return roles;
    }

    public String getUsername() {
        return username;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public Town getTown() {
        return town;
    }

}
