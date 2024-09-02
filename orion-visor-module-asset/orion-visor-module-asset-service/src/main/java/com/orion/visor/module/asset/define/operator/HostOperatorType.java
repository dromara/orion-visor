package com.orion.visor.module.asset.define.operator;

import com.orion.visor.framework.biz.operator.log.core.annotation.Module;
import com.orion.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 主机 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("asset:host")
public class HostOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "host:create";

    public static final String UPDATE = "host:update";

    public static final String DELETE = "host:delete";

    public static final String UPDATE_STATUS = "host:update-status";

    public static final String UPDATE_CONFIG = "host:update-config";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建主机 <sb>${name}</sb>"),
                new OperatorType(L, UPDATE, "修改主机 <sb>${name}</sb>"),
                new OperatorType(H, DELETE, "删除主机 <sb>${count}</sb> 条"),
                new OperatorType(M, UPDATE_STATUS, "修改主机状态 <sb>${name}</sb> - <sb>${status}</sb>"),
                new OperatorType(M, UPDATE_CONFIG, "修改主机配置 <sb>${name}</sb>"),
        };
    }

}
