package com.classic.http.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.http.download
 *
 * 文件描述: 扩展了下载进度回调的ResponseBody {https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/Progress.java}
 * 创 建 人: 续写经典
 * 创建时间: 2016/12/22 10:11
 *
 */
class ProgressResponseBody extends ResponseBody {
    private ResponseBody     mResponseBody;
    private ProgressListener mProgressListener;
    private BufferedSource   mBufferedSource;

    ProgressResponseBody(ResponseBody responseBody,
                         ProgressListener progressListener) {
        this.mResponseBody = responseBody;
        this.mProgressListener = progressListener;
    }

    @Override public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;

                if (null != mProgressListener) {
                    mProgressListener.onProgress(totalBytesRead, mResponseBody.contentLength(),
                            bytesRead == -1);
                }
                return bytesRead;
            }
        };
    }
}
