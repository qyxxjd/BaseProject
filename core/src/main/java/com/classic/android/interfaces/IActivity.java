package com.classic.android.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.interfaces
 *
 * 文件描述: 规范Activity接口协议
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/4 10:00
 */
@SuppressWarnings("unused") public interface IActivity {

    /**
     * 获取布局文件
     */
    int getLayoutResId();

    /** 第一次启动会执行此方法 */
    void onFirst();

    /**
     * 此方法会在setContentView之前调用
     */
    void onSetContentViewBefore();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化控件
     */
    void initView(@Nullable Bundle savedInstanceState);

    /**
     * 点击事件回调方法
     */
    void viewClick(@NonNull View v);

    /**
     * 跳转指定activity，并关闭当前activity
     */
    void skipActivity(@NonNull Activity aty, @NonNull Class<?> cls);

    /**
     * 跳转指定activity，并关闭当前activity
     */
    void skipActivity(@NonNull Activity aty, @NonNull Intent it);

    /**
     * 跳转指定activity，并关闭当前activity
     */
    void skipActivity(@NonNull Activity aty, @NonNull Class<?> cls, @NonNull Bundle extras);

    /**
     * 跳转activity
     */
    void startActivity(@NonNull Activity aty, @NonNull Class<?> cls);

    /**
     * 跳转activity
     */
    void startActivity(@NonNull Activity aty, @NonNull Intent it);

    /**
     * 跳转activity
     */
    void startActivity(@NonNull Activity aty, @NonNull Class<?> cls, @NonNull Bundle extras);
}
