package com.classic.http.download;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.http.download
 *
 * 文件描述: 下载接口
 * 创 建 人: 续写经典
 * 创建时间: 2016/12/22 10:51
 */
public interface DownloadApi {

    @GET @Streaming Observable<ResponseBody> download(@Url String url);
}
