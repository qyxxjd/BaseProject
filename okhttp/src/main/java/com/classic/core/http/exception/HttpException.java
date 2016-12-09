package com.classic.core.http.exception;

import android.text.TextUtils;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.http.exception
 *
 * 文件描述: 网络异常
 * 创 建 人: 续写经典
 * 创建时间: 2016/4/29 13:39
 */
@SuppressWarnings("unused") public class HttpException extends RuntimeException {

    private int mCode;

    public HttpException(int code, String detailMessage) {
        super(TextUtils.isEmpty(detailMessage) ? "" : detailMessage);
        this.mCode = code;
    }

    /** 获取错误状态码 */
    public int getCode() {
        return mCode;
    }

    @Override public String toString() {
        return "HttpException{" +
               "code=" + mCode +
               ", detailMessage='" + getMessage() + '\'' +
               '}';
    }
}
