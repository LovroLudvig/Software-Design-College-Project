package com.example.lovro.myapplication.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.lovro.myapplication.domain.Offer;

public class SuggestOfferActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean postOffer(Offer offer){
        return true;
    }

    private boolean checkFields(){
        return true;
    }
}
