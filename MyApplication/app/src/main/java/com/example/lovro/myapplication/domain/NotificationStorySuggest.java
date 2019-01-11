package com.example.lovro.myapplication.domain;

public class NotificationStorySuggest extends Notification{
    private Story story;

    public NotificationStorySuggest(Story story){
        this.story=story;
    }

    public Story getStory() {
        return story;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NotificationStorySuggest){
            NotificationStorySuggest help = (NotificationStorySuggest) obj;
            return help.story.equals(this.story);
        }
        return false;
    }
}
