package com.example.ahmedmakaty.base.domain.executer;

import io.reactivex.Scheduler;

public interface PostExecutionThread {
    Scheduler getScheduler();

}
