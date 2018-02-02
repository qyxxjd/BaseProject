package com.classic.android.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.classic.android.interfaces.IActivity;
import com.classic.android.utils.SharedPreferencesUtil;
import com.classic.android.event.ActivityEvent;
import com.classic.android.interfaces.IRegister;
import com.classic.android.permissions.EasyPermissions;

import java.util.List;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.base
 *
 * 文件描述: Activity父类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/7 18:34
 */
@SuppressWarnings("unused") public abstract class BaseActivity extends AppCompatActivity
        implements View.OnClickListener, IActivity, IRegister, EasyPermissions.PermissionCallbacks {

    private static final String SP_NAME = "firstConfig";

    private int mActivityState;
    private BaseFragment mCurrentFragment;
    protected Activity mActivity;
    protected Context mAppContext;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateChange(ActivityEvent.CREATE);
        mAppContext = getApplicationContext();
        mActivity = this;
        BaseActivityStack.getInstance().addActivity(this);
        onSetContentViewBefore();
        setContentView(getLayoutResId());
        SharedPreferencesUtil spUtil = new SharedPreferencesUtil(this, SP_NAME);
        final String simpleName = this.getClass().getSimpleName();
        if (spUtil.getBooleanValue(simpleName, true)) {
            onFirst();
            spUtil.putBooleanValue(simpleName, false);
        }
        initData();
        initView(savedInstanceState);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override public void onFirst() { }

    @Override public void onSetContentViewBefore() { }

    @Override public void initData() { }

    @Override public void initView(@Nullable Bundle savedInstanceState) { }

    @Override public void register() { }

    @Override public void unRegister() { }

    @Override public void viewClick(@NonNull View v) { }

    @Override public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) { }

    @Override public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) { }

    @Override public void onClick(View v) {
        viewClick(v);
    }

    @Override public void skipActivity(@NonNull Activity aty, @NonNull Class<?> cls) {
        startActivity(aty, cls);
        aty.finish();
    }

    @Override public void skipActivity(@NonNull Activity aty, @NonNull Intent it) {
        startActivity(aty, it);
        aty.finish();
    }

    @Override public void skipActivity(@NonNull Activity aty, @NonNull Class<?> cls, @NonNull Bundle extras) {
        startActivity(aty, cls, extras);
        aty.finish();
    }

    @Override public void startActivity(@NonNull Activity aty, @NonNull Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    @Override public void startActivity(@NonNull Activity aty, @NonNull Intent it) {
        aty.startActivity(it);
    }

    @Override public void startActivity(@NonNull Activity aty, @NonNull Class<?> cls, @NonNull Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
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
        register();
    }

    @Override protected void onPause() {
        stateChange(ActivityEvent.PAUSE);
        super.onPause();
        unRegister();
    }

    @Override protected void onStop() {
        stateChange(ActivityEvent.STOP);
        super.onStop();
    }

    @Override protected void onDestroy() {
        stateChange(ActivityEvent.DESTROY);
        super.onDestroy();
        BaseActivityStack.getInstance().finishActivity(this);
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

    /**
     * 获取当前显示的Fragment
     */
    public BaseFragment getFragment() {
        return mCurrentFragment;
    }

    /**
     * 用Fragment替换视图
     *
     * @param resView        将要被替换掉的视图
     * @param targetFragment 用来替换的Fragment
     */
    public void changeFragment(int resView, @NonNull BaseFragment targetFragment) {
        if (targetFragment.equals(mCurrentFragment)) {
            return;
        }
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(resView, targetFragment, targetFragment.getClass().getName());
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);
            targetFragment.onFragmentShow();
        }
        if (mCurrentFragment != null && mCurrentFragment.isVisible()) {
            transaction.hide(mCurrentFragment);
            mCurrentFragment.onFragmentHide();
        }
        mCurrentFragment = targetFragment;
        transaction.commitAllowingStateLoss();
    }

    void stateChange(@ActivityEvent int event) {
        mActivityState = event;
    }
}
