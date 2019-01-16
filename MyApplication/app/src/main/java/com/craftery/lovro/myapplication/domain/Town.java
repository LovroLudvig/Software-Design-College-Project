package com.craftery.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class Town {

    @Json(name = "name")
    private String name;

    @Json(name = "postCode")
    private int postCode;

    public Town(String name,int postCode){
        this.name = name;
        this.postCode = postCode;
    }

    public int getPostCode() {
        return postCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }
}
