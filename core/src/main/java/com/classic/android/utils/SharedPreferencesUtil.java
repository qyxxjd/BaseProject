package com.classic.android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.utils
 *
 * 文件描述: 偏好参数存储工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/3 17:26
 */
@SuppressWarnings("unused") public class SharedPreferencesUtil {

    private final SharedPreferences mSharedPreferences;
    private final Editor            mEditor;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesUtil(@NonNull Context context, @NonNull String name) {
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public SharedPreferences getSP() {
        return mSharedPreferences;
    }

    public Editor getEditor() {
        return mEditor;
    }

    /**
     * 存储数据(Long)
     */
    public void putLongValue(@NonNull String key, long value) {
        mEditor.putLong(key, value).commit();
    }

    /**
     * 存储数据(Int)
     */
    public void putIntValue(@NonNull String key, int value) {
        mEditor.putInt(key, value).commit();
    }

    /**
     * 存储数据(String)
     */
    public void putStringValue(@NonNull String key, @NonNull String value) {
        mEditor.putString(key, value).commit();
    }

    /**
     * 存储数据(boolean)
     */
    public void putBooleanValue(@NonNull String key, boolean value) {
        mEditor.putBoolean(key, value).commit();
    }

    /**
     * 取出数据(Long)
     */
    public long getLongValue(@NonNull String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    /**
     * 取出数据(int)
     */
    public int getIntValue(@NonNull String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    /**
     * 取出数据(boolean)
     */
    public boolean getBooleanValue(@NonNull String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 取出数据(String)
     */
    public String getStringValue(@NonNull String key, @NonNull String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    /**
     * 清空所有数据
     */
    public void clear() {
        mEditor.clear().commit();
    }

    /**
     * 移除指定数据
     */
    public void remove(String key) {
        mEditor.remove(key).commit();
    }
}
