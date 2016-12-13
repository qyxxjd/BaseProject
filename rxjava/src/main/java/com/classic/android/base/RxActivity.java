package com.classic.android.base;

import com.classic.android.base.BaseActivity;
import com.classic.android.event.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.activity
 *
 * 文件描述: 集成RxJava的基类
 * 创 建 人: 续写经典
 * 创建时间: 2016/12/8 17:03
 */
public abstract class RxActivity extends BaseActivity {
    private final BehaviorSubject<Integer> mBehaviorSubject = BehaviorSubject.create();

    /**
     * 绑定一个Activity的生命周期
     * <p>
     *     例如：网络请求时绑定ActivityEvent.STOP, onStop()时会自动取消网络请求
     * @param event
     * {@link ActivityEvent#CREATE},
     * {@link ActivityEvent#START},
     * {@link ActivityEvent#RESTART},
     * {@link ActivityEvent#RESUME},
     * {@link ActivityEvent#PAUSE},
     * {@link ActivityEvent#STOP},
     * {@link ActivityEvent#DESTROY}.
     * @param <T>
     * @return
     */
    @SuppressWarnings("unused")
    protected <T> ObservableTransformer<T, T> bindEvent(@ActivityEvent final int event) {
        final Observable<Integer> observable = mBehaviorSubject.filter(new Predicate<Integer>() {
            @Override public boolean test(Integer integer) throws Exception {
                return integer == event;
            }
        }).take(1);

        return new ObservableTransformer<T, T>() {
            @Override public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.takeUntil(observable);
            }
        };
    }

    @Override void stateChange(@ActivityEvent int event) {
        super.stateChange(event);
        mBehaviorSubject.onNext(event);
    }
}
