package com.example.ahmedmakaty.base.data.remote;

import com.example.ahmedmakaty.base.data.model.DateResponse;

import java.util.HashMap;
import java.util.Map;


import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServiceInterface {


    @POST("/api/Payfort/GenerateMobileToken")
    Flowable<String> generateToken(@Body HashMap<String, Object> body);

    @GET("https://dateandtimeasjson.appspot.com/")
    Observable<DateResponse> getDateTime();
}
