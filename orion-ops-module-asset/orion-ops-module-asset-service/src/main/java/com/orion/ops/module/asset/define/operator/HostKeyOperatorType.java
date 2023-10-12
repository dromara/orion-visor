package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.H;
import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.L;
import static com.orion.ops.framework.biz.operator.log.core.holder.OperatorTypeHolder.set;

/**
 * 主机秘钥 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
public class HostKeyOperatorType {

    private static final String MODULE = "asset:host-key";

    public static final String CREATE = "host-key:create";

    public static final String UPDATE = "host-key:update";

    public static final String DELETE = "host-key:delete";

    public static void init() {
        set(new OperatorType(L, MODULE, CREATE, "创建主机秘钥 <sb>${name}</sb>"));
        set(new OperatorType(L, MODULE, UPDATE, "修改主机秘钥 <sb>${name}</sb>"));
        set(new OperatorType(H, MODULE, DELETE, "删除主机秘钥 <sb>${name}</sb>"));
    }

}
