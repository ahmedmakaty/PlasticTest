package com.example.ahmedmakaty.base;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.support.v4.app.Fragment;

import com.example.ahmedmakaty.base.injection.AppComponent;
import com.example.ahmedmakaty.base.injection.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class App extends Application implements HasActivityInjector, HasSupportFragmentInjector, HasServiceInjector {

    @Inject
    public DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Inject
    public DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    public DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder().application(this).build().inject(this);
    }

    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceDispatchingAndroidInjector;
    }
}
