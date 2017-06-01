package com.classic.http.download;

import android.support.annotation.NonNull;

import com.classic.rx.RxUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

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
@SuppressWarnings("unused") public final class DownloadUtil {
    private static final int    TIMEOUT   = 30;
    private static final String SEPARATOR = "/";

    private DownloadUtil() {
        // no instance
    }

    public static Subscription create(@NonNull String url, @NonNull final String file) {
        return create(url, new File(file), null);
    }

    public static Subscription create(@NonNull String url, @NonNull final String file, final ProgressListener listener) {
        return create(url, new File(file), listener);
    }

    public static Subscription create(@NonNull String url, @NonNull final File file) {
        return create(url, file, null);
    }

    public static Subscription create(@NonNull String url, @NonNull final File file, final ProgressListener listener) {
        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new ProgressInterceptor(listener))
                                                              .retryOnConnectionFailure(true)
                                                              .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                                                              .build();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(getBaseUrl(url))
                                                        .client(client)
                                                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                        .build();
        return retrofit.create(DownloadApi.class)
                       .download(url)
                       .map(new Func1<ResponseBody, File>() {
                           @Override public File call(ResponseBody responseBody) {
                               try {
                                   BufferedSink sink = Okio.buffer(Okio.sink(file));
                                   sink.writeAll(responseBody.source());
                                   sink.close();
                                   return file;
                               } catch (Throwable e) {
                                   e.printStackTrace();
                               }
                               return null;
                           }
                       })
                       .compose(RxUtil.<File>applySchedulers(RxUtil.IO_TRANSFORMER))
                       .subscribe(new Observer<File>() {
                           @Override public void onCompleted() { }

                           @Override public void onError(Throwable throwable) {
                               if (null != listener) {
                                   listener.onFailure(throwable);
                               }
                           }

                           @Override public void onNext(File file) {
                               if (null != listener) {
                                   listener.onSuccess(file);
                               }
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
