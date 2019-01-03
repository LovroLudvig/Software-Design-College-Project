package com.example.lovro.myapplication.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.transition.Fade;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.network.ApiService;
import com.example.lovro.myapplication.network.InitApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BasicActivity {

    private Button sign_in_button;
    private TextInputEditText username;
    private TextInputEditText password;
    private ApiService apiService = InitApiService.apiService;
    private View backButon;
    private Call<ResponseBody> callLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.theme1) ;

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setExitTransition(new Fade());
            getWindow().setEnterTransition(new Fade());
        }
        setContentView(R.layout.activity_sign_in);

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
                        login_user(auth);
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




    private void saveUserInMemory(){
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.putBoolean("saved",true);
        editor.apply();
    }

    private void login_user(String auth){
        show_loading();
        callLogin = apiService.loginUser(auth);
        callLogin.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                stop_loading();
                if(response.isSuccessful()){
                    saveUserInMemory();
                    Intent intent = new Intent(getApplicationContext(), OffersActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    if(response.code() == 401){
                        showError("Wrong email or password!");
                    }else{
                        showError("Unexpected error occurred. Please try again!");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
