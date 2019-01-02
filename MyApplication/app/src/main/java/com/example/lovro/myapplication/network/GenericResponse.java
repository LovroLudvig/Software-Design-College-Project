package com.example.lovro.myapplication.network;

import com.squareup.moshi.Json;

public class GenericResponse<T> {

    @Json(name = "data")
    private T responseData;

    public T getResponseData(){
        return responseData;
    }
}
