package com.classic.android.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.utils
 *
 * 文件描述: 单位转换工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/4 17:26
 */
@SuppressWarnings("unused") public final class ConversionUtil {

    private ConversionUtil() { }

    /**
     * dp转px
     */
    public static int dp2px(@NonNull Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                                               context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(@NonNull Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
                                               context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static float px2dp(@NonNull Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(@NonNull Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
