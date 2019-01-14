package com.example.lovro.myapplication.activities;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;
import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.network.InitApiService;

public class SplashActivity extends AppCompatActivity {

    LottieAnimationView animation;
    private boolean isUserLoggedIn = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        animation=findViewById(R.id.animation_view);

        //API initialization
        InitApiService.initApiService();

        //check for user in memory
        checkIfUserIsLoggedIn();

        animation.setSpeed((float) 1.4);
        animation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent;

                if(isUserLoggedIn){
                    intent = new Intent(SplashActivity.this,HomeActivity.class);
                }else{
                    intent = new Intent(SplashActivity.this,LoginActivity.class);
                }

                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.playAnimation();
    }

    private void checkIfUserIsLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        if(prefs.getBoolean("saved",false)){
            if (!prefs.getString("username","").equals("")){
                isUserLoggedIn = true;
            }
        }
    }

}
