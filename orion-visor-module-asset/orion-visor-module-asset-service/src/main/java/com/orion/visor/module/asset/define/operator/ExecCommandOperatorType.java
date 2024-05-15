package com.orion.visor.module.asset.define.operator;

import com.orion.visor.framework.biz.operator.log.core.annotation.Module;
import com.orion.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.M;

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

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(M, EXEC, "执行主机命令"),
        };
    }

}
