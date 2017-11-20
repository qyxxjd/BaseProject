package com.classic.android.rx.observer;

import com.classic.android.rx.RxUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.rx.observer
 *
 * 文件描述: 自动销毁的Observer
 * 创 建 人: 续写经典
 * 创建时间: 2016/12/8 17:03
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
