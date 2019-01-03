package com.example.lovro.myapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Fade;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.network.ApiService;
import com.example.lovro.myapplication.network.GenericResponse;
import com.example.lovro.myapplication.network.InitApiService;

import java.io.IOException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ImageView profile_picture;
    private TextInputEditText email;
    private ProgressDialog progressDialog;
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
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawableResource(R.drawable.theme1) ;

        // set an exit transition
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            getWindow().setExitTransition(new Fade());
            getWindow().setEnterTransition(new Fade());
        }
        setContentView(R.layout.activity_register);

        profile_picture = findViewById(R.id.profilePicture);
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

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] options = {"From gallery","Random image"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Choose your profile image");
                builder.setCancelable(true);
                builder.setIcon(R.drawable.ic_image_black_24dp);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){

                            if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                    && ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){
                                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                            }else{
                                requestPermissionForImage();
                            }


                        }else{
                            Random rand = new Random();
                            int n = rand.nextInt(2)+1;
                            if(n == 1){
                                profile_picture.setImageResource(R.drawable.random_picture);
                            }else{
                                profile_picture.setImageResource(R.drawable.random_picture1);
                            }
                        }
                    }
                });
                builder.show();
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
        alertDialog.setTitle("Terms of Service");
        alertDialog.setIcon(R.drawable.ic_library_books_black_24dp);
        alertDialog.setView(view);

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void requestPermissionForImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try{
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                profile_picture.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(!(email.getText().toString().equals("")) || !(username.getText().toString().equals("")) || !(password.getText().toString().equals(""))){
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?\nYour entries won't be saved!")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            email.setText("");
                            username.setText("");
                            password.setText("");
                            email_layout.setHint("");
                            password_layout.setHint("");
                            username_layout.setHint("");
                            RegisterActivity.this.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }else {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    email_layout.setHint("");
                    password_layout.setHint("");
                    username_layout.setHint("");
            }
            super.onBackPressed();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setBackgroundDrawableResource(R.drawable.theme1);
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

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void show_loading(){
        progressDialog = ProgressDialog.show(this,"","Registration in process...",true,false);
    }

    private void stop_loading(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void showError(String message){
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("OK",null)
                .create()
                .show();
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


    private void register(User user){
        show_loading();
        callRegister = apiService.registerUser(user);
        callRegister.enqueue(new Callback<GenericResponse<User>>() {
            @Override
            public void onResponse(Call<GenericResponse<User>> call, Response<GenericResponse<User>> response) {
                stop_loading();
                if(response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Registration successful!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this,SignInActivity.class);
                    intent.putExtra("from","register");
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("pass",password.getText().toString());
                    startActivity(intent);
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
                    showError(t.getMessage());
                }

            }
        });
    }

}
