package com.classic.core.rx.clean;

import android.support.annotation.NonNull;

import com.classic.core.event.ActivityEvent;
import com.classic.core.event.FragmentEvent;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;

/**
 * 规范 RxJava 资源清理的接口
 *
 * @author classic
 * @version v1.0, 2018/4/22 下午4:48
 */
public interface AutoClean {

    /**
     * 绑定一个 Activity、Fragment 的生命周期，自动释放资源
     * <br/>
     * 例如：网络请求时绑定{@link ActivityEvent#STOP} 或 {@link FragmentEvent#STOP},
     * onStop()时会自动取消网络请求.
     *
     * @param event 事件类型
     * @see ActivityEvent
     * @see FragmentEvent
     */
    <Type> ObservableTransformer<Type, Type> bindEvent(int event);

    /**
     * 绑定一个 Activity、Fragment 的生命周期，自动释放资源
     * <br/>
     * 例如：网络请求时绑定{@link ActivityEvent#STOP} 或 {@link FragmentEvent#STOP},
     * onStop()时会自动取消网络请求.
     *
     * @param event 事件类型
     * @see ActivityEvent
     * @see FragmentEvent
     */
    @SuppressWarnings("SpellCheckingInspection")
    <Type> FlowableTransformer<Type, Type> bindEventWithFlowable(int event);

    /**
     * 回收Disposable,
     * 默认 {@link ActivityEvent#DESTROY} 或 {@link FragmentEvent#DESTROY_VIEW} 统一释放资源
     *
     * @param disposable Disposable
     */
    void recycle(@NonNull Disposable disposable);

    /**
     * 清理资源
     */
    void clear();

    /**
     * 清理资源
     *
     * @param disposables Disposable
     */
    void clear(Disposable... disposables);
}
