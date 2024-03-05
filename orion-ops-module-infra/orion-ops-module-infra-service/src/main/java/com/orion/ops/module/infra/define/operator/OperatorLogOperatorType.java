package com.orion.ops.module.infra.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.H;

/**
 * 操作日志 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-3-4 16:20
 */
@Module("infra:operator-log")
public class OperatorLogOperatorType extends InitializingOperatorTypes {

    public static final String DELETE = "operator-log:delete";

    public static final String CLEAR = "operator-log:clear";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(H, DELETE, "删除操作日志 <sb>${count}</sb> 条"),
                new OperatorType(H, CLEAR, "清空操作日志 <sb>${count}</sb> 条"),
        };
    }

}
