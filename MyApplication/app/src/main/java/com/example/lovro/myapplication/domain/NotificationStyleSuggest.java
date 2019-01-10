package com.example.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class NotificationStyleSuggest extends Notification {

    private Order order;

    public NotificationStyleSuggest(Order order) {
        this.order=order;
    }

    public Order getOrder() {
        return order;
    }
}
