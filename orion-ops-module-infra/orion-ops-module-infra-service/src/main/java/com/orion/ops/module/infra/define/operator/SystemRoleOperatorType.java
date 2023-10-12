package com.orion.ops.module.infra.define.operator;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;
import static com.orion.ops.framework.biz.operator.log.core.holder.OperatorTypeHolder.set;

/**
 * 系统角色 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
public class SystemRoleOperatorType {

    private static final String MODULE = "infra:system-role";

    public static final String CREATE = "system-role:create";

    public static final String UPDATE = "system-role:update";

    public static final String UPDATE_STATUS = "system-role:update-status";

    public static final String DELETE = "system-role:delete";

    public static final String GRANT_MENU = "system-role:grant-menu";

    public static void init() {
        set(new OperatorType(L, MODULE, CREATE, "创建角色 <sb>${name}(${code})</sb>"));
        set(new OperatorType(M, MODULE, UPDATE, "修改角色 <sb>${name}(${code})</sb>"));
        set(new OperatorType(M, MODULE, UPDATE_STATUS, "修改角色状态 <sb>${name}(${code})</sb> - <sb>${statusName}</sb>"));
        set(new OperatorType(H, MODULE, DELETE, "删除角色 <sb>${name}(${code})</sb>"));
        set(new OperatorType(M, MODULE, GRANT_MENU, "分配角色菜单 <sb>${name}(${code})</sb>"));
    }

}
