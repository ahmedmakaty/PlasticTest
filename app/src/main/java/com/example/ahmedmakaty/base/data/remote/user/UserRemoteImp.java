package com.example.ahmedmakaty.base.data.remote.user;

import com.example.ahmedmakaty.base.data.remote.ApiServiceInterface;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class UserRemoteImp implements UserRemote {

    private ApiServiceInterface apiServiceInterface;

    @Inject
    public UserRemoteImp(ApiServiceInterface apiServiceInterface) {
        this.apiServiceInterface = apiServiceInterface;
    }

    @Override
    public Flowable<String> generateToken(String deviceId) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("DeviceId", deviceId);

        return apiServiceInterface.generateToken(body).map(response -> {
            return response;
        });
    }
}
