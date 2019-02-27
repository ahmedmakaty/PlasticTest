package com.example.ahmedmakaty.base.data.remote;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServiceInterface {


    @POST("/api/Payfort/GenerateMobileToken")
    Flowable<String> generateToken(@Body HashMap<String, Object> body);

    @GET("/api/PaymentMethod/GetPaymentMethods")
    Flowable<String> getPaymentMethods(@HeaderMap Map<String, String> headers, @Query("countryid") String countryId, @Query("languageId") String languageId);
}
