package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.L;

/**
 * 主机终端 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("asset:host-terminal")
public class HostTerminalOperatorType extends InitializingOperatorTypes {

    public static final String ACCESS = "host-terminal:access";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, ACCESS, "连接主机终端 <sb>${hostName}</sb>"),
        };
    }

}
