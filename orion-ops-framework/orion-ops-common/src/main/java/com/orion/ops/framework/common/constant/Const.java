package com.orion.ops.framework.common.constant;

/**
 * 常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 18:49
 */
public class Const implements com.orion.lang.constant.Const {

    private Const() {
    }

    /**
     * 同 ${orion.version} 迭代时候需要手动更改
     */
    public static String VERSION = "1.0.0";

    public static final Integer NOT_DELETE = 0;

    public static final Integer IS_DELETED = 1;

    public static final int BEARER_PREFIX_LEN = 7;

    public static final int MD5_LEN = 32;

    public static final String UNKNOWN = "未知";

    public static final String INTRANET_IP = "内网IP";

    public static final Long ROOT_MENU_ID = 0L;

    public static final Integer DEFAULT_SORT = 0;

}
