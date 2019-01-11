package com.example.lovro.myapplication.domain;

import com.squareup.moshi.Json;
import java.util.List;

public class Story {

    @Json(name = "text")
    private String text;

    @Json(name = "imageUrl")
    private String imageUrl;

    @Json(name = "videoUrl")
    private String videoUrl;

    @Json(name = "status")
    private Status status;

    @Json(name = "comments")
    private List<Comment> comments;

    @Json(name = "id")
    private Long id;

    @Json(name = "user")
    private User user;

    public Story(String text,String imageUrl,String videoUrl,Status status,List<Comment> comments,Long id,User user){
        this.text = text;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.status = status;
        this.comments = comments;
        this.user = user;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getText() {
        return text;
    }

    public Status getStatus() {
        return status;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  Story){
            Story help=(Story) obj;
            return this.id==help.id;
        }
        return false;
    }
}
