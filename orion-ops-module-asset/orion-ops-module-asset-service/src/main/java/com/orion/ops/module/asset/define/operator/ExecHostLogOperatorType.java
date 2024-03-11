package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 批量执行主机日志 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 14:05
 */
@Module("asset:exec-host-log")
public class ExecHostLogOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "exec-host-log:create";

    public static final String UPDATE = "exec-host-log:update";

    public static final String DELETE = "exec-host-log:delete";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建批量执行主机日志"),
                new OperatorType(M, UPDATE, "更新批量执行主机日志"),
                new OperatorType(H, DELETE, "删除批量执行主机日志"),
        };
    }

}
