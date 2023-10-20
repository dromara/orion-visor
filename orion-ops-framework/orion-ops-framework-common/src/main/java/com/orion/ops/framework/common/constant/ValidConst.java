package com.orion.ops.framework.common.constant;

/**
 * 验证常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/20 11:45
 */
public interface ValidConst {

    String CHAR_NUMBER_1_32_PATTERN = "^[a-zA-Z0-9]{1,32}$";

    String CHAR_NUMBER_1_32_MESSAGE = "只能为 1-32 位的数字或字母";

    String CHAR_NUMBER_2_32_PATTERN = "^[a-zA-Z0-9]{2,32}$";

    String CHAR_NUMBER_2_32_MESSAGE = "只能为 2-32 位的数字或字母";

    String CHAR_NUMBER_4_32_PATTERN = "^[a-zA-Z0-9]{4,32}$";

    String CHAR_NUMBER_4_32_MESSAGE = "只能为 4-32 位的数字或字母";

}
