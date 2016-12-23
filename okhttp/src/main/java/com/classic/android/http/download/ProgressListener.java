package com.classic.android.http.download;

import java.io.File;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.http.download
 *
 * 文件描述: 下载进度回调
 * 创 建 人: 续写经典
 * 创建时间: 2016/12/22 10:10
 */
public interface ProgressListener {

    /**
     * 下载进度回调
     * @param currentBytes 已下载字节数
     * @param totalBytes 总字节数
     * @param isDone 是否完成
     */
    void onProgress(long currentBytes, long totalBytes, boolean isDone);

    /**
     * 下载完成
     * @param file
     */
    void onSuccess(File file);

    /**
     * 下载失败
     * @param throwable 异常信息
     */
    void onFailure(Throwable throwable);
}
