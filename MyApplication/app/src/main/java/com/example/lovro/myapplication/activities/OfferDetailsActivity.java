package com.example.lovro.myapplication.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Dimension;
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
    private Spinner spinner;
    ArrayAdapter<String> adapter;
    private Button buy_now;
    private CheckBox[] checkBoxes;
    private double[] prices;


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
        prices = new double[currentOffer.getStyles().size()];
        buy_now = findViewById(R.id.buy_button);
        spinner = findViewById(R.id.spinner);


        //data setup
        initLayout();
        initListeners();
        initAddStyles();
        initSpinner();
    }

    private void initSpinner(){
        String[] dimensions;
        List<Dimension> dimensionList = currentOffer.getDimensions();
        dimensions = new String[dimensionList.size()];

        int i = 0;
        for( Dimension dimension : dimensionList){
            dimensions[i] = dimension.getDescription();
            i++;
        }

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,dimensions);
        spinner.setAdapter(adapter);
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
                LayoutInflater inflater= LayoutInflater.from(OfferDetailsActivity.this);
                View view=inflater.inflate(R.layout.item_description_layout, null);
                TextView textView = view.findViewById(R.id.description_item);
                textView.setText(currentOffer.getDescription());

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OfferDetailsActivity.this);
                alertDialog.setView(view);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });

        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater= LayoutInflater.from(OfferDetailsActivity.this);
                View view=inflater.inflate(R.layout.checkout_layout, null);
                TextView textView = view.findViewById(R.id.price_tag);
                textView.setText(getPrice());

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OfferDetailsActivity.this);
                alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
                alertDialog.setTitle("Continue to checkout?");
                alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO to transaction details
                    }
                });
                alertDialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setView(view);

                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });

    }

    private String getPrice(){
        Double price = currentOffer.getPrice();

        for(int i = 0; i < checkBoxes.length ; i++){
            if(checkBoxes[i].isChecked()){
                price += prices[i];
            }
        }

        return price.toString();
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
            prices[counter] = style.getPrice();
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
