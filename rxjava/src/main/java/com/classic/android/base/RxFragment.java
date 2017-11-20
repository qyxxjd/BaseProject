package com.classic.android.base;

import android.support.annotation.NonNull;

import com.classic.android.event.FragmentEvent;
import com.classic.android.rx.clean.AutoClean;
import com.classic.android.rx.clean.RxAutoCleanDelegate;
import com.classic.android.rx.lifecycle.LifecycleListener;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.activity
 *
 * 文件描述: 集成RxJava的基类
 * 创 建 人: 续写经典
 * 创建时间: 2016/12/8 17:03
 */
@SuppressWarnings("unused") public abstract class RxFragment extends BaseFragment
        implements AutoClean, LifecycleListener {

    private final RxAutoCleanDelegate mAutoCleanDelegate = new RxAutoCleanDelegate();

    @Override void stateChange(@FragmentEvent int event) {
        super.stateChange(event);
        onLifecycleChange(event);
    }

    @Override
    public void onLifecycleChange(@FragmentEvent int event) {
        mAutoCleanDelegate.onLifecycleChange(event);
    }

    /**
     * 绑定 Fragment 的生命周期，自动释放资源
     * <br/>
     * 例如：网络请求时绑定{@link FragmentEvent#STOP}, onStop()时会自动取消网络请求.
     *
     * @param event 事件类型
     * @see FragmentEvent
     */
    @Override
    public <Type> ObservableTransformer<Type, Type> bindEvent(@FragmentEvent int event) {
        return mAutoCleanDelegate.bindEvent(event);
    }

    /**
     * 绑定 Activity 的生命周期，自动释放资源
     * <br/>
     * 例如：网络请求时绑定{@link FragmentEvent#STOP}, onStop()时会自动取消网络请求.
     *
     * @param event 事件类型
     * @see FragmentEvent
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public <Type> FlowableTransformer<Type, Type> bindEventWithFlowable(@FragmentEvent int event) {
        return mAutoCleanDelegate.bindEventWithFlowable(event);
    }

    @Override
    public void recycle(@NonNull Disposable disposable) {
        mAutoCleanDelegate.recycle(disposable);
    }

    @Override
    public void clear() {
        mAutoCleanDelegate.clear();
    }

    @Override
    public void clear(Disposable... disposables) {
        mAutoCleanDelegate.clear(disposables);
    }
}
