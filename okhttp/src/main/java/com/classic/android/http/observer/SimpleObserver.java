package com.classic.android.http.observer;

import android.text.TextUtils;

import com.classic.android.BasicProject;
import com.elvishew.xlog.XLog;

import io.reactivex.observers.DisposableObserver;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.http.observer
 *
 * 文件描述: 简单的DisposableObserver,只关注onNext
 * 创 建 人: 续写经典
 * 创建时间: 2016/11/03 17:26
 */
@SuppressWarnings("unused") public abstract class SimpleObserver<T> extends DisposableObserver<T> {

    private static final String TAG = "SimpleObserver";

    @Override public void onError(Throwable throwable) {
        if (BasicProject.getInstance().isDebug() && null != throwable &&
            !TextUtils.isEmpty(throwable.getMessage())) {
            throwable.printStackTrace();
            XLog.tag(TAG).e(throwable.getMessage());
        }
    }

    @Override public void onComplete() { }
}
