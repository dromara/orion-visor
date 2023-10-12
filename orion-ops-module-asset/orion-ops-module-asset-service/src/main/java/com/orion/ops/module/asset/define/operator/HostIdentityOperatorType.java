package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.H;
import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.L;
import static com.orion.ops.framework.biz.operator.log.core.holder.OperatorTypeHolder.set;

/**
 * 主机身份 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
public class HostIdentityOperatorType {

    private static final String MODULE = "asset:host-identity";

    public static final String CREATE = "host-identity:create";

    public static final String UPDATE = "host-identity:update";

    public static final String DELETE = "host-identity:delete";

    public static void init() {
        set(new OperatorType(L, MODULE, CREATE, "创建主机身份 <sb>${name}</sb>"));
        set(new OperatorType(L, MODULE, UPDATE, "修改主机身份 <sb>${name}</sb>"));
        set(new OperatorType(H, MODULE, DELETE, "删除主机身份 <sb>${name}</sb>"));
    }

}
