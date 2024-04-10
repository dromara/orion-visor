package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.M;

/**
 * 批量执行 操作记录类型
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Module("asset:exec-command")
public class ExecCommandOperatorType extends InitializingOperatorTypes {

    public static final String EXEC = "exec-command:exec";

    public static final String INTERRUPT_EXEC = "exec-command:interrupt-exec";

    public static final String INTERRUPT_HOST = "exec-command:interrupt-host";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(M, EXEC, "执行主机命令"),
                new OperatorType(M, INTERRUPT_EXEC, "中断执行命令"),
                new OperatorType(M, INTERRUPT_HOST, "中断主机执行命令 <sb>${logId}</sb> <sb>${hostName}</sb>"),
        };
    }

}
