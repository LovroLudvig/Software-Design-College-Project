package com.example.lovro.myapplication.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovro.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    //Variable definition
    private Button google_login;
    private Button email_login;
    private TextView terms_of_service;
    private TextView sign_in;
    private int backButtonCount=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade());
        getWindow().setBackgroundDrawableResource(R.drawable.theme1) ;

// set an exit transition


        setContentView(R.layout.activity_login);

        //variable initialisation
        google_login = findViewById(R.id.google_login);
        email_login = findViewById(R.id.email_login);
        terms_of_service = findViewById(R.id.terms);
        sign_in=findViewById(R.id.sign_in);

        //listeners initialisation
        // inside your activity (if you did not enable transitions in your theme)

        initListeners();
    }

    private void initListeners(){
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin_activity = new Intent(LoginActivity.this,SignInActivity.class);
                startActivity(signin_activity);
            }
        });

        email_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent register_activity = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(register_activity,
                        ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        backButtonCount=0;
        getWindow().setBackgroundDrawableResource(R.drawable.theme1);
    }

    @Override
    public void onBackPressed()
    {

        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
