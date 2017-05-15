package com.classic.android.rx;

import android.os.Looper;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.rx
 *
 * 文件描述: 线程工具类
 * 创 建 人: 续写经典
 * 创建时间: 2017/5/15 12:44
 */
@SuppressWarnings("unused") public final class ThreadUtil {

    private ThreadUtil() {
        // no instance.
    }

    @SuppressWarnings("WeakerAccess") public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }
}
