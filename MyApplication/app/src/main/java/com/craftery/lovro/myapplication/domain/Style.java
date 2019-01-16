package com.craftery.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class Style {

    @Json(name = "id")
    private Long id;

    @Json(name = "description")
    private String description;

    @Json(name = "price")
    private Double price;

    public Style(Long id,String description,Double price){
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public Style(String description, Double price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
