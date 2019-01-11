package com.example.lovro.myapplication.domain;

public class NotificationStorySuggest extends Notification{
    private Story story;

    public NotificationStorySuggest(Story story){
        this.story=story;
    }

    public Story getStory() {
        return story;
    }
}
