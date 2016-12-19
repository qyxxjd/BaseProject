package com.classic.android.rx;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.rx
 *
 * 文件描述: RxJava工具类
 * 创 建 人: 续写经典
 * 创建时间: 2016/11/03 17:26
 */
@SuppressWarnings({ "WeakerAccess", "unused" })
public final class RxUtil {

    private RxUtil() { }

    public static final ObservableTransformer THREAD_TRANSFORMER = new ObservableTransformer() {
        @Override public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.newThread())
                           .unsubscribeOn(Schedulers.newThread())
                           .observeOn(Schedulers.newThread());
        }
    };

    public static final ObservableTransformer THREAD_ON_UI_TRANSFORMER =
            new ObservableTransformer() {
        @Override public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.newThread())
                           .unsubscribeOn(Schedulers.newThread())
                           .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static final ObservableTransformer IO_TRANSFORMER = new ObservableTransformer() {
        @Override public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                           .unsubscribeOn(Schedulers.io())
                           .observeOn(Schedulers.io());
        }
    };

    public static final ObservableTransformer IO_ON_UI_TRANSFORMER = new ObservableTransformer() {
        @Override public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                           .unsubscribeOn(Schedulers.io())
                           .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static final ObservableTransformer COMPUTATION_TRANSFORMER =
            new ObservableTransformer() {
        @Override public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.computation())
                           .unsubscribeOn(Schedulers.newThread())
                           .observeOn(Schedulers.newThread());
        }
    };

    public static final ObservableTransformer COMPUTATION_ON_UI_TRANSFORMER =
            new ObservableTransformer() {
        @Override public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.computation())
                           .unsubscribeOn(Schedulers.newThread())
                           .observeOn(AndroidSchedulers.mainThread());
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, T> applySchedulers(ObservableTransformer transformer) {
        return (ObservableTransformer<T, T>) transformer;
    }

    /**
     * 运行一个定时任务在子线程
     *
     * @param delay 延迟时间，单位：毫秒
     * @param onNext
     * @return
     */
    public static Disposable time(long delay, @NonNull Consumer<Long> onNext) {
        return time(delay, TimeUnit.MILLISECONDS, onNext);
    }

    /**
     * 运行一个定时任务在子线程
     *
     * @param delay 延迟时间
     * @param unit 单位
     * @param onNext
     * @return
     */
    public static Disposable time(long delay, TimeUnit unit, @NonNull Consumer<Long> onNext) {
        return Observable.timer(delay, unit)
                         .compose(RxUtil.<Long>applySchedulers(COMPUTATION_TRANSFORMER))
                         .subscribe(onNext);
    }

    /**
     * 运行一个定时任务在UI线程
     *
     * @param delay 延迟时间
     * @param unit 单位
     * @param onNext
     * @return
     */
    public static Disposable timeOnUI(
            long delay, TimeUnit unit, @NonNull Consumer<Long> onNext) {
        return Observable.timer(delay, unit)
                         .compose(RxUtil.<Long>applySchedulers(COMPUTATION_ON_UI_TRANSFORMER))
                         .subscribe(onNext);
    }

    /**
     * 运行一个轮询任务在子线程
     *
     * @param interval 轮询间隔，单位：毫秒
     * @param onNext
     * @return
     */
    public static Disposable interval(long interval, @NonNull Consumer<Long> onNext) {
        return interval(interval, TimeUnit.MILLISECONDS, onNext);
    }

    /**
     * 运行一个轮询任务在子线程
     *
     * @param interval 轮询间隔
     * @param unit 单位
     * @param onNext
     * @return
     */
    public static Disposable interval(
            long interval, TimeUnit unit, @NonNull Consumer<Long> onNext) {
        return Observable.interval(interval, unit)
                         .compose(RxUtil.<Long>applySchedulers(COMPUTATION_TRANSFORMER))
                         .subscribe(onNext);
    }

    /**
     * 运行一个任务在子线程
     *
     * @param backgroundAction
     * @return
     */
    public static Disposable run(@NonNull Action backgroundAction) {
        return Observable.<Integer>empty()
                         .compose(RxUtil.<Integer>applySchedulers(THREAD_TRANSFORMER))
                         .subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER,
                                    backgroundAction);
    }

    /**
     * 运行一个任务在UI线程
     *
     * @param backgroundSubscribe
     * @param uiAction
     * @return
     */
    public static <T> Disposable runOnUI(
            @NonNull ObservableOnSubscribe<T> backgroundSubscribe,
            @NonNull Consumer<T> uiAction) {
        return Observable.create(backgroundSubscribe)
                         .compose(RxUtil.<T>applySchedulers(THREAD_ON_UI_TRANSFORMER))
                         .subscribe(uiAction);
    }
}
