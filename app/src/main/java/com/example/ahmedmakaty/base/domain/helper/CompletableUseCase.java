package com.example.ahmedmakaty.base.domain.helper;

import com.example.ahmedmakaty.base.domain.executer.PostExecutionThread;
import com.example.ahmedmakaty.base.domain.executer.ThreadExecutor;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class CompletableUseCase<Params> {

    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;

    private CompositeDisposable disposables = new CompositeDisposable();

    public CompletableUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    /**
     * Builds a [Completable] which will be used when the current [CompletableUseCase] is executed.
     */
    protected abstract Completable buildUseCaseObservable(Params params);

    /**
     * Executes the current use case.
     */
    public void execute(DisposableCompletableObserver disposableCompletableObserver, Params params) {
        Completable observable = this.buildUseCaseObservable(params)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler());
        disposables.add(observable.subscribeWith(disposableCompletableObserver));
    }

    /**
     * Unsubscribes from current [Disposable].
     */
    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
