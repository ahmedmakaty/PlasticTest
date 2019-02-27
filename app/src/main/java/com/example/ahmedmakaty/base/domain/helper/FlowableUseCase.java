package com.example.ahmedmakaty.base.domain.helper;

import com.example.ahmedmakaty.base.domain.executer.PostExecutionThread;
import com.example.ahmedmakaty.base.domain.executer.ThreadExecutor;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public abstract class FlowableUseCase<T, Params> {

    public ThreadExecutor threadExecutor;
    public PostExecutionThread postExecutionThread;

    private CompositeDisposable disposables = new CompositeDisposable();

    public FlowableUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    /**
     * Builds a [Single] which will be used when the current [FlowableUseCase] is executed.
     */
    protected abstract Flowable<T> buildUseCaseObservable(Params params);

    /**
     * Executes the current use case.
     */
    public void execute(DisposableSubscriber<T> observer, Params params) {
        Flowable<T> observable = this.buildUseCaseObservable(params)
                .subscribeOn(Schedulers.from(this.threadExecutor))
                .observeOn(this.postExecutionThread.getScheduler());
        addDisposable(observable.subscribeWith(observer));
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    public void dispose() {
        if (!disposables.isDisposed()) disposables.dispose();
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    private void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }
}
