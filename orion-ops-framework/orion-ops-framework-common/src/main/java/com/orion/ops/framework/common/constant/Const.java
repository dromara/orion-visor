package com.orion.ops.framework.common.constant;

/**
 * 常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 18:49
 */
public interface Const extends com.orion.lang.constant.Const, ConstField {

    Integer NOT_DELETE = 0;

    Integer IS_DELETED = 1;

    int BEARER_PREFIX_LEN = 7;

    int MD5_LEN = 32;

    String UNKNOWN = "未知";

    String INTRANET_IP = "内网IP";

    Long ROOT_MENU_ID = 0L;

    Integer DEFAULT_SORT = 10;

    Long NONE_ID = -1L;

    Integer DEFAULT_VERSION = 1;

}
