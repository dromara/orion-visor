package com.orion.ops.module.infra.define.operator;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;
import static com.orion.ops.framework.biz.operator.log.core.holder.OperatorTypeHolder.set;

/**
 * 系统用户 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
public class SystemUserOperatorType {

    private static final String MODULE = "infra:system-user";

    public static final String CREATE = "system-user:create";

    public static final String UPDATE = "system-user:update";

    public static final String UPDATE_STATUS = "system-user:update-status";

    public static final String GRANT_ROLE = "system-user:grant-role";

    public static final String RESET_PASSWORD = "system-user:reset-password";

    public static final String DELETE = "system-user:delete";

    public static void init() {
        set(new OperatorType(L, MODULE, CREATE, "创建用户 <sb>${username}</sb>"));
        set(new OperatorType(M, MODULE, UPDATE, "修改用户 <sb>${username}</sb>"));
        set(new OperatorType(M, MODULE, UPDATE_STATUS, "修改用户状态 <sb>${username}</sb> - <sb>${statusName}</sb>"));
        set(new OperatorType(M, MODULE, GRANT_ROLE, "用户分配角色 <sb>${username}</sb>"));
        set(new OperatorType(H, MODULE, RESET_PASSWORD, "重置用户密码 <sb>${username}</sb>"));
        set(new OperatorType(H, MODULE, DELETE, "删除用户 <sb>${username}</sb>"));
    }

}
