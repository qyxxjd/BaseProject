package com.classic.http;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.http.function
 *
 * 文件描述: 重试机制
 * 创 建 人: 续写经典
 * 创建时间: 2016/8/1 13:39
 * <pre>
 *     示例：重试5次，重试延迟间隔1秒
 *    observable.retryWhen(new RetryFunc(5, 1, TimeUnit.SECONDS))
 *              .subscribeOn(Schedulers.computation())
 *              .observeOn(Schedulers.io())
 *              .subscribe(...);
 * </pre>
 */
@SuppressWarnings("unused") public class RetryFunc implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final TimeUnit mTimeUnit;
    private final int      mMaxRetryCount;
    private final int      mRetryDelay;
    private       int      mRetryCount;

    public RetryFunc(int maxRetries, int retryDelayMillis, TimeUnit timeUnit) {
        this.mMaxRetryCount = maxRetries;
        this.mRetryDelay = retryDelayMillis;
        this.mTimeUnit = timeUnit;
        mRetryCount = 0;
    }

    @Override public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override public Observable<?> call(Throwable throwable) {
                if (++mRetryCount < mMaxRetryCount) {
                    return Observable.timer(mRetryDelay, mTimeUnit);
                }
                return Observable.error(throwable);
            }
        });
    }
}
