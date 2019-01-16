package com.craftery.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class OrderStatus {

    @Json(name = "id")
    private Long id;

    @Json(name = "name")
    private String name;

    public OrderStatus(Long id,String name){
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
