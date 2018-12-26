package com.example.lovro.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //variable initialisation
        google_login = findViewById(R.id.google_login);
        email_login = findViewById(R.id.email_login);
        terms_of_service = findViewById(R.id.terms);
        sign_in=findViewById(R.id.sign_in);

        //listeners initialisation
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
                startActivity(register_activity);
            }
        });
    }
}
