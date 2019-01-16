package com.craftery.lovro.myapplication.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.domain.Order;
import com.craftery.lovro.myapplication.domain.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class PurchaseActivity extends BasicActivity {

    private TextView hash;
    private TextView date;
    private TextView address;
    private TextView town;
    private TextView postal;
    private TextView article_name;
    private TextView article_style;
    private TextView article_dimension;
    private ImageView article_image;
    private ImageView back;
    private TextView price;
    private TextView credit_type;
    private User user;
    private Order order;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        if(savedInstanceState != null){
            restartApp();
        }

        String userAsString = getIntent().getStringExtra("user");
        String orderAsString = getIntent().getStringExtra("order");

        Gson gson = new Gson();
        user = gson.fromJson(userAsString,User.class);
        order = gson.fromJson(orderAsString,Order.class);

        hash = findViewById(R.id.hash_order);
        date = findViewById(R.id.date);
        address = findViewById(R.id.order_adress);
        town = findViewById(R.id.order_town);
        postal = findViewById(R.id.order_postal);
        article_name = findViewById(R.id.order_article_name);
        article_style = findViewById(R.id.order_article_style);
        article_dimension = findViewById(R.id.order_article_dimension);
        back = findViewById(R.id.order_back_arrow);
        article_image = findViewById(R.id.article_image);
        price = findViewById(R.id.priceeeee);
        credit_type = findViewById(R.id.credit_type);

        initLayout();
        initListener();
    }



    private void initLayout(){
        String[] credit = order.getCardNumber().split(" ");

        String token = order.getId().toString();
        String encoded_token = Base64.encodeToString(token.getBytes(),0);
        String encode = Base64.encodeToString(encoded_token.getBytes(),0);
        hash.setText(encode);
        String time = Calendar.getInstance().getTime().toString();
        date.setText(time);
        address.setText(order.getAddress());
        town.setText(order.getTown().getName());
        postal.setText(String.valueOf(user.getTown().getPostCode()));
        article_name.setText(order.getOffer().getName());
        article_style.setText(order.getStyle().getDescription());
        article_dimension.setText(order.getDimension().getDescription());
        price.setText(order.getPrice().toString());
        credit_type.setText(credit[0]);
        Picasso.get().load(order.getOffer().getImageUrl()).into(article_image);
    }

    private void initListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
