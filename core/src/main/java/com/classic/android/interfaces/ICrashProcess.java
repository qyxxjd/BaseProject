package com.classic.android.interfaces;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.interfaces
 *
 * 文件描述: 规范崩溃日志接口协议
 * 创 建 人: 续写经典
 * 创建时间: 2015/6/21 10:00
 */
public interface ICrashProcess {

    void onException(Thread thread, Throwable exception) throws Exception;
}
