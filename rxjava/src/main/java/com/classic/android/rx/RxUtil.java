package com.classic.android.rx;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.rx
 *
 * 文件描述: RxJava工具类
 * 创 建 人: 续写经典
 * 创建时间: 2016/12/8 17:03
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class RxUtil {

    private RxUtil() {
        // No instance.
    }

    /**
     * 运行一个定时任务在子线程
     *
     * @param delay  延迟时间，单位：毫秒
     * @param onNext 定时任务回调
     * @return Disposable
     */
    public static Disposable time(long delay, @NonNull Consumer<Long> onNext) {
        return time(delay, TimeUnit.MILLISECONDS, onNext);
    }

    /**
     * 运行一个定时任务在子线程
     *
     * @param delay  延迟时间
     * @param unit   单位
     * @param onNext 定时任务回调
     * @return Disposable
     */
    public static Disposable time(long delay, TimeUnit unit, @NonNull Consumer<Long> onNext) {
        return Observable.timer(delay, unit)
                         .compose(RxTransformer.<Long>applySchedulers(RxTransformer.Observable.COMPUTATION))
                         .subscribe(onNext);
    }

    /**
     * 运行一个定时任务在UI线程
     *
     * @param delay  延迟时间
     * @param unit   单位
     * @param onNext 定时任务回调
     * @return Disposable
     */
    public static Disposable timeOnUI(long delay, TimeUnit unit, @NonNull Consumer<Long> onNext) {
        return Observable.timer(delay, unit)
                         .compose(RxTransformer.<Long>applySchedulers(RxTransformer.Observable.COMPUTATION_ON_UI))
                         .subscribe(onNext);
    }

    /**
     * 运行一个轮询任务在子线程
     *
     * @param interval 轮询间隔，单位：毫秒
     * @param onNext 轮询任务回调
     * @return Disposable
     */
    public static Disposable interval(long interval, @NonNull Consumer<Long> onNext) {
        return interval(interval, TimeUnit.MILLISECONDS, onNext);
    }

    /**
     * 运行一个轮询任务在子线程
     *
     * @param interval 轮询间隔
     * @param unit     单位
     * @param onNext 轮询任务回调
     * @return Disposable
     */
    public static Disposable interval(long interval, TimeUnit unit, @NonNull Consumer<Long> onNext) {
        return Observable.interval(interval, unit)
                         .compose(RxTransformer.<Long>applySchedulers(RxTransformer.Observable.COMPUTATION))
                         .subscribe(onNext);
    }

    /**
     * 运行一个任务在子线程
     *
     * @param backgroundAction 耗时任务
     * @return Disposable
     */
    public static Disposable run(@NonNull Action backgroundAction) {
        return Observable.<Integer>empty()
                .compose(RxTransformer.<Integer>applySchedulers(RxTransformer.Observable.IO))
                .subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER, backgroundAction);
    }

    /**
     * 运行一个任务在UI线程
     *
     * @param backgroundSubscribe 耗时任务
     * @param uiAction UI回调
     * @return Disposable
     */
    public static <T> Disposable runOnUI(@NonNull ObservableOnSubscribe<T> backgroundSubscribe,
                                         @NonNull Consumer<T> uiAction) {
        return Observable.create(backgroundSubscribe)
                         .compose(RxTransformer.<T>applySchedulers(RxTransformer.Observable.IO_ON_UI))
                         .subscribe(uiAction);
    }

    /**
     * 清理Disposable，释放资源
     *
     * @param disposables Disposable array
     */
    public static void clear(Disposable... disposables) {
        if (null == disposables) {
            return;
        }
        for (Disposable disposable : disposables) {
            if (null != disposable) {
                disposable.dispose();
            }
        }
    }
}
