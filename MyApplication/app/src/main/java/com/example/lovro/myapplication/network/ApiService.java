package com.example.lovro.myapplication.network;

import com.example.lovro.myapplication.domain.Offer;
import com.example.lovro.myapplication.domain.Order;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.domain.UserProfile;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
<<<<<<< Updated upstream
import retrofit2.http.Query;
=======
>>>>>>> Stashed changes

public interface ApiService {

    @POST("/auth/register")
    Call<GenericResponse<User>> registerUser(@Body User user);

    @POST("/auth/login")
    Call<User> loginUser(@Header("Authorization") String auth,@Body User user);

    @GET("/advertisement/all")
    Call<List<Offer>> getAllOffers(@Header("Authorization") String auth);

<<<<<<< Updated upstream
    @POST("/orders/add/{advertisementId}")
    Call<Order> orderOffer(@Path("advertisementId") String offerId, @Query("styleId") String styleId, @Query("dimensionId") String dimensionId, @Body User user);
=======
    @GET("/user/{username}")
    Call<UserProfile> getUserByUsername(@Header("Authorization") String auth, @Path("username") String username);

    @POST("/user/update")
    Call<ResponseBody> updateUser(@Header("Authorization") String auth, @Body User user);
>>>>>>> Stashed changes
}
