package com.example.lovro.myapplication.activities;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.adapters.CommentAdapter;
import com.example.lovro.myapplication.adapters.SectionsPagerAdapter;
import com.example.lovro.myapplication.domain.Colors;
import com.example.lovro.myapplication.domain.Comment;
import com.example.lovro.myapplication.domain.Story;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.fragments.ImageFragment;
import com.example.lovro.myapplication.fragments.VideoFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class StoryDetailsActivity extends AppCompatActivity {
    private Story currentStory;
    private Button slide_number;
    private ViewPager viewPager;
    private TextView username;
    private TextView status;
    private RecyclerView recyclerView;
    private List<Comment> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storydetails);

        Gson gson = new Gson();
        String storyAsString = getIntent().getStringExtra("story");
        currentStory = gson.fromJson(storyAsString,Story.class);
        slide_number = findViewById(R.id.slide_number);
        username = findViewById(R.id.story_username);
        status = findViewById(R.id.story_status);
        username.setText(currentStory.getUser().getUsername());
        status.setText(currentStory.getText());

        ((TextView)findViewById(R.id.profile_image_text)).setText(String.valueOf(currentStory.getUser().getUsername().toUpperCase().charAt(0)));
        Drawable background = findViewById(R.id.profile_image_circle).getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Colors.getColor((int) currentStory.getUser().getUsername().toLowerCase().charAt(0)-97));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Colors.getColor((int) currentStory.getUser().getUsername().charAt(0)-97));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Colors.getColor((int) currentStory.getUser().getUsername().charAt(0)-97));
        }
        recyclerView = findViewById(R.id.comment_recycler_view);

        Comment comment = new Comment(Long.getLong("43"),"Predobar status!!!",new User(null,"ilovrencic",null,null,null,null));
        Comment comment1 = new Comment(Long.getLong("44"),"Daj ne seri! Svaka čast!",new User(null,"lludvig",null,null,null,null));
        Comment comment2 = new Comment(Long.getLong("45"),"Jedva cekam vidjeti kak to izgledati!",new User(null,"mdadanovic",null,null,null,null));
        Comment comment3 = new Comment(Long.getLong("46"),"Trump is RACIST!",new User(null,"mcolja",null,null,null,null));
        Comment comment4 = new Comment(Long.getLong("47"),"OMG! I am triggered. Report!!!",new User(null,"ilovrencic",null,null,null,null));
        commentList.add(comment);
        commentList.add(comment1);
        commentList.add(comment2);
        commentList.add(comment3);
        commentList.add(comment4);


        initRecyclerView();
        initAdapter(commentList);
        setupViewPager();
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initAdapter(List<Comment> comments){
        commentAdapter = new CommentAdapter(comments);
        recyclerView.setAdapter(commentAdapter);
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
