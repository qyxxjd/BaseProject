package com.classic.android.base;

import android.support.annotation.NonNull;

import com.classic.android.event.ActivityEvent;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.activity
 *
 * 文件描述: 集成RxJava的基类
 * 创 建 人: 续写经典
 * 创建时间: 2016/12/8 17:03
 */
@SuppressWarnings("unused") public abstract class RxActivity extends BaseActivity {
    private final BehaviorSubject<Integer> mBehaviorSubject = BehaviorSubject.create();

    private CompositeSubscription mCompositeSubscription;

    /**
     * 绑定一个Activity的生命周期
     * <p>
     * 例如：网络请求时绑定ActivityEvent.STOP, onStop()时会自动取消网络请求
     *
     * @param event {@link ActivityEvent#CREATE},
     *              {@link ActivityEvent#START},
     *              {@link ActivityEvent#RESTART},
     *              {@link ActivityEvent#RESUME},
     *              {@link ActivityEvent#PAUSE},
     *              {@link ActivityEvent#STOP},
     *              {@link ActivityEvent#DESTROY}.
     */
    @SuppressWarnings("unused") protected <T> Observable.Transformer<T, T> bindEvent(@ActivityEvent final int event) {
        final Observable<Integer> observable = mBehaviorSubject.takeFirst(new Func1<Integer, Boolean>() {
            @Override public Boolean call(Integer integer) {
                return event == integer;
            }
        });
        return new Observable.Transformer<T, T>() {
            @Override public Observable<T> call(Observable<T> tObservable) {
                return tObservable.takeUntil(observable);
            }
        };
    }

    /**
     * 回收Subscription，默认unRegister统一释放资源
     */
    protected void recycle(@NonNull Subscription subscription) {
        if (null == mCompositeSubscription) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    /**
     * 清理Subscription，释放资源
     */
    protected void clear() {
        if (null != mCompositeSubscription) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override public void unRegister() {
        clear();
        super.unRegister();
    }

    @Override void stateChange(@ActivityEvent int event) {
        super.stateChange(event);
        mBehaviorSubject.onNext(event);
    }
}
