package com.craftery.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class Role {

    @Json(name = "id")
    private Long id;

    @Json(name = "name")
    private String name;

    public Role(Long id,String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
