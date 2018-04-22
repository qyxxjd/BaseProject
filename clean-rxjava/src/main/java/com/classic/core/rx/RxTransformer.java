package com.classic.core.rx;

import org.reactivestreams.Publisher;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 线程切换的封装
 *
 * @author classic
 * @version v1.0, 2018/4/22 下午4:48
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class RxTransformer {

    private RxTransformer() {
        // No instance.
    }

    public static class Observable {

        public static final ObservableTransformer THREAD = new ObservableTransformer() {
            @Override
            public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.newThread())
                               .unsubscribeOn(Schedulers.newThread())
                               .observeOn(Schedulers.newThread());
            }
        };

        public static final ObservableTransformer THREAD_ON_UI = new ObservableTransformer() {
            @Override public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.newThread())
                               .unsubscribeOn(Schedulers.newThread())
                               .observeOn(AndroidSchedulers.mainThread());
            }
        };

        public static final ObservableTransformer IO = new ObservableTransformer() {
            @Override public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                               .unsubscribeOn(Schedulers.io())
                               .observeOn(Schedulers.io());
            }
        };

        public static final ObservableTransformer IO_ON_UI = new ObservableTransformer() {
            @Override public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                               .unsubscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread());
            }
        };

        public static final ObservableTransformer COMPUTATION = new ObservableTransformer() {
            @Override public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.computation())
                               .unsubscribeOn(Schedulers.io())
                               .observeOn(Schedulers.io());
            }
        };

        public static final ObservableTransformer COMPUTATION_ON_UI = new ObservableTransformer() {
            @Override public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.computation())
                               .unsubscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static class Flowable {

        public static final FlowableTransformer THREAD = new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.newThread())
                               .unsubscribeOn(Schedulers.newThread())
                               .observeOn(Schedulers.newThread());
            }
        };

        public static final FlowableTransformer THREAD_ON_UI = new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.newThread())
                               .unsubscribeOn(Schedulers.newThread())
                               .observeOn(AndroidSchedulers.mainThread());
            }
        };

        public static final FlowableTransformer IO = new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                               .unsubscribeOn(Schedulers.io())
                               .observeOn(Schedulers.io());
            }
        };

        public static final FlowableTransformer IO_ON_UI = new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                               .unsubscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread());
            }
        };

        public static final FlowableTransformer COMPUTATION = new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.computation())
                               .unsubscribeOn(Schedulers.io())
                               .observeOn(Schedulers.io());
            }
        };

        public static final FlowableTransformer COMPUTATION_ON_UI = new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.computation())
                               .unsubscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 应用一种线程切换策略
     */
    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, T> applySchedulers(ObservableTransformer transformer) {
        return (ObservableTransformer<T, T>)transformer;
    }

    /**
     * 应用一种线程切换策略
     */
    @SuppressWarnings("unchecked")
    public static <T> FlowableTransformer<T, T> applySchedulers(FlowableTransformer transformer) {
        return (FlowableTransformer<T, T>)transformer;
    }
}
