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
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_splash);

        animation=findViewById(R.id.animation_view);

        //API initialization
        InitApiService.initApiService();

        //check for user in memory
        checkIfUserIsLoggedIn();

        animation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent;

                if(isUserLoggedIn){
                    intent = new Intent(SplashActivity.this,OffersActivity.class);
                }else{
                    intent = new Intent(SplashActivity.this,LoginActivity.class);
                }

                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle());
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    }, 900 );
                }else{
                    startActivity(intent);
                    finish();
                }


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
            isUserLoggedIn = true;
        }
    }

}
