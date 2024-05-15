package com.orion.visor.module.infra.define.operator;

import com.orion.visor.framework.biz.operator.log.core.annotation.Module;
import com.orion.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 系统角色 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("infra:system-role")
public class SystemRoleOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "system-role:create";

    public static final String UPDATE = "system-role:update";

    public static final String UPDATE_STATUS = "system-role:update-status";

    public static final String DELETE = "system-role:delete";

    public static final String GRANT_MENU = "system-role:grant-menu";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建角色 <sb>${name}(${code})</sb>"),
                new OperatorType(M, UPDATE, "修改角色 <sb>${name}(${code})</sb>"),
                new OperatorType(M, UPDATE_STATUS, "修改角色状态 <sb>${name}(${code})</sb> - <sb>${statusName}</sb>"),
                new OperatorType(H, DELETE, "删除角色 <sb>${name}(${code})</sb>"),
                new OperatorType(M, GRANT_MENU, "分配角色菜单 <sb>${name}(${code})</sb>"),
        };
    }

}
