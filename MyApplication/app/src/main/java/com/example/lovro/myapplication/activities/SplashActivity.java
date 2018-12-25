package com.example.lovro.myapplication.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.lovro.myapplication.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView app_logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        app_logo = findViewById(R.id.app_logo);
        startAnimation();
    }

    private void startAnimation(){
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        app_logo.startAnimation(myFadeInAnimation);

        myFadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startApplication();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }



    private void startApplication(){
        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
