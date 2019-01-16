package com.craftery.lovro.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.adapters.CommentAdapter;
import com.craftery.lovro.myapplication.adapters.SectionsPagerAdapter;
import com.craftery.lovro.myapplication.domain.Colors;
import com.craftery.lovro.myapplication.domain.Comment;
import com.craftery.lovro.myapplication.domain.Story;
import com.craftery.lovro.myapplication.events.PauseVideoEvent;
import com.craftery.lovro.myapplication.fragments.ImageFragment;
import com.craftery.lovro.myapplication.fragments.VideoFragment;
import com.craftery.lovro.myapplication.network.ApiService;
import com.craftery.lovro.myapplication.network.InitApiService;
import com.google.gson.Gson;


import org.greenrobot.eventbus.EventBus;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryDetailsActivity extends BasicActivity {
    private Story currentStory;
    private Button slide_number;
    private ViewPager viewPager;
    private TextView username;
    private TextView status;
    private RecyclerView recyclerView;
    private List<Comment> commentList = new ArrayList<>();
    private ApiService apiService;
    private CommentAdapter commentAdapter;
    private Button postBtn;
    private EditText add_comment;
    private Call<List<Comment>> commentCall;
    private String usernameFromUser;
    private NestedScrollView scrollView;
    private ImageView back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storydetails);

        if(savedInstanceState != null){
            restartApp();
        }

        apiService = InitApiService.apiService;
        Gson gson = new Gson();
        String storyAsString = getIntent().getStringExtra("story");


        currentStory = gson.fromJson(storyAsString,Story.class);
        slide_number = findViewById(R.id.slide_number);
        username = findViewById(R.id.story_username);
        status = findViewById(R.id.story_status);
        username.setText(currentStory.getUser().getUsername());
        status.setText(currentStory.getText());
        postBtn = findViewById(R.id.postBtn);
        add_comment = findViewById(R.id.addComment);
        scrollView = findViewById(R.id.nested_scroll);
        back_arrow = findViewById(R.id.story_details_back);

        ((TextView)findViewById(R.id.profile_image_text)).setText(String.valueOf(currentStory.getUser().getUsername().toUpperCase().charAt(0)));
        Drawable background = findViewById(R.id.profile_image_circle).getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Colors.getColor((int) currentStory.getUser().getUsername().toLowerCase().charAt(0)-97));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Colors.getColor((int) currentStory.getUser().getUsername().toLowerCase().charAt(0)-97));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Colors.getColor((int) currentStory.getUser().getUsername().toLowerCase().charAt(0)-97));
        }
        recyclerView = findViewById(R.id.comment_recycler_view);
        recyclerView.setFocusable(false);
        commentList = currentStory.getComments();


        initRecyclerView();
        initAdapter(commentList);
        setupViewPager();
        initListeners();
    }


    private void initListeners(){
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetAvailable()){
                    if(add_comment.getText().toString().length() > 0){
                        if(checkIfUserIsLoggedIn()){
                            addComment(add_comment.getText().toString(),true);
                        }else{
                            addComment(add_comment.getText().toString(),false);
                        }
                    }else{
                        Toast.makeText(StoryDetailsActivity.this,"Comment can't be empty.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    showError("No internet connection!");
                }
            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addComment(final String comment, final boolean loggedIn){
        show_loading("Posting comment...");
        if(loggedIn){
            commentCall = apiService.postCommentUser(currentStory.getId().toString(),usernameFromUser,new Comment(null,comment,null));
        }else{
            commentCall = apiService.postCommentGuest(currentStory.getId().toString(),new Comment(null,comment,null));
        }
        commentCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                stop_loading();
                if(response.isSuccessful()){
                    displayComments(response.body());
                }else{
                    showError("Unexpected error occurred. Please try again!");
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                stop_loading();
                showError("Unexpected error occurred. Please try again!");
            }
        });
    }

    private void displayComments(List<Comment> comments){
        currentStory.setComments(comments);
        add_comment.setText("");
        if(commentAdapter != null){
            commentAdapter.setComments(comments);
        }else{
            initAdapter(comments);
        }
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
            }
        });
    }

    private boolean checkIfUserIsLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        if(prefs.getBoolean("saved",false)){
            usernameFromUser = prefs.getString("username","");
            return true;
        }
        return false;
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
            initPager();
        }else{
            viewPager = findViewById(R.id.slideshow);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(1);
            slide_number.setText("1 OF 1");
        }
    }

    private void initPager(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if(i == 0){
                    slide_number.setVisibility(View.VISIBLE);
                    slide_number.setText("1 OF 2");
                    EventBus.getDefault().post(new PauseVideoEvent());
                }else{
                    slide_number.setText("2 OF 2");
                    slide_number.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
