package com.fpondarts.foodie.services;

import retrofit2.Call;
import retrofit2.http.HEAD;
import retrofit2.http.Path;

public interface CheckEmailService {

    @HEAD("/user/email/{email}")
    Call<Void> checkEmailIsAvailable(@Path("email") String email);

}
