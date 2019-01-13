package com.example.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class Transaction {

    @Json(name = "id")
    private String id;

    public String getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Transaction(String id, Order order) {

        this.id = id;
        this.order = order;
    }

    @Json(name = "adOrder")

    private Order order;

}
