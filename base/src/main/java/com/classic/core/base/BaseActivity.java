package com.classic.core.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.classic.core.event.ActivityEvent;
import com.classic.core.interfaces.IActivity;
import com.classic.core.interfaces.IRegister;
import com.classic.core.permissions.EasyPermissions;
import com.classic.core.utils.SharedPreferencesUtil;
import java.util.List;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.base
 *
 * 文件描述: Activity父类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/7 18:34
 */
@SuppressWarnings("unused") public abstract class BaseActivity extends AppCompatActivity
        implements View.OnClickListener, IActivity, IRegister, EasyPermissions.PermissionCallbacks {

    private static final String SP_NAME = "firstConfig";

    private   int          mActivityState;
    private   BaseFragment mCurrentFragment;
    protected Activity     mActivity;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateChange(ActivityEvent.CREATE);
        mActivity = this;
        initPre();
        BaseActivityStack.getInstance().addActivity(this);
        setContentView(getLayoutResId());
        SharedPreferencesUtil spUtil     = new SharedPreferencesUtil(this, SP_NAME);
        final String          simpleName = this.getClass().getSimpleName();
        if (spUtil.getBooleanValue(simpleName, true)) {
            onFirst();
            spUtil.putBooleanValue(simpleName, false);
        }
        initData();
        initView(savedInstanceState);
        register();
    }

    @Override public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override public void onFirst() { }

    @Override public void initPre() { }

    @Override public void initData() { }

    @Override public void initView(Bundle savedInstanceState) { }

    @Override public void showProgress() { }

    @Override public void hideProgress() { }

    @Override public void register() { }

    @Override public void unRegister() { }

    @Override public void viewClick(View v) { }

    @Override public void onPermissionsGranted(int requestCode, List<String> perms) { }

    @Override public void onPermissionsDenied(int requestCode, List<String> perms) { }

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

    @Override public void skipActivity(
            @NonNull Activity aty, @NonNull Class<?> cls, @NonNull Bundle extras) {
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

    @Override public void startActivity(
            @NonNull Activity aty, @NonNull Class<?> cls, @NonNull Bundle extras) {
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
    }

    @Override protected void onPause() {
        super.onPause();
        stateChange(ActivityEvent.PAUSE);
    }

    @Override protected void onStop() {
        super.onStop();
        stateChange(ActivityEvent.STOP);
    }

    @Override protected void onDestroy() {
        unRegister();
        super.onDestroy();
        stateChange(ActivityEvent.DESTROY);
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
        android.support.v4.app.FragmentTransaction transaction
                = getSupportFragmentManager().beginTransaction();
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
        transaction.commit();
    }

    void stateChange(@ActivityEvent int event) {
        mActivityState = event;
    }
}
