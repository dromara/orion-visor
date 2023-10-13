package com.orion.ops.framework.biz.operator.log.core.model;

import com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel;
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
public class OperatorType {

    /**
     * 风险等级
     */
    private final OperatorRiskLevel riskLevel;

    /**
     * 模块
     */
    private String module;

    /**
     * 类型
     */
    private final String type;

    /**
     * 模板
     */
    private final String template;

    public OperatorType(OperatorRiskLevel riskLevel, String type, String template) {
        this(riskLevel, null, type, template);
    }

    public OperatorType(OperatorRiskLevel riskLevel, String module, String type, String template) {
        this.riskLevel = riskLevel;
        this.module = module;
        this.type = type;
        this.template = template;
    }

    public void setModule(String module) {
        this.module = module;
    }

}
