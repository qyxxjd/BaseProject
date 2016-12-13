package com.classic.android.utils;

import android.support.annotation.NonNull;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.utils
 *
 * 文件描述: 字节工具类(和硬件交互常用的方法)
 * 创 建 人: 续写经典
 * 创建时间: 2016/7/12 21:09
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public final class ByteUtil {

    private ByteUtil() { }

    /**
     * bytes转int
     *
     * @param asc 排序模式。true:按低位到高位顺序排列, false:按高位到低位顺序排列
     */
    public static int bytesToInt(boolean asc, byte... bytes) {
        if (null == bytes) {
            throw new NullPointerException("bytes is null!");
        }
        final int length = bytes.length;
        if (length > 4) {
            throw new IllegalArgumentException("Illegal length!");
        }
        int result = 0;
        if (asc) {
            for (int i = length - 1; i >= 0; i--) {
                result <<= 8;
                result |= (bytes[i] & 0x000000ff);
            }
        } else {
            for (int i = 0; i < length; i++) {
                result <<= 8;
                result |= (bytes[i] & 0x000000ff);
            }
        }
        return result;
    }

    /**
     * int转bytes
     */
    public static byte[] intToBytes(int number) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) (number >>> (24 - i * 8));
        }
        return bytes;
    }

    /**
     * byte[]转十六进制字符串
     */
    public static String bytesToHexString(@NonNull byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < bytes.length; i++) {
            int    v  = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv).append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * 十六进制字符串转byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(@NonNull String hexString) {
        hexString = hexString.toUpperCase();
        int    length   = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d        = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * char转byte
     *
     * @param c char
     * @return byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
