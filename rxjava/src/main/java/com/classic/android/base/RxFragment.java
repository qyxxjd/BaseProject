package com.classic.android.base;

import android.support.annotation.NonNull;

import com.classic.android.event.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
@SuppressWarnings("unused") public abstract class RxFragment extends BaseFragment {
    private final BehaviorSubject<Integer> mBehaviorSubject = BehaviorSubject.create();

    private CompositeDisposable mCompositeDisposable;

    /**
     * 绑定一个Fragment的生命周期
     * <p>
     * 例如：网络请求时绑定FragmentEvent.STOP, onStop()时会自动取消网络请求
     *
     * @param event {@link FragmentEvent#ATTACH},
     *              {@link FragmentEvent#CREATE},
     *              {@link FragmentEvent#CREATE_VIEW},
     *              {@link FragmentEvent#VIEW_CREATE},
     *              {@link FragmentEvent#START},
     *              {@link FragmentEvent#RESUME},
     *              {@link FragmentEvent#PAUSE},
     *              {@link FragmentEvent#STOP},
     *              {@link FragmentEvent#DESTROY_VIEW},
     *              {@link FragmentEvent#DESTROY},
     *              {@link FragmentEvent#DETACH}.
     */
    @SuppressWarnings("unused") protected <T> ObservableTransformer<T, T> bindEvent(@FragmentEvent final int event) {
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

    /**
     * 回收Disposable，默认unRegister统一释放资源
     */
    protected void recycle(@NonNull Disposable disposable) {
        if (null == mCompositeDisposable) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * 清理Disposable，释放资源
     */
    protected void clear() {
        if (null != mCompositeDisposable) {
            mCompositeDisposable.clear();
        }
    }

    @Override public void unRegister() {
        clear();
        super.unRegister();
    }

    @Override void stateChange(@FragmentEvent int event) {
        super.stateChange(event);
        mBehaviorSubject.onNext(event);
    }
}
