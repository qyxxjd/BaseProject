package com.classic.core.exception.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;

import com.classic.core.interfaces.ICrashProcess;
import com.classic.core.utils.SDCardUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 应用名称: AndroidBasicProject
 * 包 名 称: com.classic.core.exception.impl
 *
 * 文件描述: 默认崩溃日志处理
 * 创 建 人: 续写经典
 * 创建时间: 2016/6/21 14:28
 */
public class DefaultCrashProcess implements ICrashProcess {

    private static final String CHARSET_NAME    = "UTF-8";
    private static final String LOG_NAME_PREFIX = "crash_";
    private static final String LOG_NAME_SUFFIX = ".log";

    private final SimpleDateFormat mDateFormat;
    private final SimpleDateFormat mTimeFormat;
    private final Context          mContext;

    public DefaultCrashProcess(@NonNull Context context) {
        mContext = context.getApplicationContext();
        mDateFormat = new SimpleDateFormat("yyyyMM", Locale.CHINA);
        mTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    }

    @Override public void onException(Thread thread, Throwable exception) throws Exception {
        //noinspection StringBufferReplaceableByString
        final StringBuilder sb = new StringBuilder(LOG_NAME_PREFIX).append(
                mDateFormat.format(new Date(System.currentTimeMillis()))).append(LOG_NAME_SUFFIX);
        final File file = new File(SDCardUtil.getLogDirPath(), sb.toString());
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        }
        //PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), CHARSET_NAME));
        // 导出手机信息
        savePhoneInfo(pw);
        // 导出发生异常的时间
        pw.println(mTimeFormat.format(new Date(System.currentTimeMillis())));
        pw.println();
        // 导出异常的调用栈信息
        exception.printStackTrace(pw);
        pw.println();
        pw.close();
    }

    private void savePhoneInfo(PrintWriter pw) throws Exception {
        // 应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                                           PackageManager.GET_ACTIVITIES);
        pw.println("设备信息：");
        pw.print("App Version Name: ");
        pw.println(pi.versionName);
        pw.print("App Version Code: ");
        pw.println(pi.versionCode);

        // android版本号
        pw.print("SDK: ");
        pw.println(Build.VERSION.SDK_INT);
        pw.print("OS Version: ");
        pw.println(Build.VERSION.RELEASE);

        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        // cpu架构
        pw.print("CPU ABI: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //noinspection SpellCheckingInspection
            final String[] abis = Build.SUPPORTED_ABIS;
            if (null != abis && abis.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (String abi : abis) {
                    sb.append(abi).append(" ");
                }
                pw.println(sb.toString());
            }
        } else {
            //noinspection deprecation
            pw.println(Build.CPU_ABI);
        }
        pw.println("------------------------------------");
        pw.println();
    }
}
