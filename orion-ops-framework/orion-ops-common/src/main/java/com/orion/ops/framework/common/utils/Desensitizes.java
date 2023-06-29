package com.orion.ops.framework.common.utils;

import com.orion.lang.utils.Strings;

/**
 * 脱敏工具类
 * <p>
 * // FIXME KIT
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/9/10 9:45
 */
public class Desensitizes {

    public static final String REPLACER = "*";

    public static final char REPLACER_CHAR = '*';

    private Desensitizes() {
    }

    public static String mix(String s, int keepStart, int keepEnd) {
        return mix(s, keepStart, keepEnd, REPLACER_CHAR);
    }

    /**
     * 字符串脱敏
     * 脱敏后的长度和原先的长度一样
     *
     * @param s         原字符
     * @param keepStart 开始保留长度
     * @param keepEnd   结束保留长度
     * @param replacer  脱敏字符
     * @return 脱敏字符串
     */
    public static String mix(String s, int keepStart, int keepEnd, char replacer) {
        int length = Strings.length(s);
        if (length == 0) {
            return Strings.EMPTY;
        }

        return mix(s, keepStart, keepEnd, Strings.repeat(replacer, length - keepStart - keepEnd), 1);
    }

    public static String mix(String s, int keepStart, int keepEnd, String replacer) {
        return mix(s, keepStart, keepEnd, replacer, 1);
    }

    /**
     * 字符串脱敏
     *
     * @param s         原字符
     * @param keepStart 开始保留长度
     * @param keepEnd   结束保留长度
     * @param replacer  脱敏字符串
     * @param repeat    脱敏字符串重复次数
     * @return 脱敏字符串
     */
    public static String mix(String s, int keepStart, int keepEnd, String replacer, int repeat) {
        int length = Strings.length(s);
        if (length == 0) {
            return Strings.EMPTY;
        }
        if (keepStart < 0) {
            keepStart = 0;
        }
        if (keepEnd < 0) {
            keepEnd = 0;
        }
        // 保留的长度大于等于文本的长度则不脱敏
        if (keepStart + keepEnd >= length) {
            return s;
        }
        char[] chars = s.toCharArray();
        char[] replacerArr = Strings.repeat(replacer, repeat).toCharArray();
        char[] res = new char[keepStart + keepEnd + replacerArr.length];
        System.arraycopy(chars, 0, res, 0, keepStart);
        System.arraycopy(replacerArr, 0, res, keepStart, replacerArr.length);
        System.arraycopy(chars, chars.length - keepEnd, res, keepStart + replacerArr.length, keepEnd);
        return new String(res);
    }

}
