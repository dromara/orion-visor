package com.orion.ops.framework.log.core.enums;

/**
 * 日志忽略模式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/21 9:54
 */
public enum IgnoreLogMode {

    /**
     * 不打印任何日志
     */
    ALL,

    /**
     * 不打印参数
     */
    ARGS,

    /**
     * 不打印返回值
     */
    RET,

    /**
     * 不打印参数以及返回值
     */
    ARGS_RET,

    ;

}
