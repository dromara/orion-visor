package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 计划执行任务主机 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Module("asset:exec-job-host")
public class ExecJobHostOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "exec-job-host:create";

    public static final String UPDATE = "exec-job-host:update";

    public static final String DELETE = "exec-job-host:delete";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建计划执行任务主机"),
                new OperatorType(M, UPDATE, "更新计划执行任务主机"),
                new OperatorType(H, DELETE, "删除计划执行任务主机"),
        };
    }

}
