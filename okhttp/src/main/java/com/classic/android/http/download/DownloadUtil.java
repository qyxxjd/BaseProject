package com.classic.android.http.download;

import android.support.annotation.NonNull;
import android.util.Log;

import com.classic.android.rx.RxUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
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
@SuppressWarnings("unused") public final class DownloadUtil {
    private static final int    TIMEOUT   = 30;
    private static final String SEPARATOR = "/";

    private DownloadUtil() {
        // no instance
    }

    public static void create(@NonNull String url, @NonNull final String file) {
        create(url, new File(file), null);
    }

    public static void create(@NonNull String url, @NonNull final String file,
                              final ProgressListener listener) {
        create(url, new File(file), listener);
    }

    public static void create(@NonNull String url, @NonNull final File file) {
        create(url, file, null);
    }

    @SuppressWarnings("WeakerAccess")
    public static void create(@NonNull String url, @NonNull final File file,
                              final ProgressListener listener) {
        Log.d("DownloadUtil", "baseUrl:"+getBaseUrl(url));
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ProgressInterceptor(listener))
                .retryOnConnectionFailure(true)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl(url))
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        retrofit.create(DownloadApi.class)
                .download(url)
                .map(RESPONSE_BODY_TO_INPUT_STREAM)
                .compose(RxUtil.<InputStream>applySchedulers(RxUtil.IO_TRANSFORMER))
                .subscribe(new Observer<InputStream>() {
                    OutputStream os = null;
                    Disposable disposable;
                    boolean isError;
                    @Override public void onSubscribe(Disposable d) {
                        this.disposable = d;
                        try {
                            os = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            error(e);
                        }
                    }

                    @Override public void onNext(InputStream inputStream) {
                        int length;
                        byte[] data = new byte[4096];
                        try {
                            while ((length = inputStream.read(data)) != -1) {
                                os.write(data, 0, length);
                            }
                        } catch (IOException e) {
                            error(e);
                        }
                    }

                    @Override public void onError(Throwable e) {
                        listener.onFailure(e);
                        release();
                    }

                    @Override public void onComplete() {
                        if (null != listener && !isError) {
                            listener.onSuccess(file);
                        }
                        release();
                    }

                    private void release() {
                        if (null != os) {
                            //noinspection EmptyCatchBlock
                            try {
                                os.close();
                                os = null;
                            } catch (IOException e) { }
                        }
                        if (!disposable.isDisposed()) {
                            disposable.dispose();
                        }
                    }

                    private void error(Throwable e) {
                        isError = true;
                        if (null != listener) {
                            listener.onFailure(e);
                        }
                    }
                });
    }

    private static final Function<ResponseBody, InputStream> RESPONSE_BODY_TO_INPUT_STREAM
            = new Function<ResponseBody, InputStream>() {
        @Override public InputStream apply(ResponseBody responseBody) throws Exception {
            return responseBody.byteStream();
        }
    };

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
