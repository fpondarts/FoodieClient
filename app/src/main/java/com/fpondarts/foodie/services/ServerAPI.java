package com.fpondarts.foodie.services;

import com.fpondarts.foodie.model.FoodieUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HEAD;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServerAPI {

    @HEAD("/user/email/{email}")
    Call<Void> checkEmailIsAvailable(@Path("email") String email);

    @POST("/user")
    Call<FoodieUser> signUpUser(@Body FoodieUser user);

}
