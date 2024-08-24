package com.orion.visor.framework.common.constant;

/**
 * 常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 18:49
 */
public interface Const extends com.orion.lang.constant.Const, FieldConst, CnConst {

    Integer NOT_DELETE = 0;

    Integer IS_DELETED = 1;

    int BEARER_PREFIX_LEN = 7;

    int MD5_LEN = 32;

    Long ROOT_PARENT_ID = 0L;

    Integer DEFAULT_SORT = 10;

    Long NONE_ID = -1L;

    Integer DEFAULT_VERSION = 1;

    Long SYSTEM_USER_ID = 0L;

    String SYSTEM_USERNAME = "system";

    int BATCH_COUNT = 500;

}
