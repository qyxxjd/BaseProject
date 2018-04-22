package com.classic.core.interfaces;

/**
 * 规范崩溃日志接口协议
 *
 * @author classic
 * @version v1.0, 2018/4/22 下午4:48
 */
public interface ICrashProcess {

    void onException(Thread thread, Throwable exception);
}
