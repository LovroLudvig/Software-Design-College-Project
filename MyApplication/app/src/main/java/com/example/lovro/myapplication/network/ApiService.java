package com.example.lovro.myapplication.network;

import com.example.lovro.myapplication.domain.Offer;
import com.example.lovro.myapplication.domain.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/auth/register")
    Call<GenericResponse<User>> registerUser(@Body User user);

    @POST("/auth/login")
    Call<ResponseBody> loginUser(@Header("Authorization") String auth,@Body User user);

    @GET("/advertisement/all")
    Call<List<Offer>> getAllOffers(@Header("Authorization") String auth);
}
