package com.classic.android.utils;

import android.support.annotation.NonNull;

/**
 * HTML处理类
 *
 * @author 还如一梦中
 * @version v1.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class HtmlUtil {

    private HtmlUtil() { }

    /**
     * 描述：替换标记以正常显示.
     */
    public static String replaceTag(@NonNull String text) {
        if (!hasSpecialChars(text)) {
            return text;
        }
        StringBuilder filtered = new StringBuilder(text.length());
        char c;
        for (int i = 0; i <= text.length() - 1; i++) {
            c = text.charAt(i);
            switch (c) {
                case '<':
                    filtered.append("&lt;");
                    break;
                case '>':
                    filtered.append("&gt;");
                    break;
                case '"':
                    filtered.append("&quot;");
                    break;
                case '&':
                    filtered.append("&amp;");
                    break;
                default:
                    filtered.append(c);
            }
        }
        return filtered.toString();
    }

    /**
     * 描述：判断标记是否存在.
     */
    public static boolean hasSpecialChars(@NonNull String text) {
        boolean flag = false;
        char c;
        for (int i = 0; i <= text.length() - 1; i++) {
            c = text.charAt(i);
            switch (c) {
                case '>':
                    flag = true;
                    break;
                case '<':
                    flag = true;
                    break;
                case '"':
                    flag = true;
                    break;
                case '&':
                    flag = true;
                    break;
                default:
                    flag = false;
                    break;
            }
        }
        return flag;
    }
}
