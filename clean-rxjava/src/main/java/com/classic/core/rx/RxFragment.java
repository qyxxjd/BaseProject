package com.classic.core.rx;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classic.core.event.FragmentEvent;
import com.classic.core.rx.clean.AutoClean;
import com.classic.core.rx.clean.RxAutoCleanDelegate;
import com.classic.core.rx.lifecycle.LifecycleListener;

import java.util.List;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 集成RxJava的基类
 *
 * @author classic
 * @version v1.0, 2018/4/22 下午4:48
 */
@SuppressWarnings("unused")
public abstract class RxFragment extends Fragment implements AutoClean, LifecycleListener,
        EasyPermissions.PermissionCallbacks {

    protected abstract @LayoutRes int layout();
    protected abstract void initView(@NonNull View parentView, @Nullable Bundle savedInstanceState);

    private final RxAutoCleanDelegate mAutoCleanDelegate = new RxAutoCleanDelegate();

    private int mFragmentState;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        stateChange(FragmentEvent.ATTACH);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateChange(FragmentEvent.CREATE);
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        stateChange(FragmentEvent.CREATE_VIEW);
        View parentView = inflater.inflate(layout(), container, false);
        initView(parentView, savedInstanceState);
        return parentView;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateChange(FragmentEvent.VIEW_CREATE);
    }

    @Override public void onStart() {
        super.onStart();
        stateChange(FragmentEvent.START);
    }

    @Override public void onResume() {
        super.onResume();
        stateChange(FragmentEvent.RESUME);
    }

    @Override public void onPause() {
        stateChange(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override public void onStop() {
        stateChange(FragmentEvent.STOP);
        super.onStop();
    }

    @Override public void onDestroyView() {
        stateChange(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        stateChange(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override public void onDetach() {
        stateChange(FragmentEvent.DETACH);
        super.onDetach();
    }

    /**
     * 获取当前Fragment状态
     * {@link FragmentEvent#ATTACH},
     * {@link FragmentEvent#CREATE},
     * {@link FragmentEvent#CREATE_VIEW},
     * {@link FragmentEvent#VIEW_CREATE},
     * {@link FragmentEvent#START},
     * {@link FragmentEvent#RESUME},
     * {@link FragmentEvent#PAUSE},
     * {@link FragmentEvent#STOP},
     * {@link FragmentEvent#DESTROY_VIEW},
     * {@link FragmentEvent#DESTROY},
     * {@link FragmentEvent#DETACH}.
     */
    public int getFragmentState() {
        return mFragmentState;
    }

    void stateChange(@FragmentEvent int event) {
        mFragmentState = event;
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

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) { }

    @Override public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) { }
}
