package com.classic.core.utils;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.utils
 *
 * 文件描述: 字节、毫秒相关常量
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/3 17:26
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public final class SizeUtil {

    private SizeUtil() { }

    /**
     * GB转字节
     */
    public static final long GB_2_BYTE   = 1073741824;
    /**
     * MB转字节
     */
    public static final long MB_2_BYTE   = 1048576;
    /**
     * KB转字节
     */
    public static final long KB_2_BYTE   = 1024;
    /**
     * 秒转毫秒
     */
    public static final long SECOND_2_MS = 1000;
    /**
     * 分钟转毫秒
     */
    public static final long MINUTE_2_MS = 60000;
    /**
     * 小时转毫秒
     */
    public static final long HOUR_2_MS   = 3600000;
    /**
     * 天转毫秒
     */
    public static final long DAY_2_MS    = 86400000;
}
