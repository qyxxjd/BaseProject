package com.classic.core.rx;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.classic.core.event.ActivityEvent;
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
public abstract class RxActivity extends AppCompatActivity implements AutoClean, LifecycleListener,
        EasyPermissions.PermissionCallbacks {

    protected abstract @LayoutRes int layout();
    protected abstract void initView(@Nullable Bundle savedInstanceState);

    private final RxAutoCleanDelegate mAutoCleanDelegate = new RxAutoCleanDelegate();

    private int mActivityState;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateChange(ActivityEvent.CREATE);
        setContentView(layout());
        initView(savedInstanceState);
    }

    @Override protected void onStart() {
        super.onStart();
        stateChange(ActivityEvent.START);
    }

    @Override protected void onRestart() {
        super.onRestart();
        stateChange(ActivityEvent.RESTART);
    }

    @Override protected void onResume() {
        super.onResume();
        stateChange(ActivityEvent.RESUME);
    }

    @Override protected void onPause() {
        stateChange(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override protected void onStop() {
        stateChange(ActivityEvent.STOP);
        super.onStop();
    }

    @Override protected void onDestroy() {
        stateChange(ActivityEvent.DESTROY);
        super.onDestroy();
    }

    /**
     * 获取当前Activity状态
     * {@link ActivityEvent#CREATE},
     * {@link ActivityEvent#START},
     * {@link ActivityEvent#RESTART},
     * {@link ActivityEvent#RESUME},
     * {@link ActivityEvent#PAUSE},
     * {@link ActivityEvent#STOP},
     * {@link ActivityEvent#DESTROY}.
     */
    public int getActivityState() {
        return mActivityState;
    }

    void stateChange(@ActivityEvent int event) {
        mActivityState = event;
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

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) { }

    @Override public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) { }
}
