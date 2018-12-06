package com.example.lovro.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.lovro.myapplication.domain.Story;
import com.example.lovro.myapplication.domain.User;

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
