package com.example.lovro.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Offer;
import com.example.lovro.myapplication.domain.Style;
import com.example.lovro.myapplication.domain.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfferDetailsActivity extends AppCompatActivity {
    private Offer currentOffer;
    private Button item_desc;
    private TextView offer_name;
    private TextView offer_desc;
    private TextView offer_spec;
    private TextView offer_price;
    private ImageView offer_pic;
    private LinearLayout linearLayout;
    private CheckBox[] checkBoxes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offerdetails);

        String offerAsString = getIntent().getStringExtra("Offer");
        Gson gson = new Gson();
        currentOffer = gson.fromJson(offerAsString,Offer.class);


        item_desc = findViewById(R.id.item_desc);
        offer_desc = findViewById(R.id.offer_description);
        offer_name = findViewById(R.id.offer_name);
        offer_spec = findViewById(R.id.offer_specification);
        offer_price = findViewById(R.id.offer_price);
        offer_pic = findViewById(R.id.offer_picture);
        linearLayout = findViewById(R.id.style_layout);
        checkBoxes = new CheckBox[currentOffer.getStyles().size()];

        //data setup
        initLayout();
        initListeners();
        initAddStyles();
    }

    private void initLayout(){
        //Picasso.get().load(currentOffer.getImageUrl()).into(offer_pic);
        offer_spec.setText(currentOffer.getSpecification());
        offer_name.setText(currentOffer.getName());
        offer_desc.setText(currentOffer.getDescription());
        offer_price.setText(currentOffer.getPrice().toString());
    }

    private void initListeners(){
        item_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Add alert dialog with item description
            }
        });
    }

    private void initAddStyles(){
        List<Style> styles = currentOffer.getStyles();

        int counter = 0;
        for(Style style : styles){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(style.getDescription()+"            "+"+"+style.getPrice().toString()+" "+"HRK");
            checkBox.setId(style.getId().intValue());
            linearLayout.addView(checkBox);
            checkBoxes[counter] = checkBox;
            counter++;
        }
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
