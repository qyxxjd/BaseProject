package com.classic.core.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.utils
 *
 * 文件描述: 双击退出应用程序工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/4 17:26
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public class DoubleClickExitHelper {

    private static final String DEFAULT_TOAST_STRING = "再按一次返回键退出应用";

    private final Activity mActivity;

    /** 两次点击的有效时间间隔，单位：毫秒 */
    private int timeInterval = 2000;
    private boolean     isOnKeyBacking;
    private WeakHandler mHandler;
    private Toast       mBackToast;

    /**
     * 设置两次点击的有效时间间隔
     *
     * @param time 单位：毫秒
     */
    public DoubleClickExitHelper setTimeInterval(int time) {
        timeInterval = time;
        return this;
    }

    /**
     * 设置toast内容
     */
    @SuppressLint("ShowToast") public DoubleClickExitHelper setToastContent(int resId) {
        mBackToast = Toast.makeText(mActivity, resId, Toast.LENGTH_LONG);
        return this;
    }

    /**
     * 设置toast内容
     */
    @SuppressLint("ShowToast") public DoubleClickExitHelper setToastContent(
            @NonNull String content) {
        mBackToast = Toast.makeText(mActivity, content, Toast.LENGTH_LONG);
        return this;
    }

    @SuppressLint("ShowToast") public DoubleClickExitHelper(@NonNull Activity activity) {
        mActivity = activity;
        mHandler = new WeakHandler(Looper.getMainLooper());
        mBackToast = Toast.makeText(mActivity, DEFAULT_TOAST_STRING, Toast.LENGTH_LONG);
    }

    /**
     * Activity onKeyDown事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false;
        }
        if (isOnKeyBacking) {
            mHandler.removeCallbacks(onBackTimeRunnable);
            if (mBackToast != null) {
                mBackToast.cancel();
            }
            mActivity.finish();
            return true;
        } else {
            isOnKeyBacking = true;
            mBackToast.show();
            mHandler.postDelayed(onBackTimeRunnable, timeInterval);
            return true;
        }
    }

    private Runnable onBackTimeRunnable = new Runnable() {

        @Override public void run() {
            isOnKeyBacking = false;
            if (mBackToast != null) {
                mBackToast.cancel();
            }
        }
    };
}
