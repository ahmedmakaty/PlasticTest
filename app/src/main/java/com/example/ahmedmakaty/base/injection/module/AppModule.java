package com.example.ahmedmakaty.base.injection.module;

import android.app.Application;
import android.content.Context;

import com.example.ahmedmakaty.base.data.Executor.JobExecutor;
import com.example.ahmedmakaty.base.data.remote.ApiServiceInterface;
import com.example.ahmedmakaty.base.domain.executer.PostExecutionThread;
import com.example.ahmedmakaty.base.domain.executer.ThreadExecutor;
import com.example.ahmedmakaty.base.presentation.UiThread;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class AppModule {

    @Provides
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    ApiServiceInterface provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiServiceInterface.class);
    }

    @Provides
    JobExecutor providesJobExecutor() {
        return JobExecutor.getInstance();
    }

    @Provides
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    PostExecutionThread providePostExecutionThread(UiThread uiThread) {
        return uiThread;
    }
}
