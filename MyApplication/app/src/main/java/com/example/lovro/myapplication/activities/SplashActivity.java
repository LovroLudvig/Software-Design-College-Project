package com.example.lovro.myapplication.activities;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;
import com.example.lovro.myapplication.R;

public class SplashActivity extends AppCompatActivity {

    LottieAnimationView animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_splash);

        animation=findViewById(R.id.animation_view);
        animation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle());

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        finish();
                    }
                }, 900 );

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

}
