package com.example.lovro.myapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Offer;
import com.example.lovro.myapplication.domain.Role;
import com.example.lovro.myapplication.domain.Status;
import com.example.lovro.myapplication.domain.Town;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.domain.UserProfile;
import com.google.gson.Gson;

import java.util.ArrayList;

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
    private UserProfile currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        String userAsString = getIntent().getStringExtra("UserCurrent");
        Gson gson = new Gson();
        currentUser = gson.fromJson(userAsString,UserProfile.class);

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
        initListeners();
        initEditTexts();
    }

    private void initEditTexts() {
        nameEditText.setText(currentUser.getName());
        usernameEditText.setText(currentUser.getUsername());
        if (!(currentUser.getTown()==null)){
            townEditText.setText(currentUser.getTown().getName());
            poscalCodeEditText.setText(currentUser.getTown().getPostCode());
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
                    User user=new User(emailEditText.getText().toString(), usernameEditText.getText().toString(),nameEditText.getText().toString(), cardPicker.getSelectedItem().toString()+" "+cardEditText.getText().toString(),addressEditText.getText().toString(),new Town(townEditText.getText().toString(),Integer.parseInt(poscalCodeEditText.toString())));
                    finish();
                }else{
                    Toast.makeText(EditProfileActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
