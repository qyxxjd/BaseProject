package com.classic.http;

import android.text.TextUtils;

import com.classic.android.BasicProject;

import rx.Subscriber;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.http
 *
 * 文件描述: TODO
 * 创 建 人: 续写经典
 * 创建时间: 2017/5/15 17:32
 */
public abstract class SimpleSubscriber<T> extends Subscriber<T> {

    @Override public void onCompleted() { }

    @Override public void onError(Throwable throwable) {
        if (BasicProject.getInstance().isDebug() && null != throwable &&
            !TextUtils.isEmpty(throwable.getMessage())) {
            throwable.printStackTrace();
        }
    }

}
