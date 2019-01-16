package com.craftery.lovro.myapplication.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.domain.User;
import com.craftery.lovro.myapplication.network.ApiService;
import com.craftery.lovro.myapplication.network.GenericResponse;
import com.craftery.lovro.myapplication.network.InitApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BasicActivity {

    private TextInputEditText email;
    private TextInputLayout email_layout;
    private TextInputEditText username;
    private TextInputLayout username_layout;
    private TextInputEditText password;
    private TextInputLayout password_layout;
    private Button register_button;
    private TextView terms_of_service;
    private View backButton;
    private Call<GenericResponse<User>> callRegister;
    private ApiService apiService;
    private static final int GET_FROM_GALLERY = 3;
    private static int PERMISSION_FOR_GALLERY = 97;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawableResource(R.drawable.theme1) ;
        setContentView(R.layout.activity_register);

        if(savedInstanceState != null){
            InitApiService.initApiService();
        }

        email = findViewById(R.id.email_input);
        username = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);
        register_button = findViewById(R.id.register_button);
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);
        username_layout = findViewById(R.id.username_layout);
        backButton = findViewById(R.id.backButton);
        terms_of_service = findViewById(R.id.terms_register);

        //Initialization of static api service
        apiService = InitApiService.apiService;

        //Initialization of listeners
        initListeners();
    }

    private void initListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs()){
                    if(isValidEmail(email.getText().toString())){
                        email_layout.setErrorEnabled(false);
                        if(isInternetAvailable()){
                            hideKeyboard();
                            User user = new User(email.getText().toString(),password.getText().toString(),username.getText().toString(),null,null,null,null,null,null);
                            register(user);
                        }else {
                            showError("No internet connection!");
                        }
                    }else{
                        email_layout.setError("Invalid e-mail");
                    }
                }
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







    private boolean checkInputs(){
        if(email.getText().toString().equals("")){
            email_layout.setErrorEnabled(true);
            email_layout.setError("E-mail can't be empty!");
            return false;
        }
        if(username.getText().toString().equals("")){
            email_layout.setErrorEnabled(false);
            username_layout.setErrorEnabled(true);
            username_layout.setError("Username can't be empty!");
            return false;
        }

        if(password.getText().toString().equals("")){
            username_layout.setErrorEnabled(false);
            password_layout.setErrorEnabled(true);
            password_layout.setError("Password can't be empty!");
            return false;
        }

        if(password.getText().length() < 6){
            username_layout.setErrorEnabled(false);
            password_layout.setErrorEnabled(true);
            password_layout.setError("Password too short - Minimum length is 6!");
            return false;
        }

        email_layout.setErrorEnabled(false);
        username_layout.setErrorEnabled(false);
        password_layout.setErrorEnabled(false);
        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }




    private void register(User user){
        show_loading("Registering in process...");
        callRegister = apiService.registerUser(user);
        callRegister.enqueue(new Callback<GenericResponse<User>>() {
            @Override
            public void onResponse(Call<GenericResponse<User>> call, Response<GenericResponse<User>> response) {
                stop_loading();
                if(response.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,"Registration successful!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                    intent.putExtra("from","register");
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("pass",password.getText().toString());
                    startActivity(intent);
                    //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }else {
                    if(response.code() == 400){
                        showError("E-mail or username is already in use!");
                    }else{
                        showError("Unexpected error occurred. Please try again!");
                    }
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<User>> call, Throwable t) {
                stop_loading();
                if(call.isCanceled()){
                    //nothing
                }else{
                    showError("Unexpected error occurred. Please try again!");
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        if(!(email.getText().toString().equals("")) || !(username.getText().toString().equals("")) || !(password.getText().toString().equals(""))){
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?\nYour entries won't be saved!")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //email.setText("");
                            //username.setText("");
                            //password.setText("");
                            //email_layout.setHint("");
                            //password_layout.setHint("");
                            //username_layout.setHint("");
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }else {
            //if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
              //  email_layout.setHint("");
                //password_layout.setHint("");
                //username_layout.setHint("");
            //}
            super.onBackPressed();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setBackgroundDrawableResource(R.drawable.theme1);
    }


    @Override
    protected void onPause() {
        if(callRegister != null){
            callRegister.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(callRegister != null){
            callRegister.cancel();
        }
        super.onStop();
    }
}
