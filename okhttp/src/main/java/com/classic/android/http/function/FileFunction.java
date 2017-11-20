package com.classic.android.http.function;

import android.support.annotation.NonNull;

import java.io.File;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.http.function
 *
 * 文件描述: 文件解析Function
 * 创 建 人: 续写经典
 * 创建时间: 2016/8/1 13:39
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FileFunction implements Function<ResponseBody, File> {

    private final File mFile;

    public FileFunction(@NonNull String path) {
        this(new File(path));
    }

    public FileFunction(@NonNull File file) {
        mFile = file;
    }

    @Override
    public File apply(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
        if (!mFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            mFile.createNewFile();
        }
        BufferedSink sink = Okio.buffer(Okio.sink(mFile));
        sink.writeAll(responseBody.source());
        sink.close();
        return mFile;
    }
}
