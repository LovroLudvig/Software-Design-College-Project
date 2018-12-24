package com.example.lovro.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lovro.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    //Variable definition
    private Button google_login;
    private Button email_login;
    private TextView sign_up;
    private TextView terms_of_service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //variable initialisation
        google_login = findViewById(R.id.google_login);
        email_login = findViewById(R.id.email_login);
        sign_up = findViewById(R.id.sign_in);
        terms_of_service = findViewById(R.id.terms);

        //listeners initialisation
        initListeners();
    }

    private void initListeners(){
        email_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_activity = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(register_activity);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         }
        });
    }
}
