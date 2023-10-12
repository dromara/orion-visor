package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;
import static com.orion.ops.framework.biz.operator.log.core.holder.OperatorTypeHolder.set;

/**
 * 主机 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
public class HostOperatorType {

    private static final String MODULE = "asset:host";

    public static final String CREATE = "host:create";

    public static final String UPDATE = "host:update";

    public static final String DELETE = "host:delete";

    public static final String UPDATE_CONFIG = "host:update-config";

    public static final String UPDATE_CONFIG_STATUS = "host:update-config-status";

    public static void init() {
        set(new OperatorType(L, MODULE, CREATE, "创建主机 <sb>${name}</sb>"));
        set(new OperatorType(L, MODULE, UPDATE, "修改主机 <sb>${name}</sb>"));
        set(new OperatorType(H, MODULE, DELETE, "删除主机 <sb>${name}</sb>"));
        set(new OperatorType(M, MODULE, UPDATE_CONFIG, "修改主机配置 <sb>${name}</sb> | <sb>${type}</sb>"));
        set(new OperatorType(M, MODULE, UPDATE_CONFIG_STATUS, "修改主机配置状态 <sb>${name}</sb> | <sb>${type}</sb> - <sb>${statusName}</sb>"));
    }

}
