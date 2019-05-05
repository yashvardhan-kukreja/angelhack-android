package com.ieeevit.evento.networkAPIs;

import com.ieeevit.evento.networkmodels.GeneralResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegistrationAPI {

    @POST("/authenticate/user/register")
    @FormUrlEncoded
    Call<GeneralResponse> userRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("contact") String contact
    );

}
