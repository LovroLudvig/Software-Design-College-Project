package com.example.lovro.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.lovro.myapplication.domain.Offer;

import java.util.ArrayList;
import java.util.List;

public class OffersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private List<Offer> getOffers(){
        List<Offer> offers = new ArrayList<>();
        return offers;
    }

    private void suggestOffer(){}
}
