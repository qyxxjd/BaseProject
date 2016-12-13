package com.classic.android.utils;

import android.database.Cursor;
import android.support.annotation.NonNull;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.utils
 *
 * 文件描述: Cursor工具类
 * 创 建 人: 续写经典
 * 创建时间: 2016/6/17 17:26
 */
@SuppressWarnings("unused") public final class CursorUtil {

    private CursorUtil() { }

    public static String getString(@NonNull Cursor cursor, @NonNull String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static long getLong(@NonNull Cursor cursor, @NonNull String columnName) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }

    public static double getDouble(@NonNull Cursor cursor, @NonNull String columnName) {
        return cursor.getDouble(cursor.getColumnIndexOrThrow(columnName));
    }

    public static float getFloat(@NonNull Cursor cursor, @NonNull String columnName) {
        return cursor.getFloat(cursor.getColumnIndexOrThrow(columnName));
    }

    public static short getShort(@NonNull Cursor cursor, @NonNull String columnName) {
        return cursor.getShort(cursor.getColumnIndexOrThrow(columnName));
    }

    public static int getInt(@NonNull Cursor cursor, @NonNull String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public static byte[] getBlob(@NonNull Cursor cursor, @NonNull String columnName) {
        return cursor.getBlob(cursor.getColumnIndexOrThrow(columnName));
    }
}
