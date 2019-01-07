package com.example.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class Notification {

    //OVO JE SVE MOCKANO I CIJELU KLASU TREBA NAPISAT, nisam znao koja polja da stavim pa sam ova
    private String userWhoCaused;
    private String text;
    private String date;

    public Notification(String userWhoCaused, String text, String date) {
        this.userWhoCaused = userWhoCaused;
        this.text = text;
        this.date = date;
    }

    public String getUserWhoCaused() {
        return userWhoCaused;
    }

    public void setUserWhoCaused(String userWhoCaused) {
        this.userWhoCaused = userWhoCaused;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
