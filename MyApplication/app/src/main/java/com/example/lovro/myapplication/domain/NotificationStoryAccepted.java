package com.example.lovro.myapplication.domain;

public class NotificationStoryAccepted extends Notification {
    private Story story;

    public NotificationStoryAccepted(Story story) {
        this.story = story;
    }

    public Story getStory() {
        return story;
    }
}
