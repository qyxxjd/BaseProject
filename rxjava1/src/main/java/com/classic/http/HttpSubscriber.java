package com.classic.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.classic.android.BasicProject;
import com.classic.rx.R;
import com.elvishew.xlog.XLog;

import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.http
 *
 * 文件描述: TODO
 * 创 建 人: 续写经典
 * 创建时间: 2017/5/15 17:21
 */
public abstract class HttpSubscriber<T> extends Subscriber<T> {

    private static final String TAG = "HttpObserver";
    private Context mAppContext;
    private boolean isDebug;

    public HttpSubscriber(@NonNull Context context) {
        this.mAppContext = context.getApplicationContext();
        this.isDebug = BasicProject.getInstance().isDebug();
    }

    private boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isAvailable();
    }

    @Override public void onStart() {
        super.onStart();
        if (!isNetworkAvailable(mAppContext)) {
            unsubscribe();
            onNetworkNotConnected();
            onCancel();
            onFinish();
        }
    }

    @Override public final void onNext(T t) {
        onSuccess(t);
    }

    @Override public void onCompleted() {
        onFinish();
    }

    @Override public void onError(Throwable e) {
        if (e instanceof HttpException) {
            final HttpException exception = (HttpException) e;
            onFailure(exception);
        } else if (e instanceof SocketTimeoutException) {
            onSocketTimeout();
        } else {
            onFailure(new HttpException(-1, e.getMessage()));
        }
        onUnifiedErrorHandling();
        onFinish();
    }

    /**
     * 接口调用之前会检测网络，如果无网络执行此回调方法
     */
    public void onNetworkNotConnected() {
        if (isDebug) {
            XLog.tag(TAG).e(mAppContext.getResources().getString(R.string.network_error_hint));
        }
    }

    /**
     * 请求成功回调
     */
    public abstract void onSuccess(T t);

    /** 网络连接超时回调 */
    public void onSocketTimeout() {
        if (isDebug) {
            XLog.tag(TAG).e(mAppContext.getResources().getString(R.string.network_timeout_hint));
        }
    }

    /**
     * 请求失败回调
     *
     * @see HttpException
     */
    public void onFailure(HttpException e) {
        if (isDebug && null != e && !TextUtils.isEmpty(e.getMessage())) {
            XLog.tag(TAG).e(e.getMessage());
        }
    }

    /** 统一的错误处理,所有的错误最终都会执行此方法 */
    public void onUnifiedErrorHandling() { }

    /** 网络请求取消回调 */
    public void onCancel() { }

    /**
     * 不管成功失败，最后都会执行此方法
     */
    public void onFinish() { }

}
