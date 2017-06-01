package com.classic.rx;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observables.AsyncOnSubscribe;
import rx.schedulers.Schedulers;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.rx
 *
 * 文件描述: RxJava工具类
 * 创 建 人: 续写经典
 * 创建时间: 2016/11/03 17:26
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public final class RxUtil {

    private RxUtil() { }

    public static final Observable.Transformer THREAD_TRANSFORMER = new Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable)observable).subscribeOn(Schedulers.newThread())
                                           .unsubscribeOn(Schedulers.newThread())
                                           .observeOn(Schedulers.newThread());
        }
    };

    public static final Observable.Transformer THREAD_ON_UI_TRANSFORMER = new Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable)observable).subscribeOn(Schedulers.newThread())
                                           .unsubscribeOn(Schedulers.newThread())
                                           .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static final Observable.Transformer IO_TRANSFORMER = new Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable)observable).subscribeOn(Schedulers.io())
                                           .unsubscribeOn(Schedulers.io())
                                           .observeOn(Schedulers.io());
        }
    };

    public static final Observable.Transformer IO_ON_UI_TRANSFORMER = new Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable)observable).subscribeOn(Schedulers.io())
                                           .unsubscribeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static final Observable.Transformer COMPUTATION_TRANSFORMER       = new Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable)observable).subscribeOn(Schedulers.computation())
                                           .unsubscribeOn(Schedulers.newThread())
                                           .observeOn(Schedulers.newThread());
        }
    };
    public static final Observable.Transformer COMPUTATION_ON_UI_TRANSFORMER = new Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable)observable).subscribeOn(Schedulers.computation())
                                           .unsubscribeOn(Schedulers.newThread())
                                           .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static final Action1<Throwable> ERROR_ACTION = new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
            if (null != throwable && !TextUtils.isEmpty(throwable.getMessage())) {
                throwable.printStackTrace();
            }
        }
    };

    public static <T> Observable.Transformer<T, T> applySchedulers(Observable.Transformer transformer) {
        //noinspection unchecked
        return (Observable.Transformer<T, T>)transformer;
    }

    /**
     * 运行一个定时任务在子线程
     *
     * @param delay  延迟时间，单位：毫秒
     * @param onNext
     * @return
     */
    public static Subscription time(long delay, @NonNull Action1<Long> onNext) {
        return time(delay, TimeUnit.MILLISECONDS, onNext);
    }

    /**
     * 运行一个定时任务在子线程
     *
     * @param delay  延迟时间
     * @param unit   单位
     * @param onNext
     * @return
     */
    public static Subscription time(long delay, TimeUnit unit, @NonNull Action1<Long> onNext) {
        return Observable.timer(delay, unit)
                         .compose(RxUtil.<Long>applySchedulers(COMPUTATION_TRANSFORMER))
                         .subscribe(onNext);
    }

    /**
     * 运行一个定时任务在UI线程
     *
     * @param delay  延迟时间
     * @param unit   单位
     * @param onNext
     * @return
     */
    public static Subscription timeOnUI(long delay, TimeUnit unit, @NonNull Action1<Long> onNext) {
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
    public static Subscription interval(long interval, @NonNull Action1<Long> onNext) {
        return interval(interval, TimeUnit.MILLISECONDS, onNext);
    }

    /**
     * 运行一个轮询任务在子线程
     *
     * @param interval 轮询间隔
     * @param unit     单位
     * @param onNext
     * @return
     */
    public static Subscription interval(long interval, TimeUnit unit, @NonNull Action1<Long> onNext) {
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
    public static Subscription run(@NonNull Action0 backgroundAction) {
        return Observable.<Integer>empty().compose(RxUtil.<Integer>applySchedulers(THREAD_TRANSFORMER))
                                          .subscribe(new Action1<Integer>() {
                                              @Override public void call(Integer integer) {

                                              }
                                          }, new Action1<Throwable>() {
                                              @Override public void call(Throwable throwable) {

                                              }
                                          }, backgroundAction);
    }

    /**
     * 运行一个任务在UI线程
     *
     * @param backgroundSubscribe
     * @param uiAction
     * @return
     */
    public static <S, T> Subscription runOnUI(@NonNull AsyncOnSubscribe<S, T> backgroundSubscribe,
                                              @NonNull Action1<T> uiAction) {
        return Observable.create(backgroundSubscribe)
                         .compose(RxUtil.<T>applySchedulers(THREAD_ON_UI_TRANSFORMER))
                         .subscribe(uiAction);
    }
}
