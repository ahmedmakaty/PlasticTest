package com.example.ahmedmakaty.base.injection;

import android.app.Application;

import com.example.ahmedmakaty.base.App;
import com.example.ahmedmakaty.base.injection.module.AppModule;
import com.example.ahmedmakaty.base.injection.module.FragmentBindingModule;
import com.example.ahmedmakaty.base.injection.module.NetworkModule;
import com.example.ahmedmakaty.base.injection.module.data.UserDataModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {NetworkModule.class,
        UserDataModule.class,
        AndroidSupportInjectionModule.class,
        AppModule.class,
        FragmentBindingModule.class})
public interface AppComponent {

    void inject(App app);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
