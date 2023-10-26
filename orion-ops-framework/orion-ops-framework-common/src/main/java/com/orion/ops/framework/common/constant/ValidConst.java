package com.orion.ops.framework.common.constant;

/**
 * 验证常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/20 11:45
 */
public interface ValidConst {

    String USERNAME_4_32_PATTERN = "^[a-zA-Z0-9_]{4,32}$";

    String USERNAME_4_32_MESSAGE = "只能为 4-32 位的数字,字母或下滑线";

    String DICT_1_32_PATTERN = "^[a-zA-Z0-9_]{1,32}$";

    String DICT_1_32_MESSAGE = "只能为 1-32 位的数字,字母或下滑线";

}
