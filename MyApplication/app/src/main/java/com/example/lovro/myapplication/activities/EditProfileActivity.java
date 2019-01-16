package com.example.lovro.myapplication.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Town;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.network.ApiService;
import com.example.lovro.myapplication.network.InitApiService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BasicActivity {
    private ArrayAdapter<String> arrayAdapter;
    private Spinner cardPicker;
    private Button saveChangesButton;
    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText townEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private EditText cardEditText;
    private EditText poscalCodeEditText;
    private User currentUser;
    private ApiService apiService = InitApiService.apiService;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if(savedInstanceState != null){
            restartApp();
        }

        String userAsString = getIntent().getStringExtra("UserCurrent");
        Gson gson = new Gson();
        currentUser = gson.fromJson(userAsString,User.class);

        cardPicker=findViewById(R.id.card_picker);
        saveChangesButton=findViewById(R.id.saveChangesButton);
        arrayAdapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>() {{
            add("Mastercard");
            add("Visa");
            add("Maestro");
        }});
        cardPicker.setAdapter(arrayAdapter);
        nameEditText=findViewById(R.id.name_editText);
        usernameEditText=findViewById(R.id.username_editText);
        townEditText=findViewById(R.id.town_editText);
        addressEditText=findViewById(R.id.address_editText);
        emailEditText=findViewById(R.id.email_editText);
        cardEditText=findViewById(R.id.card_editText);
        poscalCodeEditText=findViewById(R.id.postalCode_editText);
        toolbar = findViewById(R.id.edit_profile_toolbar);
        initListeners();
        initEditTexts();
    }



    private void initEditTexts() {
        nameEditText.setText(currentUser.getName());
        usernameEditText.setText(currentUser.getUsername());
        if (!(currentUser.getTown()==null)){
            townEditText.setText(currentUser.getTown().getName());
            poscalCodeEditText.setText(String.valueOf(currentUser.getTown().getPostCode()));
        }
        addressEditText.setText(currentUser.getAddress());
        emailEditText.setText(currentUser.getEmail());

        if (!(currentUser.getCardNumber()==null)){
            cardEditText.setText(currentUser.getCardNumber().split(" ")[1]);
            if (currentUser.getCardNumber().split(" ")[0].equals("MasterCard")){
                cardPicker.setSelection(0);
            }
            if (currentUser.getCardNumber().split(" ")[0].equals("Visa")){
                cardPicker.setSelection(1);
            }
            if (currentUser.getCardNumber().split(" ")[0].equals("Maestro")){
                cardPicker.setSelection(2);
            }

        }
    }

    private boolean checkFields(){
        if (!nameEditText.getText().toString().equals("") && !usernameEditText.getText().toString().equals("")
                && !townEditText.getText().toString().equals("") && !addressEditText.getText().toString().equals("")
                && !emailEditText.getText().toString().equals("") && !cardEditText.getText().toString().equals("") && !poscalCodeEditText.getText().toString().equals("")){
            return true;
        }
        return false;

    }

    private void initListeners() {
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()){
                    String postalCode= poscalCodeEditText.getText().toString();
                    int finalValue=Integer.parseInt(postalCode);


                    currentUser.setName(nameEditText.getText().toString());
                    currentUser.setEmail(emailEditText.getText().toString());
                    currentUser.setUsername(usernameEditText.getText().toString());
                    currentUser.setCardNumber(cardPicker.getSelectedItem().toString()+" "+cardEditText.getText().toString());
                    currentUser.setTown(new Town(townEditText.getText().toString(),finalValue));
                    currentUser.setAddress(addressEditText.getText().toString());

                    updateUser(currentUser);
                }else{
                    Toast.makeText(EditProfileActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateUser(User user){
        show_loading("Updating user...");
        Call<ResponseBody> updateUser = apiService.updateUser(getUserAuth(), user);

        updateUser.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                stop_loading();
                if(response.isSuccessful()){
                    returnUser();
                }else{

                    if(call.isCanceled()){
                        //nothing
                    }else{
                        try {
                            showError(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                stop_loading();
                showError("Unexpected error occurred. Please try again!");
            }
        });
    }

    private void returnUser() {
        Gson gson = new Gson();
        String userAsString = gson.toJson(currentUser);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",userAsString);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
