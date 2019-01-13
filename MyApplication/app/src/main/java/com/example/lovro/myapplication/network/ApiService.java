package com.example.lovro.myapplication.network;

import com.example.lovro.myapplication.domain.Comment;
import com.example.lovro.myapplication.domain.Offer;
import com.example.lovro.myapplication.domain.Order;
import com.example.lovro.myapplication.domain.Story;
import com.example.lovro.myapplication.domain.Transaction;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.domain.UserProfile;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

import retrofit2.http.Query;


public interface ApiService {

    @POST("/auth/register")
    Call<GenericResponse<User>> registerUser(@Body User user);

    @POST("/auth/login")
    Call<User> loginUser(@Header("Authorization") String auth,@Body User user);

    @GET("/advertisement/all")
    Call<List<Offer>> getAllOffers();

    @GET("/transactions/all")
    Call<List<Transaction>> getAllTransactions(@Header("Authorization") String auth);

    @POST("/orders/add/{advertisementId}")
    Call<Order> orderOffer(@Path("advertisementId") String offerId, @Query("styleId") String styleId, @Query("dimensionId") String dimensionId, @Body User user);

    @GET("/user/{username}")
    Call<UserProfile> getUserByUsername(@Header("Authorization") String auth, @Path("username") String username);

    @POST("/user/update")
    Call<ResponseBody> updateUser(@Header("Authorization") String auth, @Body User user);

    @GET("/user/{username}")
    Call<User> getUserByUsername2(@Header("Authorization") String auth, @Path("username") String username);

    @GET("/stories/all")
    Call<List<Story>> getAllStories();

    @GET("/stories/evaluation")
    Call<List<Story>> getStoriesInEvaluation(@Header("Authorization") String auth);

    @POST("/orders/orderDecoration/{advertisementId}")
    Call<Order> order_style(@Header("Authorization") String auth,@Path("advertisementId") String offerId, @Query("styleId") String styleId,@Query("styleName") String styleName,@Body User user);

    @GET("/orders")
    Call<List<Order>> getAllOrders(@Header("Authorization") String auth);

    @POST("/orders/manage")
    Call<ResponseBody> manageOrder(@Header("Authorization") String auth,@Query("orderId") String orderId,@Query("isAllowed") String isAllowed,@Query("price") String price);

    @POST("/stories/manage")
    Call<ResponseBody> manageStory(@Header("Authorization") String auth,@Query("storyId") String storyId,@Query("isAllowed") String isAllowed);

    @POST("/stories/setseen/{storyId}")
    Call<ResponseBody> setStorySeen(@Header("Authorization") String auth,@Path("storyId") String storyId);

    @POST("/user/forbid/{username}")
    Call<ResponseBody> forbidUser(@Header("Authorization") String auth,@Path("username") String username);

    @POST("/advertisement/publish")
    Call<Offer> postOffer(@Header("Authorization") String auth, @Body Offer offer);

    @POST("/media/advertisement/image/upload/{advertisementId}")
    @Multipart
    Call<ResponseBody> uploadAdvertisementImage(@Header("Authorization") String auth,@Path("advertisementId") String advertisementId, @Part("file\"; filename=\"image.jpg\"") RequestBody request);

    @POST("/stories/recommend")
    Call<Story> suggestStory(@Header("Authorization") String auth, @Body Story story);

    @POST("/media/story/image/upload/{storyId}")
    @Multipart
    Call<ResponseBody> uploadStoryImage(@Header("Authorization") String auth,@Path("storyId") String storyId, @Part("file\"; filename=\"image.jpg\"") RequestBody request);

    @POST("/comments/post/{storyId}")
    Call<List<Comment>> postCommentUser(@Path("storyId") String storyId,@Query("username") String username,@Body Comment comment);

    @POST("/comments/post/{storyId}")
    Call<List<Comment>> postCommentGuest(@Path("storyId") String storyId,@Body Comment comment);

}
