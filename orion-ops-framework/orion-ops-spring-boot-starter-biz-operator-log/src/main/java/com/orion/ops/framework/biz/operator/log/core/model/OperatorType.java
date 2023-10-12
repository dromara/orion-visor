package com.orion.ops.framework.biz.operator.log.core.model;

import com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型定义
 * <p>
 * 因为枚举需要实现 注解中不可以使用 则需要使用对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 10:29
 */
@Getter
@AllArgsConstructor
public class OperatorType {

    /**
     * 风险等级
     */
    private final OperatorRiskLevel riskLevel;

    /**
     * 模块
     */
    private final String module;

    /**
     * 类型
     */
    private final String type;

    /**
     * 模板
     */
    private final String template;

}
