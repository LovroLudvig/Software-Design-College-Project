package com.example.lovro.myapplication.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.adapters.SectionsPagerAdapter;
import com.example.lovro.myapplication.domain.Story;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.fragments.ImageFragment;
import com.example.lovro.myapplication.fragments.VideoFragment;
import com.google.gson.Gson;

public class StoryDetailsActivity extends AppCompatActivity {
    private Story currentStory;
    private Button slide_number;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storydetails);

        Gson gson = new Gson();
        String storyAsString = getIntent().getStringExtra("story");
        currentStory = gson.fromJson(storyAsString,Story.class);
        slide_number = findViewById(R.id.slide_number);


        setupViewPager();
    }

    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ImageFragment(currentStory));

        if(currentStory.getVideoUrl() != null){
            adapter.addFragment(new VideoFragment(currentStory));
            viewPager = findViewById(R.id.slideshow);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(2);
            initListeners();
        }else{
            viewPager = findViewById(R.id.slideshow);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(1);
            slide_number.setText("1 OF 1");
        }
    }

    private void initListeners(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if(i == 0){
                    slide_number.setText("1 OF 2");
                }else{
                    slide_number.setText("2 OF 2");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

}
