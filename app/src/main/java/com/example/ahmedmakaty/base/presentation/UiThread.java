package com.example.ahmedmakaty.base.presentation;

import com.example.ahmedmakaty.base.domain.executer.PostExecutionThread;

import java.nio.file.attribute.PosixFileAttributes;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class UiThread implements PostExecutionThread {

    @Inject
    public UiThread() {
    }

    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}


