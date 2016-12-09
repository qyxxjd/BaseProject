package com.classic.simple.app;

import android.app.Application;

import com.classic.core.BasicProject;
import com.classic.simple.BuildConfig;
import com.elvishew.xlog.LogLevel;

public class ClassicApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        final BasicProject.Builder builder = new BasicProject.Builder()
                .setDebug(BuildConfig.DEBUG)
                .setRootDirectoryName(getPackageName())
                //自定义异常信息处理，实现ICrashProcess
                //.setExceptionHandler(new CustomCrashProcessImpl())
                .setLog(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);

        BasicProject.config(builder);
    }
}
