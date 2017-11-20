package com.classic.android.http.download;

import android.support.annotation.NonNull;

import com.classic.android.http.function.FileFunction;
import com.classic.android.rx.RxTransformer;
import com.classic.android.rx.observer.AutoObserver;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.http.download
 *
 * 文件描述: 下载工具类
 * 创 建 人: 续写经典
 * 创建时间: 2016/12/22 10:48
 *
 * <pre>
 *     使用示例：
 *     DownloadUtil.create(url, file); //不需要回调的情况
 *
 *     DownloadUtil.create(url, file, new ProgressListener() {
 *          @Override
 *          public void onProgress(long currentBytes, long totalBytes, boolean isDone) {
 *              // ...
 *          }
 *
 *          @Override
 *          public void onSuccess(File file) {
 *              // ...
 *          }
 *
 *          @Override
 *          public void onFailure(Throwable throwable) {
 *              // ...
 *          }
 *     });
 * </pre>
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class DownloadUtil {
    private static final int    TIMEOUT   = 30;
    private static final String SEPARATOR = "/";

    private DownloadUtil() {
        // no instance
    }

    public static void create(@NonNull String url, @NonNull final String file) {
        create(url, new File(file), null);
    }

    public static void create(@NonNull String url, @NonNull final String file, final ProgressListener listener) {
        create(url, new File(file), listener);
    }

    public static void create(@NonNull String url, @NonNull final File file) {
        create(url, file, null);
    }

    public static void create(@NonNull String url, @NonNull final File file, final ProgressListener listener) {
        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new ProgressInterceptor(listener))
                                                              .retryOnConnectionFailure(true)
                                                              .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                                                              .build();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(getBaseUrl(url))
                                                        .client(client)
                                                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                                        .build();
        retrofit.create(DownloadApi.class)
                .download(url)
                .map(new FileFunction(file))
                .compose(RxTransformer.<File>applySchedulers(RxTransformer.Observable.IO))
                .subscribe(new AutoObserver<File>() {
                    @Override public void onNext(File file) {
                        if (null != listener) {
                            listener.onSuccess(file);
                        }
                    }

                    @Override public void onError(Throwable e) {
                        if (null != listener) {
                            listener.onFailure(e);
                        }
                        super.onError(e);
                    }
                });
    }

    private static String getBaseUrl(@NonNull String url) {
        try {
            final String[] items = url.split(SEPARATOR);
            //noinspection StringBufferReplaceableByString
            return new StringBuilder().append(items[0])
                                      .append(SEPARATOR)
                                      .append(SEPARATOR)
                                      .append(items[2])
                                      .append(SEPARATOR)
                                      .toString();
        } catch (Exception e) {
            // http://.../....
            // https://.../....
            throw new IllegalArgumentException("error url:" + url);
        }
    }

}
