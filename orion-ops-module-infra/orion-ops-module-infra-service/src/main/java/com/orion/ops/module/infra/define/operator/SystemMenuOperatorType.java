package com.orion.ops.module.infra.define.operator;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;
import static com.orion.ops.framework.biz.operator.log.core.holder.OperatorTypeHolder.set;

/**
 * 系统菜单 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
public class SystemMenuOperatorType {

    private static final String MODULE = "infra:system-menu";

    public static final String CREATE = "system-menu:create";

    public static final String UPDATE = "system-menu:update";

    public static final String UPDATE_STATUS = "system-menu:update-status";

    public static final String DELETE = "system-menu:delete";

    public static void init() {
        set(new OperatorType(L, MODULE, CREATE, "创建菜单 <sb>${name}</sb>"));
        set(new OperatorType(L, MODULE, UPDATE, "修改菜单 <sb>${name}</sb>"));
        set(new OperatorType(M, MODULE, UPDATE_STATUS, "修改菜单状态 <sb>${name}</sb> - <sb>${label}</sb>"));
        set(new OperatorType(H, MODULE, DELETE, "删除菜单 <sb>${name}</sb>"));
    }

}
