package com.example.lovro.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.lovro.myapplication.domain.Story;

import java.util.ArrayList;
import java.util.List;

public class StoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private List<Story> getStorys(){
        List<Story> stories = new ArrayList<>();
        return stories;
    }

    private boolean addStory(){
        return true;
    }







}
