package com.classic.android.base;

import android.support.annotation.NonNull;

import com.classic.android.event.ActivityEvent;
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
@SuppressWarnings("unused") public abstract class RxActivity extends BaseActivity
        implements AutoClean, LifecycleListener {

    private final RxAutoCleanDelegate mAutoCleanDelegate = new RxAutoCleanDelegate();

    @Override void stateChange(@ActivityEvent int event) {
        super.stateChange(event);
        onLifecycleChange(event);
    }

    @Override
    public void onLifecycleChange(@ActivityEvent int event) {
        mAutoCleanDelegate.onLifecycleChange(event);
    }

    /**
     * 绑定 Activity 的生命周期，自动释放资源
     * <br/>
     * 例如：网络请求时绑定{@link ActivityEvent#STOP}, onStop()时会自动取消网络请求.
     *
     * @param event 事件类型
     * @see ActivityEvent
     */
    @Override
    public <Type> ObservableTransformer<Type, Type> bindEvent(@ActivityEvent int event) {
        return mAutoCleanDelegate.bindEvent(event);
    }

    /**
     * 绑定 Activity 的生命周期，自动释放资源
     * <br/>
     * 例如：网络请求时绑定{@link ActivityEvent#STOP}, onStop()时会自动取消网络请求.
     *
     * @param event 事件类型
     * @see ActivityEvent
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public <Type> FlowableTransformer<Type, Type> bindEventWithFlowable(@ActivityEvent int event) {
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
