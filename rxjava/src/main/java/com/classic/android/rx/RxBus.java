package com.classic.android.rx;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.rx
 *
 * 文件描述: RxBus
 * 创 建 人: 续写经典
 * 创建时间: 2016/11/03 17:26
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"}) public class RxBus {

    private final Subject<Object> mBus = PublishSubject.create().toSerialized();

    public void send(Object event) {
        mBus.onNext(event);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    @SuppressWarnings("WeakerAccess") public Flowable<Object> toFlowable(BackpressureStrategy strategy) {
        return mBus.toFlowable(strategy);
    }

    public Flowable<Object> toFlowable() {
        return toFlowable(BackpressureStrategy.LATEST);
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }
}
