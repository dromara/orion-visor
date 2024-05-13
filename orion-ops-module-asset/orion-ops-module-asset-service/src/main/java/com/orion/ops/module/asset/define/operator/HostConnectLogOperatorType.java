package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.H;
import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.M;

/**
 * 主机连接日志 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/2 14:37
 */
@Module("asset:host-connect-log")
public class HostConnectLogOperatorType extends InitializingOperatorTypes {

    public static final String DELETE = "host-connect-log:delete";

    public static final String CLEAR = "host-connect-log:clear";

    public static final String FORCE_OFFLINE = "host-connect-log:force-offline";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(H, DELETE, "删除主机连接记录 <sb>${count}</sb> 条"),
                new OperatorType(H, CLEAR, "清空主机连接记录 <sb>${count}</sb> 条"),
                new OperatorType(M, FORCE_OFFLINE, "强制下线主机连接 <sb>${hostName}</sb>"),
        };
    }

}
