package com.craftery.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class Dimension {

    @Json(name = "id")
    private Long id;

    @Json(name = "description")
    private String description;

    public Dimension(Long id,String description){
        this.description = description;
        this.id = id;
    }

    public Dimension(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
