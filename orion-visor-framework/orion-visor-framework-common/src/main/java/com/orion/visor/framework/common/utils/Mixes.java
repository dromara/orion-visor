package com.orion.visor.framework.common.utils;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.crypto.Caesars;

/**
 * 混淆工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/17 18:27
 */
public class Mixes {

    private Mixes() {
    }

    /**
     * 混淆
     * <p>
     * 此方法不可修改
     *
     * @param str str
     * @return str
     */
    public static String obfuscate(String str) {
        char[] chars = str.toCharArray();
        Arrays1.reverse(chars);
        for (int i = 0; i < chars.length; i += 2) {
            char temp = chars[i];
            chars[i] = chars[i + 1];
            chars[i + 1] = temp;
        }
        String res = new String(chars);
        return new Caesars().encrypt(res);
    }

}
