package com.craftery.lovro.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.domain.Role;
import com.craftery.lovro.myapplication.domain.User;
import com.craftery.lovro.myapplication.network.ApiService;
import com.craftery.lovro.myapplication.network.InitApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BasicActivity {

    private Button sign_in_button;
    private TextInputEditText username;
    private TextInputEditText password;
    private ApiService apiService = InitApiService.apiService;
    private View backButon;
    private Call<User> callLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.theme1) ;
        setContentView(R.layout.activity_sign_in);

        if(savedInstanceState != null){
            InitApiService.initApiService();
        }

        sign_in_button=findViewById(R.id.login_button);
        backButon=findViewById(R.id.backButton);
        username = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);

        if(getIntent().getStringExtra("from").equals("register")){
            username.setText(getIntent().getStringExtra("username"));
            password.setText(getIntent().getStringExtra("pass"));
        }

        initlisteners();
    }

    private void initlisteners() {

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs(username.getText().toString(),password.getText().toString())){
                    String token = username.getText().toString().trim()+":"+password.getText().toString().trim();
                    String encoded_token = Base64.encodeToString(token.getBytes(),0);
                    String auth = "Basic"+" "+encoded_token.trim();
                    if(isInternetAvailable()){
                        hideKeyboard();
                        login_user(auth, username.getText().toString().trim());
                    }else{
                        showError("No internet connection!");
                    }
                }
            }
        });

        backButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private boolean checkInputs(String username,String password){
        if(username.equals("") || password.equals("")){
            Toast.makeText(SignInActivity.this,"Please fill all inputs!",Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }

    private boolean checkIfUserAdmin(User user) {
        for (Role role:user.getRoles()){
            if (role.getId()==4){
                return true;
            }
        }
        return false;
    }




    private void saveUserInMemory(User user){
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.putBoolean("saved",true);
        if(checkIfUserAdmin(user)){
            editor.putBoolean("admin",true);
        }else{
            editor.putBoolean("admin",false);
        }
        editor.apply();
    }

    private void login_user(String auth,String username){
        show_loading("Logging in...");
        callLogin = apiService.loginUser(auth, new User(username));
        callLogin.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                stop_loading();
                if(response.isSuccessful()){
                    saveUserInMemory(response.body());
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    if(response.code() == 401){
                        showError("Wrong username or password!");
                    }else{
                        showError("Unexpected error occurred. Please try again!");
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                stop_loading();
                if(call.isCanceled()){
                    //nothing
                }else{
                    showError("Unknown error occurred. Please try again!");
                    //showError(t.getMessage());
                    //t.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        if(callLogin != null){
            callLogin.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(callLogin != null){
            callLogin.cancel();
        }
        super.onStop();
    }

}
