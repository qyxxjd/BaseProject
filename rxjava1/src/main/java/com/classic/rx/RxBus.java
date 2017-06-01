package com.classic.rx;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.rx
 *
 * 文件描述: RxBus
 * 创 建 人: 续写经典
 * 创建时间: 2016/11/03 17:26
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"}) public class RxBus {

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public void send(final Object event) {
        bus.onNext(event);
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
