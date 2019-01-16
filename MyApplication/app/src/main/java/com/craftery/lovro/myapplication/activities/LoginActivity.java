package com.craftery.lovro.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.network.InitApiService;

public class LoginActivity extends AppCompatActivity {

    //Variable definition
    private Button guest_login;
    private TextView email_login;
    private TextView terms_of_service;
    private Button sign_in;
    private int backButtonCount=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.theme1) ;
        setContentView(R.layout.activity_login);

        if(savedInstanceState != null){
            InitApiService.initApiService();
        }

        //variable initialisation
        guest_login = findViewById(R.id.guest_login);
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
                signin_activity.putExtra("from","login");
                startActivity(signin_activity);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        guest_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        email_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent register_activity = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(register_activity);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                //Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(LoginActivity.this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                //startActivity(register_activity, bundle);
            }
        });

        terms_of_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_terms();
            }
        });
    }

    private void open_terms(){
        LayoutInflater inflater= LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.terms_of_service_layout, null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.ic_library_books_black_24dp);
        alertDialog.setView(view);

        AlertDialog alert = alertDialog.create();
        alert.show();
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
