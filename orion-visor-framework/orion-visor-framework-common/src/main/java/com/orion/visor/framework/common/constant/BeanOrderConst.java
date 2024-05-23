package com.orion.visor.framework.common.constant;

/**
 * bean 排序常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 16:09
 */
public interface BeanOrderConst {

    /**
     * 公共返回值包装处理器
     */
    int RESPONSE_ADVICE_WRAPPER = Integer.MIN_VALUE + 1000;

    /**
     * 演示模式切面
     */
    int DEMO_DISABLE_API_ASPECT = 20;

    /**
     * 操作日志切面
     */
    int OPERATOR_LOG_ASPECT = 30;

}
