package com.craftery.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class Comment {

    @Json(name = "id")
    private Long id;

    @Json(name = "text")
    private String text;

    @Json(name = "user")
    private User user;

    public Comment(Long id,String text,User user){
        this.id = id;
        this.text = text;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
