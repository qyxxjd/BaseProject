package com.classic.core.rx.observer;

import com.classic.core.rx.RxUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 自动销毁的Observer
 *
 * @author classic
 * @version v1.0, 2018/4/22 下午4:48
 */
@SuppressWarnings("unused")
public class AutoObserver<T> implements Observer<T> {
    private Disposable mDisposable;

    @Override
    public void onSubscribe(@NonNull Disposable disposable) {
        mDisposable = disposable;
    }

    @Override
    public void onNext(@NonNull T t) { }

    @Override
    public void onError(@NonNull Throwable throwable) {
        RxUtil.clear(mDisposable);
    }

    @Override
    public void onComplete() {
        RxUtil.clear(mDisposable);
    }
}
