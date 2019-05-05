package com.ieeevit.evento.networkAPIs;

import com.ieeevit.evento.networkmodels.EventModel;
import com.ieeevit.evento.networkmodels.ScannableModel;
import com.ieeevit.evento.networkmodels.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FetchAPI {

    @GET("user/fetch/personal-info")
    Call<UserModel> userInfo(
            @Header("x-access-token") String token
    );

    @POST("event/fetch/info")
    @FormUrlEncoded
    Call<EventModel> eventInfo(
            @Field("event_id") String event_id
    );

    @POST("event/fetch/scannables")
    @FormUrlEncoded
    Call<ScannableModel> eventScannables(
            @Field("event_id") String event_id
    );
}
