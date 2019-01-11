package com.example.lovro.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Offer;
import com.example.lovro.myapplication.domain.Order;
import com.example.lovro.myapplication.domain.Town;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.network.ApiService;
import com.example.lovro.myapplication.network.InitApiService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FillYourInfoActivity extends BasicActivity {

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
    private ApiService apiService = InitApiService.apiService;
    private Call<Order> buy_offer;
    private User currentUser;
    private Offer currentOffer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toast.makeText(this,"Please fill your info to continue.",Toast.LENGTH_LONG).show();

        String offerAsString = getIntent().getStringExtra("Offer");
        Gson gson = new Gson();
        currentOffer = gson.fromJson(offerAsString,Offer.class);

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
        saveChangesButton.setText("Finish order");

        initListeners();
    }

    private boolean checkFields(){
        if (!nameEditText.getText().toString().equals("") && !usernameEditText.getText().toString().equals("")
                && !townEditText.getText().toString().equals("") && !addressEditText.getText().toString().equals("")
                && !emailEditText.getText().toString().equals("") && !cardEditText.getText().toString().equals("") && !poscalCodeEditText.getText().toString().equals("")){
            return true;
        }
        return false;

    }

    private void initListeners(){
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    currentUser = new User(emailEditText.getText().toString(),"pass",usernameEditText.getText().toString(),nameEditText.getText().toString(),cardPicker.getSelectedItem().toString()+" "+cardEditText.getText().toString(),addressEditText.getText().toString(),null,null,new Town(townEditText.getText().toString(),Integer.parseInt(poscalCodeEditText.getText().toString())));
                    order_item(currentUser);
                }else{
                    Toast.makeText(FillYourInfoActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void order_item(final User user){
        show_loading("Finishing order...");
        buy_offer = apiService.orderOffer(currentOffer.getId().toString(),getIntent().getStringExtra("Style"),getIntent().getStringExtra("Dimension"),user);
        buy_offer.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.isSuccessful()){
                    stop_loading();
                    Toast.makeText(getApplicationContext(),"The purchase has been successful!",Toast.LENGTH_LONG).show();

                    Gson gson = new Gson();
                    String userAsString = gson.toJson(user);
                    String orderAsString = gson.toJson(response.body());

                    Intent intent = new Intent(FillYourInfoActivity.this,PurchaseActivity.class);
                    intent.putExtra("user",userAsString);
                    intent.putExtra("order",orderAsString);
                    startActivity(intent);
                    finish();
                }else{
                    stop_loading();
                    try {
                        showError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                stop_loading();
                showError(t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
