package com.example.ahmedmakaty.base.presentation.screens.MainScreen;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import retrofit2.HttpException;

public class MainViewModel extends ViewModel {


    public MainViewModel() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
