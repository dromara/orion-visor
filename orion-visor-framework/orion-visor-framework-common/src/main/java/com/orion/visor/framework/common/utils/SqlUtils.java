package com.orion.visor.framework.common.utils;

import com.orion.visor.framework.common.constant.Const;

/**
 * sql 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/8/26 16:03
 */
public class SqlUtils {

    private SqlUtils() {
    }

    /**
     * limit n
     *
     * @param limit limit
     * @return limit
     */
    public static String limit(Number limit) {
        return Const.LIMIT + Const.SPACE + limit;
    }

    /**
     * limit offset limit
     *
     * @param offset offset
     * @param limit  limit
     * @return limit
     */
    public static String limit(Number offset, Number limit) {
        return Const.LIMIT + Const.SPACE + offset + Const.COMMA + limit;
    }

}
