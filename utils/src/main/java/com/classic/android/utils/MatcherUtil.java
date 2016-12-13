package com.classic.android.utils;

import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.utils
 *
 * 文件描述: 正则匹配工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/3 17:26
 */
@SuppressWarnings("unused") public final class MatcherUtil {

    private MatcherUtil() { }

    /**
     * 手机号验证
     *
     * @return 验证通过返回true
     */
    public static boolean isMobile(@NonNull String str) {
        Pattern p = Pattern.compile("^[1][3,4,5,6,7,8][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * email格式验证
     */
    public static boolean isEmail(@NonNull String email) {
        String emailPattern
                = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(emailPattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 电话号码验证
     *
     * @return 验证通过返回true
     */
    public static boolean isPhone(@NonNull String str) {
        Matcher m;
        boolean b;
        Pattern p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
        Pattern p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }
}
