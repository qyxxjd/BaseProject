package com.classic.core.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.utils
 *
 * 文件描述: 跳转到对应的系统界面
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/4 17:26
 */
@SuppressWarnings("unused") public final class IntentUtil {
    private static final String TAG = "IntentUtil";

    private IntentUtil() { }

    /** 进入拨号界面 */
    public static void dial(@NonNull Context context, @NonNull String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 直接拨号
     * 需要权限：android.permission.CALL_PHONE
     */
    public static void call(@NonNull Context context, @NonNull String phoneNumber) {
        Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) ==
            PackageManager.PERMISSION_GRANTED) {
            context.startActivity(intentPhone);
        } else {
            Log.e(TAG, "no permission: android.permission.CALL_PHONE");
        }
    }

    /** 用浏览器打开url */
    public static void browser(@NonNull Context context, @NonNull String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    /** 用系统浏览器打开url */
    public static void browserBySystem(@NonNull Context context, @NonNull String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        context.startActivity(intent);
    }

    /** 发送短信 */
    public static void sendSms(@NonNull Context context, @NonNull String smsBody) {
        Uri    smsToUri = Uri.parse("smsto:");
        Intent intent   = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        context.startActivity(intent);
    }

    /** 发送短信 */
    public static void sendSms(@NonNull Context context, @NonNull String phone,
                               @NonNull String smsBody) {
        Uri    smsToUri = Uri.parse("smsto:" + phone);
        Intent intent   = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        context.startActivity(intent);
    }

    /** 跳转到设置界面 */
    public static void setting(@NonNull Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
    }

    /** 跳转到网络设置界面 */
    public static void settingNetwork(@NonNull Context context) {
        Intent intent;
        if (Build.VERSION.SDK_INT > 10) {
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
        }
        context.startActivity(intent);
    }

    /**
     * 跳转到系统程序详细信息界面
     */
    public static void startInstalledAppDetails(
            @NonNull Context context, @NonNull String packageName) {
        Intent intent     = new Intent();
        int    sdkVersion = Build.VERSION.SDK_INT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", packageName, null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra((sdkVersion == Build.VERSION_CODES.FROYO ?
                             "pkg" : "com.android.settings.ApplicationPkgName"),
                            packageName);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 分享图片
     *
     * @param title    标题
     * @param imageUri 图片uri
     */
    public static void shareImage(
            @NonNull Context context, @NonNull String title, @NonNull Uri imageUri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

    /**
     * 分享文本
     *
     * @param titie   标题
     * @param subject 主题
     * @param content 内容
     */
    public static void shareText(
            @NonNull Context context,
            @NonNull String titie, @NonNull String subject, @NonNull String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, titie));
    }

    /**
     * 安装应用
     *
     * @param apkPath   apk路径
     * @param authority Android 7.0以上用到的参数，7.0以下可以传:null
     *                  The authority of a {@link FileProvider} defined in a
     *                  {@code <provider>} element in your app's manifest.
     */
    public static void installApp(
            @NonNull Context context, @NonNull String apkPath, String authority) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri  uri;
        File file = new File(apkPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), authority, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 查看图片
     *
     * @param authority Android 7.0以上用到的参数，7.0以下可以传:null
     *                  The authority of a {@link FileProvider} defined in a
     *                  {@code <provider>} element in your app's manifest.
     */
    public static void viewPicture(@NonNull Context context, @NonNull File file, String authority) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, authority, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
