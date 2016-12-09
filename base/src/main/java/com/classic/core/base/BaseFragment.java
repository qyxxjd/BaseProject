package com.classic.core.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classic.core.event.FragmentEvent;
import com.classic.core.interfaces.IFragment;
import com.classic.core.interfaces.IRegister;
import com.classic.core.permissions.EasyPermissions;
import com.classic.core.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.base
 *
 * 文件描述: Fragment父类
 * 创 建 人: 续写经典
 * 创建时间: 2015/12/16 18:34
 */
@SuppressWarnings("unused") public abstract class BaseFragment extends Fragment
        implements IFragment, IRegister, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final String SP_NAME         = "firstConfig";
    private static final String STATE_IS_HIDDEN = "isHidden";

    private   int      mFragmentState;
    protected Activity mActivity;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        stateChange(FragmentEvent.ATTACH);
        mActivity = (Activity) context;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IS_HIDDEN, isHidden());
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateChange(FragmentEvent.CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        stateChange(FragmentEvent.CREATE_VIEW);
        View                  parentView = inflater.inflate(getLayoutResId(), container, false);
        SharedPreferencesUtil spUtil     = new SharedPreferencesUtil(mActivity, SP_NAME);
        final String          simpleName = this.getClass().getSimpleName();
        if (spUtil.getBooleanValue(simpleName, true)) {
            onFirst();
            spUtil.putBooleanValue(simpleName, false);
        }
        initData();
        initView(parentView, savedInstanceState);
        return parentView;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateChange(FragmentEvent.VIEW_CREATE);
        if (savedInstanceState != null) {
            boolean             isHidden    = savedInstanceState.getBoolean(STATE_IS_HIDDEN);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (isHidden) {
                transaction.hide(this);
                onFragmentHide();
            } else {
                transaction.show(this);
                onFragmentShow();
            }
            transaction.commit();
        }
        register();
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
        super.onPause();
        stateChange(FragmentEvent.PAUSE);
    }

    @Override public void onStop() {
        super.onStop();
        stateChange(FragmentEvent.STOP);
    }

    @Override public void onDestroyView() {
        unRegister();
        super.onDestroyView();
        stateChange(FragmentEvent.DESTROY_VIEW);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        stateChange(FragmentEvent.DESTROY);
    }

    @Override public void onDetach() {
        super.onDetach();
        stateChange(FragmentEvent.DETACH);
    }

    @Override public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override public void onFirst() { }

    @Override public void initData() { }

    @Override public void initView(View parentView, Bundle savedInstanceState) { }

    @Override public void register() { }

    @Override public void unRegister() { }

    @Override public void onFragmentShow() { }

    @Override public void onFragmentHide() { }

    @Override public void showProgress() { }

    @Override public void hideProgress() { }

    @Override public void viewClick(View v) { }

    @Override public void onPermissionsGranted(int requestCode, List<String> perms) { }

    @Override public void onPermissionsDenied(int requestCode, List<String> perms) { }

    @Override public void onClick(View v) {
        viewClick(v);
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
    }
}
