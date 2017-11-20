package com.classic.android.interfaces;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.interfaces
 *
 * 文件描述: 规范Fragment接口协议
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/12 10:00
 */
public interface IProgress {

    /**
     * 显示进度条
     */
    void showProgress();

    /**
     * 隐藏进度条
     */
    void hideProgress();
}
