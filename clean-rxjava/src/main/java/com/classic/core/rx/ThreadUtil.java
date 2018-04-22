package com.classic.core.rx;

import android.os.Looper;

/**
 * 线程工具类
 *
 * @author classic
 * @version v1.0, 2018/4/22 下午4:48
 */
@SuppressWarnings("unused")
public final class ThreadUtil {

    private ThreadUtil() {
        // no instance.
    }

    @SuppressWarnings("WeakerAccess")
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }
}
