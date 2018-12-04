package com.example.lovro.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class StoryDetailsActivity extends AppCompatActivity {
    Story currentStory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean postComment(User user, String text){
        return true;
    }

    private boolean checkCommentEmpty(){
        return true;
    }
}
