package com.example.lovro.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lovro.myapplication.domain.Offer;
import com.example.lovro.myapplication.domain.User;

public class OfferDetailsActivity extends AppCompatActivity {
    Offer currentOffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean orderOffer(){
        return true;
    }

    private boolean checkUserDetails(User user){
        return true;
    }

    private boolean sendReceipt(){
        return true;
    }


}
