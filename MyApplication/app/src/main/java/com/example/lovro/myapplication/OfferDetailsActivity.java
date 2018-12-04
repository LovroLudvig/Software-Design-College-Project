package com.example.lovro.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class OfferDetailsActivity extends AppCompatActivity {
    Offer currentOffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean orderOffer(){
        return true;
    }

    private User getUserDetails(){
        return new User();
    }

    private boolean checkUserDetails(User user){
        return true;
    }

    private boolean sendReceipt(){
        return true;
    }


}
