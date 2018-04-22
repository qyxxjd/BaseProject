package com.classic.core.exception;

import android.support.annotation.NonNull;

import com.classic.core.interfaces.ICrashProcess;

/**
 * 异常信息收集
 *
 * @author classic
 * @version v1.0, 2018/4/22 下午4:48
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static volatile CrashHandler sInstance;
    private final ICrashProcess mCrashProcess;

    private CrashHandler(@NonNull ICrashProcess crashProcessImpl) {
        // 将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.mCrashProcess = crashProcessImpl;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static CrashHandler getInstance(@NonNull ICrashProcess crashProcessImpl) {
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                if (sInstance == null) {
                    sInstance = new CrashHandler(crashProcessImpl);
                }
            }
        }
        return sInstance;
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用uncaughtException方法
     * thread为出现未捕获异常的线程，
     * exception为未捕获的异常。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        mCrashProcess.onException(thread, exception);
    }
}
