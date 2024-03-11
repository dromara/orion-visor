package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.H;
import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.M;

/**
 * 批量执行 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Module("asset:exec")
public class ExecOperatorType extends InitializingOperatorTypes {

    public static final String START = "exec:start";

    public static final String DELETE_LOG = "exec:delete-log";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(M, START, "批量执行主机命令"),
                new OperatorType(H, DELETE_LOG, "删除批量执行日志"),
        };
    }

}
