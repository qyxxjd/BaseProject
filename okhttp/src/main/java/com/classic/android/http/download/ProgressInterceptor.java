package com.classic.android.http.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.http.download
 *
 * 文件描述: 下载进度拦截器
 * 创 建 人: 续写经典
 * 创建时间: 2016/12/22 10:14
 * <br/>
 * {https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/Progress.java}
 */
class ProgressInterceptor implements Interceptor {

    private ProgressListener mListener;

    ProgressInterceptor(ProgressListener listener) {
        this.mListener = listener;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                               .body(new ProgressResponseBody(originalResponse.body(), mListener))
                               .build();
    }
}
