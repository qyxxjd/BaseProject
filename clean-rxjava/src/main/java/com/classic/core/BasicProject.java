package com.classic.core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.classic.core.exception.CrashHandler;
import com.classic.core.exception.DefaultCrashProcess;
import com.classic.core.interfaces.ICrashProcess;
import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.Printer;

/**
 * 全局配置
 *
 * @author classic
 * @version v1.0, 2018/4/22 下午4:48
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class BasicProject {

    private static volatile BasicProject sInstance;

    private boolean isDebug;
    private ICrashProcess mExceptionHandler;
    private Integer mLogLevel;
    private Printer[] mPrinters;
    private LogConfiguration mLogConfiguration;

    private BasicProject(Builder builder) {
        isDebug = builder.isDebug;
        mExceptionHandler = builder.mExceptionHandler;
        mLogLevel = builder.mLogLevel;
        mPrinters = builder.mPrinters;
        mLogConfiguration = builder.mLogConfiguration;
        setConfig();
    }

    public static BasicProject config(@NonNull Builder builder) {
        if (sInstance == null) {
            synchronized (BasicProject.class) {
                if (sInstance == null) {
                    sInstance = builder.build();
                }
            }
        }
        return sInstance;
    }

    public static BasicProject getInstance() {
        return sInstance == null ? config(new Builder()) : sInstance;
    }

    private void setConfig() {
        if (null != mExceptionHandler) {
            CrashHandler.getInstance(mExceptionHandler);
        }

        if (null != mPrinters && null != mLogConfiguration) {
            XLog.init(mLogConfiguration, mPrinters);
        } else if (null != mLogLevel && null != mPrinters) {
            XLog.init(mLogLevel, mPrinters);
        } else if (null != mPrinters) {
            XLog.init(mPrinters);
        } else if (null != mLogConfiguration) {
            XLog.init(mLogConfiguration);
        } else if (null != mLogLevel) {
            XLog.init(mLogLevel);
        } else {
            XLog.init();
        }
    }

    public boolean isDebug() {
        return isDebug;
    }

    public static final class Builder {

        private boolean isDebug;
        private Integer mLogLevel;
        private Printer[] mPrinters;
        private ICrashProcess mExceptionHandler;
        private LogConfiguration mLogConfiguration;

        public Builder() { }

        public Builder setDebug(boolean debug) {
            isDebug = debug;
            return this;
        }

        public Builder setExceptionHandler(@NonNull Context context) {
            mExceptionHandler = new DefaultCrashProcess(context.getApplicationContext());
            return this;
        }

        public Builder setExceptionHandler(@NonNull ICrashProcess crashProcess) {
            mExceptionHandler = crashProcess;
            return this;
        }

        /**
         * 日志打印参数配置
         *
         * @param logLevel 日志等级
         */
        public Builder setLog(int logLevel) {
            mLogLevel = logLevel;
            return this;
        }

        /**
         * 日志打印参数配置
         *
         * @param config 日志配置
         */
        public Builder setLog(@NonNull LogConfiguration config) {
            mLogConfiguration = config;
            return this;
        }

        /**
         * 日志打印参数配置
         *
         * @param printers 打印方式配置
         */
        public Builder setLog(@NonNull Printer... printers) {
            mPrinters = printers;
            return this;
        }

        /**
         * 日志打印参数配置
         *
         * @param logLevel 日志等级
         * @param config   日志配置
         */
        public Builder setLog(int logLevel, @NonNull LogConfiguration config) {
            mLogLevel = logLevel;
            mLogConfiguration = config;
            return this;
        }

        /**
         * 日志打印参数配置
         *
         * @param logLevel 日志等级
         * @param printers 打印方式配置
         */
        public Builder setLog(int logLevel, @NonNull Printer... printers) {
            mLogLevel = logLevel;
            mPrinters = printers;
            return this;
        }

        public BasicProject build() {
            return new BasicProject(this);
        }
    }
}
