package com.example.ahmedmakaty.base.injection.module;


import com.example.ahmedmakaty.base.injection.module.screens.MainScreenModule;
import com.example.ahmedmakaty.base.injection.scopes.PerActivity;
import com.example.ahmedmakaty.base.presentation.screens.MainScreen.MainFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {

    @PerActivity
    @ContributesAndroidInjector(modules = {MainScreenModule.class})
    abstract MainFragment bindPaymentMethodsScreen();
}
