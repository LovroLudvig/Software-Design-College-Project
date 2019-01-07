package com.example.lovro.myapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.lovro.myapplication.R;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {
    private ArrayAdapter<String> arrayAdapter;
    private Spinner cardPicker;
    private Button saveChangesButton;
    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText townEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private EditText cardEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        cardPicker=findViewById(R.id.card_picker);
        saveChangesButton=findViewById(R.id.saveChangesButton);
        arrayAdapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>() {{
            add("Mastercard");
            add("Visa");
            add("Maestro");
        }});
        cardPicker.setAdapter(arrayAdapter);
        initListeners();
    }

    private void initListeners() {
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
