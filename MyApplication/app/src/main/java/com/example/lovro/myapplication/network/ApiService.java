package com.example.lovro.myapplication.network;

import com.example.lovro.myapplication.domain.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/auth/register")
    Call<GenericResponse<User>> registerUser(@Body User user);

    @POST("/auth/login")
    Call<GenericResponse<String>> loginUser(@Header("Authorization") String auth);

}
