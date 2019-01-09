package com.example.lovro.myapplication.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Dimension;
import com.example.lovro.myapplication.domain.Offer;
import com.example.lovro.myapplication.domain.Order;
import com.example.lovro.myapplication.domain.Style;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.network.ApiService;
import com.example.lovro.myapplication.network.InitApiService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferDetailsActivity extends BasicActivity {
    private Offer currentOffer;
    private Button item_desc;
    private TextView offer_name;
    private TextView offer_desc;
    private TextView offer_spec;
    private TextView offer_price;
    private ImageView offer_pic;
    private Button new_style;
    private Spinner spinner;
    private Spinner spinner2;
    ArrayAdapter<String> adapter_dimension;
    ArrayAdapter<String> adapter_style;
    private Button buy_now;
    private CheckBox[] checkBoxes;
    private double[] prices;
    private String user;
    private Call<User> callLogin;
    private Call<Order> buy_offer;
    private ApiService apiService = InitApiService.apiService;


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
        offer_pic = findViewById(R.id.offer_image);
        checkBoxes = new CheckBox[currentOffer.getStyles().size()];
        prices = new double[currentOffer.getStyles().size()];
        buy_now = findViewById(R.id.buy_button);
        spinner = findViewById(R.id.spinner);
        new_style = findViewById(R.id.ask_for_own_style);
        spinner2 = findViewById(R.id.spinner2);


        //data setup
        initLayout();
        initListeners();
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

        adapter_dimension = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,dimensions);
        spinner.setAdapter(adapter_dimension);


        String[] styles;
        List<Style> styleList = currentOffer.getStyles();
        styles = new String[styleList.size()];

        i = 0;
        for(Style style : styleList){
            styles[i] = style.getDescription()+"   "+"+"+style.getPrice();
            i++;
        }

        adapter_style = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,styles);
        spinner2.setAdapter(adapter_style);
    }

    private void initLayout(){
        Picasso.get().load(currentOffer.getImageUrl()).placeholder(R.drawable.placeholder).into(offer_pic);
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
                        if(isInternetAvailable()){
                            if(isUserLoggedIn()){
                                getUserFromLogin();
                            }else{
                                //TODO add new screen for adding info
                            }
                        }
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

        new_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO dodati novi activity za narucivanje stila
            }
        });

    }

    private String getPrice(){
        Double price = currentOffer.getPrice();

        Style style = currentOffer.getStyles().get(spinner2.getSelectedItemPosition());
        price = price+style.getPrice();

        return price.toString();
    }

    private void getUserFromLogin(){
        callLogin = apiService.loginUser(getUserAuth(),new User(getSharedPreferences("UserData", MODE_PRIVATE).getString("username", "")));
        callLogin.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if((response.body().getCardNumber() != null)){
                        order_item(response.body());
                    }else{
                        //TODO add fill your information
                        Toast.makeText(OfferDetailsActivity.this, "Please fill in your details in profile tab", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    try {
                        showError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showError(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    protected void showError(String message){
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("OK",null)
                .create()
                .show();
    }


    protected boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean isUserLoggedIn(){
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        if(prefs.getBoolean("saved",false)){
            return true;
        }
        return false;
    }

    private String getStyleId(){
        Style style = currentOffer.getStyles().get(spinner2.getSelectedItemPosition());
        return style.getId().toString();
    }

    private String getDimensionId(){
        Dimension dimension = currentOffer.getDimensions().get(spinner.getSelectedItemPosition());
        return dimension.getId().toString();
    }

    private void order_item(final User user){
        show_loading("Finishing order...");
        buy_offer = apiService.orderOffer(currentOffer.getId().toString(),getStyleId(),getDimensionId(),user);
        buy_offer.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.isSuccessful()){
                    stop_loading();
                    Toast.makeText(getApplicationContext(),"The purchase has been successful!",Toast.LENGTH_LONG).show();
                    //TODO add screen about purchase info
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
